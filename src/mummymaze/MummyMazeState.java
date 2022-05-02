package mummymaze;

import agent.Action;
import agent.State;
import java.util.ArrayList;
import java.util.Arrays;

public class MummyMazeState extends State implements Cloneable {

    private final char[][] matrix;
    private int lineHero;
    private int columnHero;

    public MummyMazeState(char[][] matrix) {
        this.matrix = new char[matrix.length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                this.matrix[i][j] = matrix[i][j];
                if (this.matrix[i][j] == 'H') {
                    lineHero = i;
                    columnHero = j;
                }
            }
        }
    }

    @Override
    public void executeAction(Action action) {
        action.execute(this);
        firePuzzleChanged(null);
    }

    public boolean canMoveUp() {
        return lineHero != 0;
    }

    public boolean canMoveRight() {
        return columnHero != matrix.length - 1;
    }

    public boolean canMoveDown() {
        return lineHero != matrix.length - 1;
    }

    public boolean canMoveLeft() {
        return columnHero != 0;
    }

    /*
     * In the next four methods we don't verify if the actions are valid.
     * This is done in method executeActions in class EightPuzzleProblem.
     * Doing the verification in these methods would imply that a clone of the
     * state was created whether the operation could be executed or not.
     */
    public void moveUp() {
        matrix[lineHero][columnHero] = matrix[--lineHero][columnHero];
        matrix[lineHero][columnHero] = 0;
    }

    public void moveRight() {
        matrix[lineHero][columnHero] = matrix[lineHero][++columnHero];
        matrix[lineHero][columnHero] = 0;
    }

    public void moveDown() {
        matrix[lineHero][columnHero] = matrix[++lineHero][columnHero];
        matrix[lineHero][columnHero] = 0;
    }

    public void moveLeft() {
        matrix[lineHero][columnHero] = matrix[lineHero][--columnHero];
        matrix[lineHero][columnHero] = 0;
    }

    //no need for finalState, we can already compare with door
    public double computeTilesOutOfPlace() {
        double h = 0;

        return h;
    }

    //not relevant for now, start with largura and profundidade que não precisam de heuristica
    //no need for finalState, we can already compare with door
    public double computeTileDistances() {
        double h = 0;

        return h;
    }

    public char[][] getMatrix() {
        return matrix;
    }

    public int getNumLines() {
        return matrix.length;
    }

    public int getNumColumns() {
        return matrix[0].length;
    }

    public int getTileValue(int line, int column) {
        if (!isValidPosition(line, column)) {
            throw new IndexOutOfBoundsException("Invalid position!");
        }
        return matrix[line][column];
    }

    public boolean isValidPosition(int line, int column) {
        return line >= 0 && line < matrix.length && column >= 0 && column < matrix[0].length;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MummyMazeState)) {
            return false;
        }

        MummyMazeState o = (MummyMazeState) other;
        if (matrix.length != o.matrix.length) {
            return false;
        }

        return Arrays.deepEquals(matrix, o.matrix);
    }

    @Override
    public int hashCode() {
        return 97 * 7 + Arrays.deepHashCode(this.matrix);
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            buffer.append('\n');
            for (int j = 0; j < matrix.length; j++) {
                buffer.append(matrix[i][j]);
                buffer.append(' ');
            }
        }
        return buffer.toString();
    }

    @Override
    public MummyMazeState clone() {
        return new MummyMazeState(matrix);
    }
    //Listeners
    private transient ArrayList<MummyMazeListener> listeners = new ArrayList<MummyMazeListener>(3);

    public synchronized void removeListener(MummyMazeListener l) {
        if (listeners != null && listeners.contains(l)) {
            listeners.remove(l);
        }
    }

    public synchronized void addListener(MummyMazeListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    public void firePuzzleChanged(MummyMazeEvent pe) {
        for (MummyMazeListener listener : listeners) {
            listener.puzzleChanged(null);
        }
    }
}
