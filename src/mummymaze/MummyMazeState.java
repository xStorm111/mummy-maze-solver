package mummymaze;

import agent.Action;
import agent.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import static gui.Properties.*;

public class MummyMazeState extends State implements Cloneable {

    private final char[][] matrix;

    private int lineHero;
    private int columnHero;

    public LinkedList<MummyMazeWhiteMummy> whiteMummies = new LinkedList<>();
    public LinkedList<MummyMazeRedMummy> redMummies = new LinkedList<>();
    public LinkedList<MummyMazeScorpion> scorpions = new LinkedList<>();
    public LinkedList<MummyMazeDoor> doors = new LinkedList<>();

    private final int lineTrap;
    private final int columnTrap;

    private final int lineKey;
    private final int columnKey;

    private final int lineExit;
    private final int columnExit;


    public MummyMazeState(char[][] matrix, int lineTrap,int  columnTrap, int lineKey,int columnKey, int lineExit, int columnExit) {
        this.matrix = new char[matrix.length][matrix.length];

        this.lineTrap = lineTrap;
        this.columnTrap = columnTrap;
        this.lineKey = lineKey;
        this.columnKey = columnKey;
        this.lineExit = lineExit;
        this.columnExit = columnExit;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                this.matrix[i][j] = matrix[i][j];

                switch (this.matrix[i][j]) {
                    case HERO_CHAR -> {
                        lineHero = i;
                        columnHero = j;
                    }

                    case WHITE_MUMMY_CHAR -> {
                        whiteMummies.add(new MummyMazeWhiteMummy(i,j,this.matrix[i][j]));
                    }
                    case RED_MUMMY_CHAR -> {
                        redMummies.add(new MummyMazeRedMummy(i,j,this.matrix[i][j]));
                    }
                    case SCORPION_CHAR -> {
                        scorpions.add(new MummyMazeScorpion(i,j,this.matrix[i][j]));
                    }

                    case HORIZONTAL_DOOR_CLOSED_CHAR -> {
                        doors.add(new MummyMazeDoor(i, j, false, false));
                    }
                    case HORIZONTAL_DOOR_OPEN_CHAR -> {
                        doors.add(new MummyMazeDoor(i, j, true, false));
                    }

                    case VERTICAL_DOOR_CLOSED_CHAR -> {
                        doors.add(new MummyMazeDoor(i, j, false, true));
                    }
                    case VERTICAL_DOOR_OPEN_CHAR -> {
                        doors.add(new MummyMazeDoor(i, j, true, true));
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
        return new MummyMazeState(matrix, lineTrap, columnTrap, lineKey, columnKey, lineExit, columnExit);
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
                HandleDoors();
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
                HandleDoors();
            }
        }

        //put previous char in place
        resetTile(lineHero, columnHero);

        columnHero = targetColumn;
        matrix[lineHero][columnHero] = HERO_CHAR;
    }

    private void moveEnemies(){
        for (MummyMazeWhiteMummy whiteMummy: whiteMummies) {
            moveWhiteMummy(whiteMummy);
            moveWhiteMummy(whiteMummy);
        }
        for (MummyMazeRedMummy redMummy: redMummies) {
            moveRedMummy(redMummy);
            moveRedMummy(redMummy);
        }
        for (MummyMazeScorpion scorpion : scorpions) {
            moveWhiteMummy(scorpion);
        }
    }

    private void moveWhiteMummy(MummyMazeEnemy enemy){
        if(HeroIsDead())
            return;

        if (enemyMovedLeft(enemy))
            return;

        if (enemyMovedRight(enemy))
            return;

        if (enemyMovedUp(enemy))
            return;

        enemyMovedDown(enemy);
    }

    private void moveRedMummy(MummyMazeEnemy enemy){
        if(HeroIsDead())
            return;

        if (enemyMovedUp(enemy))
            return;

        if (enemyMovedDown(enemy))
            return;

        if (enemyMovedLeft(enemy))
            return;

        enemyMovedRight(enemy);
    }

    private boolean enemyMovedUp(MummyMazeEnemy enemy){
        int previousLine = enemy.line;
        enemy.line = moveEnemiesVertical(enemy, false);

        return enemy.line == -1 || enemy.line < previousLine;
    }
    private boolean enemyMovedDown(MummyMazeEnemy enemy){
        int previousLine = enemy.line;
        enemy.line = moveEnemiesVertical(enemy, true);

        return enemy.line == -1 || enemy.line > previousLine;
    }

    private boolean enemyMovedLeft(MummyMazeEnemy enemy){
        int previousColumn = enemy.column;
        enemy.column = moveEnemiesHorizontal(enemy, false);

        return enemy.column == -1 || enemy.column < previousColumn;

    }
    private boolean enemyMovedRight(MummyMazeEnemy enemy){
        int previousColumn = enemy.column;
        enemy.column = moveEnemiesHorizontal(enemy, true);

        return enemy.column == -1 || enemy.column > previousColumn;
    }

