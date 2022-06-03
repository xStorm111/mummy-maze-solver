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

//    private int lineWhiteMummy2;
//    private int columnWhiteMummy2;

    private int lineRedMummy;
    private int columnRedMummy;

//    private int lineRedMummy2;
//    private int columnRedMummy2;

    private int lineScorpion;
    private int columnScorpion;

    private int lineTrap;
    private int columnTrap;

    //used when something is in trap position
    private int tempLineTrap;
    private int tempColumnTrap;

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

    public void stay(){
            moveEnemies();
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

        moveEnemies();
    }

    public void moveDown() {
        if (lineHero == MATRIX_LINE_COLUMN_SIZE - 2)
            moveVertical(1);
        else
            moveVertical(2);

        moveEnemies();
    }

    public void moveLeft() {
        if (columnHero == 1)
            moveHorizontal(-1);
        else
            moveHorizontal(-2);

        moveEnemies();
    }

    public void moveRight() {
        if (columnHero == MATRIX_LINE_COLUMN_SIZE - 2)
            moveHorizontal(1);
        else
            moveHorizontal(2);

        moveEnemies();
    }

    private void moveEnemies(){
        if(columnWhiteMummy > 0 && lineWhiteMummy > 0)
        {
            moveWhiteMummy();
            moveWhiteMummy();
        }

        if(columnRedMummy > 0 && lineRedMummy > 0)
        {
            moveRedMummy();
            moveRedMummy();
        }

        if(columnScorpion > 0 && lineScorpion > 0)
        {
            moveScorpion();
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
        int targetLine = lineHero + positions;
        //if next hero move is to something != tile
        switch (matrix[targetLine][columnHero]){
            case TRAP_CHAR -> {
                killHero();
                return;
            }
            case KEY_CHAR -> {
                HandleDoor();
            }
        }

        //put previous char in place
        resetTile(lineHero, columnHero);

        lineHero = targetLine;
        matrix[lineHero][columnHero] = HERO_CHAR;
    }

    private void moveHorizontal(int positions) {
        int targetColumn = columnHero + positions;
        //if next hero move is to something != tile
        switch (matrix[lineHero][targetColumn]){
            case TRAP_CHAR -> {
                killHero();
                return;
            }
            case KEY_CHAR -> {
                HandleDoor();
            }
        }

        //put previous char in place
        resetTile(lineHero, columnHero);

        columnHero = targetColumn;
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

    private void moveRedMummy(){
        if(HeroIsDead())
            return;

        if (moveRedMummyUp())
            return;

        if (moveRedMummyDown())
            return;

        if (moveRedMummyLeft())
            return;

        moveRedMummyRight();
    }

    private void moveScorpion(){
        if(HeroIsDead())
            return;

        if (moveScorpionLeft())
            return;

        if (moveScorpionRight())
            return;

        if (moveScorpionUp())
            return;

        moveScorpionDown();

    }

    private boolean moveWhiteMummyUp(){
        int previousLine = lineWhiteMummy;
        lineWhiteMummy = moveEnemiesUp(WHITE_MUMMY_CHAR, lineWhiteMummy, columnWhiteMummy);

        return lineWhiteMummy < previousLine;
    }
    private boolean moveWhiteMummyDown(){
        int previousLine = lineWhiteMummy;
        lineWhiteMummy = moveEnemiesDown(WHITE_MUMMY_CHAR, lineWhiteMummy, columnWhiteMummy);

        return lineWhiteMummy > previousLine;

    }
    private boolean moveWhiteMummyLeft(){
        int previousColumn = columnWhiteMummy;
        columnWhiteMummy = moveEnemiesLeft(WHITE_MUMMY_CHAR, lineWhiteMummy, columnWhiteMummy);

        return columnWhiteMummy < previousColumn;

    }
    private boolean moveWhiteMummyRight(){
        int previousColumn = columnWhiteMummy;
        columnWhiteMummy = moveEnemiesRight(WHITE_MUMMY_CHAR, lineWhiteMummy, columnWhiteMummy);

        return columnWhiteMummy > previousColumn;
    }

    private boolean moveRedMummyUp(){
        int previousLine = lineRedMummy;
        lineRedMummy = moveEnemiesUp(RED_MUMMY_CHAR, lineRedMummy, columnRedMummy);

        return lineRedMummy < previousLine;
    }
    private boolean moveRedMummyDown(){
        int previousLine = lineRedMummy;
        lineRedMummy = moveEnemiesDown(RED_MUMMY_CHAR, lineRedMummy, columnRedMummy);

        return lineRedMummy > previousLine;

    }
    private boolean moveRedMummyLeft(){
        int previousColumn = columnRedMummy;
        columnRedMummy = moveEnemiesLeft(RED_MUMMY_CHAR, lineRedMummy, columnRedMummy);

        return columnRedMummy < previousColumn;

    }
    private boolean moveRedMummyRight(){
        int previousColumn = columnRedMummy;
        columnRedMummy = moveEnemiesRight(RED_MUMMY_CHAR, lineRedMummy, columnRedMummy);

        return columnRedMummy > previousColumn;
    }

    private boolean moveScorpionUp(){
        int previousLine = lineScorpion;
        lineScorpion = moveEnemiesUp(SCORPION_CHAR, lineScorpion, columnScorpion);

        return lineScorpion < previousLine;
    }
    private boolean moveScorpionDown(){
        int previousLine = lineScorpion;
        lineScorpion = moveEnemiesDown(SCORPION_CHAR, lineScorpion, columnScorpion);

        return lineScorpion > previousLine;

    }
    private boolean moveScorpionLeft(){
        int previousColumn = columnScorpion;
        columnScorpion = moveEnemiesLeft(SCORPION_CHAR, lineScorpion, columnScorpion);

        return columnScorpion < previousColumn;

    }
    private boolean moveScorpionRight(){
        int previousColumn = columnScorpion;
        columnScorpion = moveEnemiesRight(SCORPION_CHAR, lineScorpion, columnScorpion);

        return columnScorpion > previousColumn;
    }

    private int moveEnemiesUp(char enemy, int line, int column){
        //can't go more up
        if(line == 1)
            return  line;

        int targetLine = line - 2;
        int obstacleLine = line - 1;

        if(line > lineHero
                && matrix[obstacleLine][column] != WALL_HORIZONTAL_CHAR
                && matrix[obstacleLine][column] != HORIZONTAL_DOOR_CLOSED_CHAR
        ){
            if(enemy == SCORPION_CHAR)
            {
                if(matrix[targetLine][column] == WHITE_MUMMY_CHAR || matrix[targetLine][column] == RED_MUMMY_CHAR)
                    return column;
                //enemy will validate if the target position is 1 possible killing situation
                scorpionFoundSomethingOnTargetPosition(matrix[targetLine][column]);
            }

            else{
                mummyFoundSomethingOnTargetPosition(matrix[targetLine][column]);
            }

            resetTile(line, column);

            matrix[targetLine][column] = enemy;

            return targetLine;
        }
        return line;
    }

    private int moveEnemiesDown(char enemy, int line, int column){
        //can't go more down
        if(line == 11)
            return  line;

        int targetLine = line + 2;
        int obstacleLine = line + 1;

        if(line < lineHero
                && matrix[obstacleLine][column] != WALL_HORIZONTAL_CHAR
                && matrix[obstacleLine][column] != HORIZONTAL_DOOR_CLOSED_CHAR
        ){
            if(enemy == SCORPION_CHAR)
            {
                if(matrix[targetLine][column] == WHITE_MUMMY_CHAR || matrix[targetLine][column] == RED_MUMMY_CHAR)
                    return column;
                //enemy will validate if the target position is 1 possible killing situation
                scorpionFoundSomethingOnTargetPosition(matrix[targetLine][column]);
            }

            else{
                mummyFoundSomethingOnTargetPosition(matrix[targetLine][column]);
            }

            resetTile(line, column);

            matrix[targetLine][column] = enemy;

            return targetLine;
        }
        return line;
    }

    private int moveEnemiesLeft(char enemy, int line, int column){

        //can't go more left
        if(column == 1)
            return  column;

        int targetColumn = column - 2;
        int obstacleColumn = column - 1;

        if(column > columnHero
                && matrix[line][obstacleColumn] != WALL_VERTICAL_CHAR
                && matrix[line][obstacleColumn] != VERTICAL_DOOR_CLOSED_CHAR
        ){

            if(enemy == SCORPION_CHAR)
            {
                if(matrix[line][targetColumn] == WHITE_MUMMY_CHAR || matrix[line][targetColumn] == RED_MUMMY_CHAR)
                    return column;
                //enemy will validate if the target position is 1 possible killing situation
                scorpionFoundSomethingOnTargetPosition(matrix[line][targetColumn]);
            }

            else{
                mummyFoundSomethingOnTargetPosition(matrix[line][targetColumn]);
            }

            resetTile(line, column);

            matrix[line][targetColumn] = enemy;

            return targetColumn;
        }
        return column;
    }

    private int moveEnemiesRight(char enemy, int line, int column){

        //can't go more right
        if(column == 11)
            return  column;

        int targetColumn = column + 2;
        int obstacleColumn = column + 1;

        if(column < columnHero
                && matrix[line][obstacleColumn] != WALL_VERTICAL_CHAR
                && matrix[line][obstacleColumn] != VERTICAL_DOOR_CLOSED_CHAR
        ){
            if(enemy == SCORPION_CHAR)
            {
                if(matrix[line][targetColumn] == WHITE_MUMMY_CHAR || matrix[line][targetColumn] == RED_MUMMY_CHAR)
                    return column;
                //enemy will validate if the target position is 1 possible killing situation
                scorpionFoundSomethingOnTargetPosition(matrix[line][targetColumn]);
            }

            else{
                mummyFoundSomethingOnTargetPosition(matrix[line][targetColumn]);
            }

            resetTile(line, column);

            matrix[line][targetColumn] = enemy;

            return targetColumn;
        }
        return column;
    }


    private void mummyFoundSomethingOnTargetPosition(char position)
    {
        //let's look into the target position and apply the rules
        switch (position){
            case HERO_CHAR -> {
                killHero();
            }
            case SCORPION_CHAR -> {
                killScorpion();
            }
            case TRAP_CHAR -> {
                setTempTrap();
            }
            case WHITE_MUMMY_CHAR -> { //target position is white and enemy is red mummy then kill
                killWhiteMummy();
            }
            case RED_MUMMY_CHAR -> { //target position is red and enemy is white mummy then kill
                killRedMummy();
            }
            case KEY_CHAR -> { //regarding the key, only mummies can toggle the door
                HandleDoor();
            }
        }
    }

    private void scorpionFoundSomethingOnTargetPosition(char position)
    {
        //let's look into the target position and apply the rules
        switch (position){
            case HERO_CHAR -> {
                killHero();
            }
            case SCORPION_CHAR -> {
                killScorpion();
            }
            case TRAP_CHAR -> {
                setTempTrap();
            }
            case KEY_CHAR -> { //regarding the key, only mummies can toggle the door
                setTempKey();
            }
        }
    }

    private void killHero(){
            lineHero = -1;
            columnHero = -1;
    }

    private void killScorpion(){
            lineScorpion = -1;
            columnScorpion = -1;
    }

    private void killWhiteMummy(){
            lineWhiteMummy = -1;
            columnWhiteMummy = -1;
    }

    private void killRedMummy(){
            lineRedMummy = -1;
            columnRedMummy = -1;
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

            doorIsOpen = !doorIsOpen;
        }

        setTempKey();
    }

    private void resetTile(int line, int column){
        if(line == tempLineKey && column == tempColumnKey) {
            matrix[line][column] = KEY_CHAR;

            //reset temp
            resetTempKey();
        }
        else if (line == tempLineTrap && column == tempColumnTrap)
        {
            matrix[line][column] = TRAP_CHAR;

            resetTempTrap();
        }
        else{
            matrix[line][column] = TILE_CHAR;
        }
    }

    private void setTempKey(){
        tempLineKey = lineKey;
        tempColumnKey = columnKey;
    }

    private void resetTempKey(){
        tempLineKey = -1;
        tempColumnKey = -1;
    }

    private void setTempTrap(){
        tempLineTrap = lineTrap;
        tempColumnTrap = columnTrap;
    }

    private void resetTempTrap(){
        tempLineTrap = -1;
        tempColumnTrap = -1;
    }
}
