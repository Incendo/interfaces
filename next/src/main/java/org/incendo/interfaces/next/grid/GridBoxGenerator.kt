package org.incendo.interfaces.next.grid

/**
 * A grid position generator that generates every point inside a box with corners of
 * [min] and [max] (inclusive).
 */
public data class GridBoxGenerator(
    private val min: GridPoint,
    private val max: GridPoint
) : GridPositionGenerator {

    override fun generate(): List<GridPoint> =
        (min..max).toList()
}
