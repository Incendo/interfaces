package org.incendo.interfaces.next.utilities

public fun gridPointToBukkitIndex(column: Int, row: Int): Int {
    return column * 9 + row
}

public fun gridPointToBukkitIndex(gridPoint: GridPoint): Int = gridPointToBukkitIndex(gridPoint.x, gridPoint.y)
