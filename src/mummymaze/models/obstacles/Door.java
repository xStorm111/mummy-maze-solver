package mummymaze.models.obstacles;

import mummymaze.models.MatrixPosition;

public class Door extends MatrixPosition {
    public boolean isOpened;
    public boolean isVertical;

    public Door(int line, int column, boolean isOpened, boolean isVertical) {
        super(line, column);
        this.isOpened = isOpened;
        this.isVertical = isVertical;
    }
}
