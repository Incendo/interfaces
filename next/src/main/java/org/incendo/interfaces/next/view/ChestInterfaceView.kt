package org.incendo.interfaces.next.view

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.incendo.interfaces.next.interfaces.ChestInterface
import org.incendo.interfaces.next.pane.ChestPane
import org.incendo.interfaces.next.utilities.runSync

public class ChestInterfaceView(
    player: Player,
    backing: ChestInterface
) : InterfaceView<ChestPane>(
    player,
    backing
) {
    override fun createInventory(): Inventory {
        val currentTitle = titleState.current
        val rows = backing.rows * COLUMNS_IN_CHEST

        return if (currentTitle != null) {
            Bukkit.createInventory(this, rows, currentTitle)
        } else {
            Bukkit.createInventory(this, rows)
        }
    }

    override fun openInventory(): Unit = runSync {
        player.openInventory(this.inventory)
    }
}
