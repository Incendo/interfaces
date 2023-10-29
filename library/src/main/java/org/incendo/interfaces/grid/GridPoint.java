package org.incendo.interfaces.grid;

public record GridPoint(int x, int y) {

    public static GridPoint at(int x, int y) {
        return new GridPoint(x, y);
    }

    public GridPoint minus(GridPoint other) {
        return new GridPoint(x - other.x(), y - other.y());
    }
}
