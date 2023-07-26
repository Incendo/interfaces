package org.incendo.interfaces.next.utilities

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.PlayerInventory
import org.incendo.interfaces.next.InterfacesListeners
import org.incendo.interfaces.next.grid.GridPoint
import org.incendo.interfaces.next.view.AbstractInterfaceView
import org.incendo.interfaces.next.view.AbstractInterfaceView.Companion.COLUMNS_IN_CHEST
import org.incendo.interfaces.next.view.InterfaceView

public fun gridPointToBukkitIndex(row: Int, column: Int): Int {
    return row * 9 + column
}

public fun gridPointToBukkitIndex(gridPoint: GridPoint): Int = gridPointToBukkitIndex(gridPoint.x, gridPoint.y)

public fun forEachInGrid(rows: Int, columns: Int, function: (row: Int, column: Int) -> Unit) {
    for (row in 0 until rows) {
        for (column in 0 until columns) {
            function(row, column)
        }
    }
}

public fun createBukkitInventory(
    holder: InventoryHolder,
    rows: Int,
    title: Component?,
): Inventory {
    if (title == null) {
        return Bukkit.createInventory(holder, rows * COLUMNS_IN_CHEST)
    }

    return Bukkit.createInventory(holder, rows * COLUMNS_IN_CHEST, title)
}
