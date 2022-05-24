package mummymaze;

import agent.Action;
import agent.State;

import java.util.ArrayList;
import java.util.Arrays;

import static gui.Properties.MATRIX_LINE_COLUMN_SIZE;

public class MummyMazeState extends State implements Cloneable {

    private final char[][] matrix;

    private int lineHero;
    private int columnHero;

    private int lineWhiteMummy;
    private int columnWhiteMummy;

    private int lineExit;
    private int columnExit;

    public MummyMazeState(char[][] matrix) {
        this.matrix = new char[matrix.length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                this.matrix[i][j] = matrix[i][j];

                switch (this.matrix[i][j]) {
                    case 'H' -> {
                        lineHero = i;
                        columnHero = j;
                    }
                    case 'M' -> {
                        lineWhiteMummy = i;
                        columnWhiteMummy = j;
                    }
                    case 'S' -> {
                        lineExit = i;
                        columnExit = j;
                    }
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
        if(HeroIsDead())
            return false;

        return (lineHero > 1 && matrix[lineHero - 1][columnHero] != '-' && matrix[lineHero - 2][columnHero] == '.')
                ||
                (lineHero == 1 && matrix[lineHero - 1][columnHero] == 'S');
    }

    public boolean canMoveDown() {
        if(HeroIsDead())
            return false;

        return (lineHero < MATRIX_LINE_COLUMN_SIZE - 2 && matrix[lineHero + 1][columnHero] != '-' && matrix[lineHero + 2][columnHero] == '.')
                ||
                (lineHero == MATRIX_LINE_COLUMN_SIZE - 2 && matrix[lineHero + 1][columnHero] == 'S');
    }

    public boolean canMoveRight() {
        if(HeroIsDead())
            return false;

        return (columnHero < MATRIX_LINE_COLUMN_SIZE - 2 && matrix[lineHero][columnHero + 1] != '|' && matrix[lineHero][columnHero + 2] == '.')
                ||
                (columnHero == MATRIX_LINE_COLUMN_SIZE - 2 && matrix[lineHero][columnHero + 1] == 'S');
    }

    public boolean canMoveLeft() {
        if(HeroIsDead())
            return false;

        return (columnHero > 1 && matrix[lineHero][columnHero - 1] != '|' && matrix[lineHero][columnHero - 2] == '.')
                ||
                (columnHero == 1 && matrix[lineHero][columnHero - 1] == 'S');
    }

    public boolean shouldStay() {
        return false;
//        if(HeroIsDead())
//            return false;
//
//        if(columnHero > columnWhiteMummy)
//        {
//            if(lineHero > lineWhiteMummy)
//            {
//                //White mummy wants to move right
//
//            }
//            else if(lineHero < lineWhiteMummy){
//                //White mummy wants to move right
//
//            }
//            else{
//                //White mummy wants to move down
//
//            }
//        }
//        return (columnHero > 1 && matrix[lineHero][columnHero - 1] == '|' && matrix[lineHero][columnHero - 2] == '.')
//                ||
//                (columnHero == 1 && matrix[lineHero][columnHero - 1] == 'S');
    }

    /*
     * In the next four methods we don't verify if the actions are valid.
     * This is done in method executeActions in class EightPuzzleProblem.
     * Doing the verification in these methods would imply that a clone of the
     * state was created whether the operation could be executed or not.
     */

    public void moveUp() {
        if (lineHero == 1)
            moveVertical(-1);
        else
            moveVertical(-2);

        moveWhiteMummy();
        moveWhiteMummy();
    }

    public void moveDown() {
        if (lineHero == MATRIX_LINE_COLUMN_SIZE - 2)
            moveVertical(1);
        else
            moveVertical(2);

        moveWhiteMummy();
        moveWhiteMummy();
    }



    public void moveLeft() {
        if (columnHero == 1)
            moveHorizontal(-1);
        else
            moveHorizontal(-2);

        moveWhiteMummy();
        moveWhiteMummy();
    }

    public void moveRight() {
        if (columnHero == MATRIX_LINE_COLUMN_SIZE - 2)
            moveHorizontal(1);
        else
            moveHorizontal(2);

        moveWhiteMummy();
        moveWhiteMummy();
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

    public int getLineHero() {
        return lineHero;
    }

    public int getColumnHero() {
        return columnHero;
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

    private void moveVertical(int positions) {
        matrix[lineHero][columnHero] = '.';

        lineHero = lineHero + positions;
        matrix[lineHero][columnHero] = 'H';
    }

    private void moveHorizontal(int positions) {
        matrix[lineHero][columnHero] = '.';

        columnHero = columnHero + positions;
        matrix[lineHero][columnHero] = 'H';
    }

    private void moveWhiteMummy(){
        if(HeroIsDead())
            return;

        if (moveMummyLeft())
            return;

        if (moveMummyRight())
            return;

        if (moveMummyUp())
            return;

        moveMummyDown();

    }

    private boolean moveMummyUp(){
        if(lineWhiteMummy > lineHero && matrix[lineWhiteMummy - 1][columnWhiteMummy] != '-'){
            killHero(matrix[lineWhiteMummy - 2][columnWhiteMummy] == 'H');

            matrix[lineWhiteMummy][columnWhiteMummy] = '.';

            lineWhiteMummy = lineWhiteMummy - 2;
            matrix[lineWhiteMummy][columnWhiteMummy] = 'M';
            return true;
        }
        return false;
    }
    private boolean moveMummyDown(){
        if(lineWhiteMummy < lineHero && matrix[lineWhiteMummy + 1][columnWhiteMummy] != '-'){
            killHero(matrix[lineWhiteMummy + 2][columnWhiteMummy] == 'H');

            matrix[lineWhiteMummy][columnWhiteMummy] = '.';

            lineWhiteMummy = lineWhiteMummy + 2;
            matrix[lineWhiteMummy][columnWhiteMummy] = 'M';
            return true;
        }
        return false;
    }
    private boolean moveMummyLeft(){
        if(columnWhiteMummy > columnHero && matrix[lineWhiteMummy][columnWhiteMummy - 1] != '|'){

            killHero(matrix[lineWhiteMummy][columnWhiteMummy - 2] == 'H');

            matrix[lineWhiteMummy][columnWhiteMummy] = '.';

            columnWhiteMummy = columnWhiteMummy - 2;
            matrix[lineWhiteMummy][columnWhiteMummy] = 'M';
            return true;
        }
        return false;
    }
    private boolean moveMummyRight(){
        if(columnWhiteMummy < columnHero && matrix[lineWhiteMummy][columnWhiteMummy + 1] != '|'){

            killHero(matrix[lineWhiteMummy][columnWhiteMummy + 2] == 'H');

            matrix[lineWhiteMummy][columnWhiteMummy] = '.';

            columnWhiteMummy = columnWhiteMummy + 2;
            matrix[lineWhiteMummy][columnWhiteMummy] = 'M';

            return true;
        }
        return false;
    }

    public int getLineExit() {
        return lineExit;
    }

    public int getColumnExit() {
        return columnExit;
    }

    private void killHero(boolean condition){
        if(condition)
        {
            lineHero = -1;
            columnHero = -1;
        }
    }

    //if a mais?
    private boolean HeroIsDead(){
        return lineHero == -1 || columnHero == -1;
    }
}
