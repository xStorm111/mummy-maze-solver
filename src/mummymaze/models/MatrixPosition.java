package mummymaze.models;

import mummymaze.models.items.Item;

import java.util.Objects;

public class MatrixPosition {
    public int line;
    public int column;

    public MatrixPosition(int line, int column) {
        this.line = line;
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return line == item.line && column == item.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, column);
    }
}
