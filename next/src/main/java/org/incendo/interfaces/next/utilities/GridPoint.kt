package org.incendo.interfaces.next.utilities

/**
 * A 2-dimensional vector storing integer components.
 */
public data class GridPoint(val x: Int, val y: Int) {

    public companion object {
        public fun at(x: Int, y: Int): GridPoint = GridPoint(x, y)
    }
}
