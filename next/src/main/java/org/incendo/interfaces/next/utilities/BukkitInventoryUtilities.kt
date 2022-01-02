package org.incendo.interfaces.next.utilities

public fun gridPointToBukkitIndex(column: Int, row: Int): Int {
    return row * 9 + column
}

public fun gridPointToBukkitIndex(gridPoint: GridPoint): Int = gridPointToBukkitIndex(gridPoint.x, gridPoint.y)

public fun forEachInGrid(columns: Int, rows: Int, function: (Int, Int) -> Unit) {
    for (column in 0 until columns) {
        for (row in 0 until rows) {
            function(column, row)
        }
    }
}
