package org.incendo.interfaces.grid;

public record GridPoint(int x, int y) {

    public static GridPoint at(final int x, final int y) {
        return new GridPoint(x, y);
    }

    public GridPoint minus(final GridPoint other) {
        return new GridPoint(this.x - other.x(), this.y - other.y());
    }
}