    private int moveEnemiesVertical(MummyMazeEnemy enemy, boolean movePositive){
        int targetLine;
        int obstacleLine;

        boolean heroInThisDirection;

        //down
        if(movePositive){
            if(enemy.line == 11)
                return enemy.line;

            targetLine = enemy.line + 2;
            obstacleLine = enemy.line + 1;
            heroInThisDirection = enemy.line < lineHero;
        }
        //up
        else{
            if(enemy.line == 1)
                return enemy.line;

            targetLine = enemy.line - 2;
            obstacleLine = enemy.line - 1;
            heroInThisDirection = enemy.line > lineHero;
        }

        if (!heroInThisDirection
                || matrix[obstacleLine][enemy.column] == WALL_HORIZONTAL_CHAR
                || matrix[obstacleLine][enemy.column] == HORIZONTAL_DOOR_CLOSED_CHAR
        ) {
            return enemy.line;
        }

        if(enemy.character == SCORPION_CHAR)
        {
            if(matrix[targetLine][enemy.column] == WHITE_MUMMY_CHAR || matrix[targetLine][enemy.column] == RED_MUMMY_CHAR)
            {
                //found a mummy, so he died
                resetTile(enemy.line, enemy.column);
                killScorpion(enemy.line, enemy.column);
                return enemy.line;
            }
            else{
                //enemy will validate if the target position is 1 possible killing situation
                scorpionFoundSomethingOnTargetPosition(targetLine, enemy.column);
            }
        }
        else{
            mummyFoundSomethingOnTargetPosition(targetLine, enemy.column);
        }


        matrix[targetLine][enemy.column] = enemy.character;
        resetTile(enemy.line, enemy.column);

        return targetLine;
    }

    private int moveEnemiesHorizontal(MummyMazeEnemy enemy, boolean positive){

        int targetColumn;
        int obstacleColumn;
        boolean heroInThisDirection;

        //right
        if(positive){
            if(enemy.column == 11)
                return enemy.column;

            targetColumn = enemy.column + 2;
            obstacleColumn = enemy.column + 1;

            heroInThisDirection = enemy.column < columnHero;
        }
        //left
        else{
            if(enemy.column == 1)
                return enemy.column;

            targetColumn = enemy.column - 2;
            obstacleColumn = enemy.column - 1;

            heroInThisDirection = enemy.column > columnHero;
        }

        if (!heroInThisDirection
                || matrix[enemy.line][obstacleColumn] == WALL_VERTICAL_CHAR
                || matrix[enemy.line][obstacleColumn] == VERTICAL_DOOR_CLOSED_CHAR
        ) {
            return enemy.column;
        }

        if(enemy.character == SCORPION_CHAR)
        {
            if(matrix[enemy.line][targetColumn] == WHITE_MUMMY_CHAR || matrix[enemy.line][targetColumn] == RED_MUMMY_CHAR)
            {
                resetTile(enemy.line, enemy.column);
                killScorpion(enemy.line, enemy.column);
                return enemy.column;
            }
            else{
                //enemy will validate if the target position is 1 possible killing situation
                scorpionFoundSomethingOnTargetPosition(enemy.line, targetColumn);
            }
        }
        else{
            mummyFoundSomethingOnTargetPosition(enemy.line, targetColumn);
        }

        matrix[enemy.line][targetColumn] = enemy.character;
        resetTile(enemy.line, enemy.column);

        return targetColumn;
    }




    private void mummyFoundSomethingOnTargetPosition(int line, int column)
    {
        //let's look into the target position and apply the rules
        switch (matrix[line][column]){
            case HERO_CHAR -> {
                killHero();
            }
            case WHITE_MUMMY_CHAR -> {
                killWhiteMummy(line, column);
            }
            case RED_MUMMY_CHAR -> {
                killRedMummy(line, column);
            }
            case SCORPION_CHAR -> {
                killScorpion(line, column);
            }
            case KEY_CHAR -> { //regarding the key, only mummies can toggle the door
                HandleDoors();
            }
        }
    }

    private void scorpionFoundSomethingOnTargetPosition(int line, int column)
    {
        //let's look into the target position and apply the rules
        switch (matrix[line][column]){
            case HERO_CHAR -> {
                killHero();
            }
            case SCORPION_CHAR -> {
                killScorpion(line, column);
            }
        }
    }

    //if a mais?
    private boolean HeroIsDead(){
        return lineHero == -1 || columnHero == -1;
    }

    private void HandleDoors() {
        for (MummyMazeDoor door : doors) {
            if (door.isVertical)
                matrix[door.line][door.column] = door.isOpened ? VERTICAL_DOOR_CLOSED_CHAR : VERTICAL_DOOR_OPEN_CHAR;
            else
                matrix[door.line][door.column] = door.isOpened ? HORIZONTAL_DOOR_CLOSED_CHAR : HORIZONTAL_DOOR_OPEN_CHAR;

            door.isOpened = !door.isOpened;
        }
    }

    private void resetTile(int line, int column){
        if(line == lineKey && column == columnKey) {
            matrix[line][column] = KEY_CHAR;

            //reset temp
//            resetTempKey();
        }
        else if (line == lineTrap && column == columnTrap)
        {
            matrix[line][column] = TRAP_CHAR;
//
//            resetTempTrap();
        }
        else{
            matrix[line][column] = TILE_CHAR;
        }
    }

    private void killHero(){
        lineHero = -1;
        columnHero = -1;
    }

    private void killWhiteMummy(int line, int column){
        for (Iterator<MummyMazeWhiteMummy> iterator = whiteMummies.iterator(); iterator.hasNext();) {
            MummyMazeEnemy enemy = iterator.next();
            if (enemy.line == line && enemy.column == column) {
                enemy.line = -1;
                enemy.column = -1;
//                iterator.remove();
                return;
            }
        }
    }

    private void killRedMummy(int line, int column){
        for (Iterator<MummyMazeRedMummy> iterator = redMummies.iterator(); iterator.hasNext();) {
            MummyMazeEnemy enemy = iterator.next();
            if (enemy.line == line && enemy.column == column) {
                enemy.line = -1;
                enemy.column = -1;
//                iterator.remove();
                return;
            }
        }
    }

    private void killScorpion(int line, int column){

        for (Iterator<MummyMazeScorpion> iterator = scorpions.iterator(); iterator.hasNext();) {
            MummyMazeEnemy enemy = iterator.next();
            if (enemy.line == line && enemy.column == column) {
                enemy.line = -1;
                enemy.column = -1;
//                iterator.remove();
                return;
            }
        }
    }
}
