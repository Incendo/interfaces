package org.incendo.interfaces.next.grid

/**
 * A 2-dimensional vector storing integer components.
 */
public data class GridPoint(val x: Int, val y: Int) {

    public companion object {
        public fun at(x: Int, y: Int): GridPoint = GridPoint(x, y)
    }

    public operator fun minus(other: GridPoint): GridPoint {
        return GridPoint(x - other.x, y - other.y)
    }

    public operator fun rangeTo(other: GridPoint): Sequence<GridPoint> = sequence {
        for (y in y..other.y) {
            for (x in x..other.x) {
                yield(at(x, y))
            }
        }
    }
}
