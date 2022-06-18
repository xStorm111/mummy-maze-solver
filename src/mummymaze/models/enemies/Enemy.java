package mummymaze.models.enemies;

import mummymaze.models.MatrixPosition;

public class Enemy extends MatrixPosition {
    public char character;

    public Enemy(int line, int column, char enemyChar) {
        super(line, column);
        this.character = enemyChar;
    }
}
