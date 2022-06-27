package org.incendo.interfaces.next.grid

public class GridBoxGenerator(
    private val min: GridPoint,
    private val max: GridPoint
) : GridPositionGenerator {

    override fun generate(): List<GridPoint> {
        val results = mutableListOf<GridPoint>()

        for (y in min.y..min.y + max.y) {
            for (x in min.x..min.x + max.x) {
                results += GridPoint.at(x, y)
            }
        }

        return results
    }
}
