package mummymaze;

import agent.Action;
import agent.State;

import java.util.ArrayList;
import java.util.Arrays;

import static gui.Properties.*;

public class MummyMazeState extends State implements Cloneable {

    private final char[][] matrix;

    private int lineHero;
    private int columnHero;

    private int lineWhiteMummy;
    private int columnWhiteMummy;

    private int lineRedMummy;
    private int columnRedMummy;

    private int lineScorpion;
    private int columnScorpion;

    private int lineTrap;
    private int columnTrap;

    private int lineKey;
    private int columnKey;

    //used when something is in key position
    private int tempLineKey;
    private int tempColumnKey;

    private int lineDoor;
    private int columnDoor;
    private boolean doorIsVertical;
    private boolean doorIsOpen;

    private int lineExit;
    private int columnExit;


    public MummyMazeState(char[][] matrix) {
        this.matrix = new char[matrix.length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                this.matrix[i][j] = matrix[i][j];

                switch (this.matrix[i][j]) {
                    case HERO_CHAR -> {
                        lineHero = i;
                        columnHero = j;
                    }
                    case EXIT_CHAR -> {
                        lineExit = i;
                        columnExit = j;
                    }
                    case WHITE_MUMMY_CHAR -> {
                        lineWhiteMummy = i;
                        columnWhiteMummy = j;
                    }
                    case RED_MUMMY_CHAR -> {
                        lineRedMummy = i;
                        columnRedMummy = j;
                    }
                    case SCORPION_CHAR -> {
                        lineScorpion = i;
                        columnScorpion = j;
                    }
                    case TRAP_CHAR -> {
                        lineTrap = i;
                        columnTrap = j;
                    }
                    case KEY_CHAR -> {
                        lineKey = i;
                        columnKey = j;
                    }
                    case HORIZONTAL_DOOR_CLOSED_CHAR -> {
                        lineDoor = i;
                        columnDoor = j;
                        doorIsVertical = false;
                        doorIsOpen = false;
                    }
                    case HORIZONTAL_DOOR_OPEN_CHAR -> {
                        lineDoor = i;
                        columnDoor = j;
                        doorIsVertical = false;
                        doorIsOpen = true;
                    }

                    case VERTICAL_DOOR_CLOSED_CHAR -> {
                        lineDoor = i;
                        columnDoor = j;
                        doorIsVertical = true;
                        doorIsOpen = false;
                    }
                    case VERTICAL_DOOR_OPEN_CHAR -> {
                        lineDoor = i;
                        columnDoor = j;
                        doorIsVertical = true;
                        doorIsOpen = true;
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

        return (lineHero > 1
                && matrix[lineHero - 1][columnHero] != WALL_HORIZONTAL_CHAR
                && matrix[lineHero - 1][columnHero] != HORIZONTAL_DOOR_CLOSED_CHAR
                && (matrix[lineHero - 2][columnHero] == TILE_CHAR || matrix[lineHero - 2][columnHero] == KEY_CHAR)
        )
                ||
                (lineHero == 1 && matrix[lineHero - 1][columnHero] == EXIT_CHAR);
    }

    public boolean canMoveDown() {
        if(HeroIsDead())
            return false;

        return (lineHero < MATRIX_LINE_COLUMN_SIZE - 2
                && matrix[lineHero + 1][columnHero] != WALL_HORIZONTAL_CHAR
                && matrix[lineHero + 1][columnHero] != HORIZONTAL_DOOR_CLOSED_CHAR
                && (matrix[lineHero + 2][columnHero] == TILE_CHAR || matrix[lineHero + 2][columnHero] == KEY_CHAR)
        )
                ||
                (lineHero == MATRIX_LINE_COLUMN_SIZE - 2 && matrix[lineHero + 1][columnHero] == EXIT_CHAR);
    }

    public boolean canMoveRight() {
        if(HeroIsDead())
            return false;

        return (columnHero < MATRIX_LINE_COLUMN_SIZE - 2
                && matrix[lineHero][columnHero + 1] != WALL_VERTICAL_CHAR
                && matrix[lineHero][columnHero + 1] != VERTICAL_DOOR_CLOSED_CHAR
                && (matrix[lineHero][columnHero + 2] == TILE_CHAR || matrix[lineHero][columnHero + 2] == KEY_CHAR)
        )
                ||
                (columnHero == MATRIX_LINE_COLUMN_SIZE - 2 && matrix[lineHero][columnHero + 1] == EXIT_CHAR);
    }

    public boolean canMoveLeft() {
        if(HeroIsDead())
            return false;

        return (columnHero > 1
                && matrix[lineHero][columnHero - 1] != WALL_VERTICAL_CHAR
                && matrix[lineHero][columnHero - 1] != VERTICAL_DOOR_CLOSED_CHAR
                && (matrix[lineHero][columnHero - 2] == TILE_CHAR || matrix[lineHero][columnHero - 2] == KEY_CHAR)
        )
                ||
                (columnHero == 1 && matrix[lineHero][columnHero - 1] == EXIT_CHAR);
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
//        return (columnHero > 1 && matrix[lineHero][columnHero - 1] == WALL_VERTICAL_CHAR && matrix[lineHero][columnHero - 2] == TILE_CHAR)
//                ||
//                (columnHero == 1 && matrix[lineHero][columnHero - 1] == EXIT_CHAR);
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
        if(columnWhiteMummy > 0 && lineWhiteMummy > 0)
        {
            moveWhiteMummy();
            moveWhiteMummy();
        }
    }

    public void moveDown() {
        if (lineHero == MATRIX_LINE_COLUMN_SIZE - 2)
            moveVertical(1);
        else
            moveVertical(2);

        if(columnWhiteMummy > 0 && lineWhiteMummy > 0)
        {
            moveWhiteMummy();
            moveWhiteMummy();
        }
    }



    public void moveLeft() {
        if (columnHero == 1)
            moveHorizontal(-1);
        else
            moveHorizontal(-2);

        if(columnWhiteMummy > 0 && lineWhiteMummy > 0)
        {
            moveWhiteMummy();
            moveWhiteMummy();
        }
    }

    public void moveRight() {
        if (columnHero == MATRIX_LINE_COLUMN_SIZE - 2)
            moveHorizontal(1);
        else
            moveHorizontal(2);

        if(columnWhiteMummy > 0 && lineWhiteMummy > 0)
        {
            moveWhiteMummy();
            moveWhiteMummy();
        }
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

    public int getLineExit() {
        return lineExit;
    }

    public int getColumnExit() {
        return columnExit;
    }

    private void moveVertical(int positions) {

        //if next hero move is to something != tile
        switch (matrix[lineHero+positions][columnHero]){
            case TRAP_CHAR -> {
                killHero(true, TILE_CHAR);
                return;
            }
            case KEY_CHAR -> {
                HandleDoor();
                tempLineKey = lineKey;
                tempColumnKey = columnKey;
            }
        }

        if(lineHero == tempLineKey && columnHero == tempColumnKey)
            matrix[lineHero][columnHero] = KEY_CHAR;
        else
            matrix[lineHero][columnHero] = TILE_CHAR;



        lineHero = lineHero + positions;
        matrix[lineHero][columnHero] = HERO_CHAR;
    }

    private void moveHorizontal(int positions) {

        //if next hero move is to something != tile
        switch (matrix[lineHero][columnHero+positions]){
            case TRAP_CHAR -> {
                killHero(true, TILE_CHAR);
                return;
            }
            case KEY_CHAR -> {
                HandleDoor();
                tempLineKey = lineKey;
                tempColumnKey = columnKey;
            }
        }

        //put the key again on the board
        if(lineHero == tempLineKey && columnHero == tempColumnKey) {
            matrix[lineHero][columnHero] = KEY_CHAR;

            //reset temp
            tempLineKey = -1;
            tempColumnKey = -1;
        }
        else
            matrix[lineHero][columnHero] = TILE_CHAR;

        columnHero = columnHero + positions;
        matrix[lineHero][columnHero] = HERO_CHAR;
    }

    private void moveWhiteMummy(){
        if(HeroIsDead())
            return;

        if (moveWhiteMummyLeft())
            return;

        if (moveWhiteMummyRight())
            return;

        if (moveWhiteMummyUp())
            return;

        moveWhiteMummyDown();

    }

    private boolean moveWhiteMummyUp(){
//        if(lineWhiteMummy > lineHero && matrix[lineWhiteMummy - 1][columnWhiteMummy] != WALL_HORIZONTAL_CHAR){
//            killHero(matrix[lineWhiteMummy - 2][columnWhiteMummy] == HERO_CHAR,WHITE_MUMMY_CHAR);
//
//            matrix[lineWhiteMummy][columnWhiteMummy] = TILE_CHAR;
//
//            lineWhiteMummy = lineWhiteMummy - 2;
//            matrix[lineWhiteMummy][columnWhiteMummy] = WHITE_MUMMY_CHAR;
//            return true;
//        }
//        return false;

        int previousLine = lineWhiteMummy;
        lineWhiteMummy = moveEnemiesUp(WHITE_MUMMY_CHAR, lineWhiteMummy, columnWhiteMummy);
        return lineWhiteMummy < previousLine;
    }
    private boolean moveWhiteMummyDown(){
//        if(lineWhiteMummy < lineHero && matrix[lineWhiteMummy + 1][columnWhiteMummy] != WALL_HORIZONTAL_CHAR){
//            killHero(matrix[lineWhiteMummy + 2][columnWhiteMummy] == HERO_CHAR,WHITE_MUMMY_CHAR);
//
//            matrix[lineWhiteMummy][columnWhiteMummy] = TILE_CHAR;
//
//            lineWhiteMummy = lineWhiteMummy + 2;
//            matrix[lineWhiteMummy][columnWhiteMummy] = WHITE_MUMMY_CHAR;
//            return true;
//        }
//        return false;

        int previousLine = lineWhiteMummy;
        lineWhiteMummy = moveEnemiesDown(WHITE_MUMMY_CHAR, lineWhiteMummy, columnWhiteMummy);
        return lineWhiteMummy > previousLine;
    }
    private boolean moveWhiteMummyLeft(){
//        if(columnWhiteMummy > columnHero && matrix[lineWhiteMummy][columnWhiteMummy - 1] != WALL_VERTICAL_CHAR){
//
//            killHero(matrix[lineWhiteMummy][columnWhiteMummy - 2] == HERO_CHAR,WHITE_MUMMY_CHAR);
//
//            matrix[lineWhiteMummy][columnWhiteMummy] = TILE_CHAR;
//
//            columnWhiteMummy = columnWhiteMummy - 2;
//            matrix[lineWhiteMummy][columnWhiteMummy] = WHITE_MUMMY_CHAR;
//            return true;
//        }
//        return false;
        int previousColumn = columnWhiteMummy;
        columnWhiteMummy = moveEnemiesLeft(WHITE_MUMMY_CHAR, lineWhiteMummy, columnWhiteMummy);
        return columnWhiteMummy < previousColumn;
    }
    private boolean moveWhiteMummyRight(){
//        if(columnWhiteMummy < columnHero && matrix[lineWhiteMummy][columnWhiteMummy + 1] != WALL_VERTICAL_CHAR){
//
//            killHero(matrix[lineWhiteMummy][columnWhiteMummy + 2] == HERO_CHAR,WHITE_MUMMY_CHAR);
//
//            matrix[lineWhiteMummy][columnWhiteMummy] = TILE_CHAR;
//
//            columnWhiteMummy = columnWhiteMummy + 2;
//            matrix[lineWhiteMummy][columnWhiteMummy] = WHITE_MUMMY_CHAR;
//
//            return true;
//        }
//        return false;

        int previousColumn = columnWhiteMummy;
        columnWhiteMummy = moveEnemiesRight(WHITE_MUMMY_CHAR, lineWhiteMummy, columnWhiteMummy);
        return columnWhiteMummy > previousColumn;
    }

    private int moveEnemiesUp(char enemy, int line, int column){
        int targetLine = line - 2;
        int obstacleLine = line - 1;

        if(line > lineHero
                && matrix[obstacleLine][column] != WALL_HORIZONTAL_CHAR
                && matrix[obstacleLine][column] != HORIZONTAL_DOOR_CLOSED_CHAR
        ){
            killHero(matrix[targetLine][column] == HERO_CHAR,enemy);

            matrix[line][column] = TILE_CHAR;
            matrix[targetLine][column] = enemy;
            return targetLine;
        }
        return line;
    }

    private int moveEnemiesDown(char enemy, int line, int column){
        int targetLine = line + 2;
        int obstacleLine = line + 1;

        if(line < lineHero
                && matrix[obstacleLine][column] != WALL_HORIZONTAL_CHAR
                && matrix[obstacleLine][column] != HORIZONTAL_DOOR_CLOSED_CHAR
        ){
            killHero(matrix[targetLine][column] == HERO_CHAR,enemy);

            matrix[line][column] = TILE_CHAR;
            matrix[targetLine][column] = enemy;
            return targetLine;
        }
        return line;
    }

    private int moveEnemiesLeft(char enemy, int line, int column){
        int targetColumn = column - 2;
        int obstacleColumn = column - 1;

        if(column > columnHero
                && matrix[line][obstacleColumn] != WALL_VERTICAL_CHAR
                && matrix[line][obstacleColumn] != VERTICAL_DOOR_CLOSED_CHAR
        ){

            killHero(matrix[line][targetColumn] == HERO_CHAR,enemy);

            matrix[line][column] = TILE_CHAR;
            matrix[line][targetColumn] = enemy;

            return targetColumn;
        }
        return column;
    }

    private int moveEnemiesRight(char enemy, int line, int column){
        int targetColumn = column + 2;
        int obstacleColumn = column + 1;

        if(column < columnHero
                && matrix[line][obstacleColumn] != WALL_VERTICAL_CHAR
                && matrix[line][obstacleColumn] != VERTICAL_DOOR_CLOSED_CHAR
        ){

            killHero(matrix[line][targetColumn] == HERO_CHAR,enemy);

            matrix[line][column] = TILE_CHAR;
            matrix[line][targetColumn] = enemy;

            return targetColumn;
        }
        return column;
    }

    private void killHero(boolean condition, char replace){
        if(condition)
        {
            matrix[lineHero][columnHero] = replace;
            lineHero = -1;
            columnHero = -1;
        }
    }

    //if a mais?
    private boolean HeroIsDead(){
        return lineHero == -1 || columnHero == -1;
    }

    private void HandleDoor() {
        if (lineDoor > 0 && columnDoor > 0) {
            if(doorIsVertical)
                matrix[lineDoor][columnDoor] = doorIsOpen ? VERTICAL_DOOR_CLOSED_CHAR : VERTICAL_DOOR_OPEN_CHAR;
            else
                matrix[lineDoor][columnDoor] = doorIsOpen ? HORIZONTAL_DOOR_CLOSED_CHAR : HORIZONTAL_DOOR_OPEN_CHAR;
        }
    }


    private void moveRedMummy(){
        if(HeroIsDead())
            return;

        if (moveWhiteMummyUp())
            return;

        if (moveWhiteMummyDown())
            return;

        if (moveWhiteMummyLeft())
            return;

        moveWhiteMummyRight();
    }
}
