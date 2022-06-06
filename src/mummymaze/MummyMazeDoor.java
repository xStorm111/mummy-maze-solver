package mummymaze;

public class MummyMazeDoor {
    public int line;
    public int column;
    public boolean isOpened;
    public boolean isVertical;

    public MummyMazeDoor(int line, int column, boolean isOpened, boolean isVertical) {
        this.line = line;
        this.column = column;
        this.isOpened = isOpened;
        this.isVertical = isVertical;
    }
}
