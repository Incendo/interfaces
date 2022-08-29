package org.incendo.interfaces.next.view

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.incendo.interfaces.next.interfaces.ChestInterface
import org.incendo.interfaces.next.pane.ChestPane
import org.incendo.interfaces.next.utilities.TitleState
import org.incendo.interfaces.next.utilities.runSync

public class ChestInterfaceView(
    player: Player,
    backing: ChestInterface
) : InterfaceView<ChestPane>(
    player,
    backing
) {
    private val titleState = TitleState(backing.initialTitle)

    override fun createInventory(): Inventory {
        val currentTitle = titleState.current
        val rows = backing.rows * COLUMNS_IN_CHEST

        return if (currentTitle != null) {
            Bukkit.createInventory(this, rows, currentTitle)
        } else {
            Bukkit.createInventory(this, rows)
        }
    }

    override fun openInventory() {
        titleState.hasChanged = false

        runSync {
            player.openInventory(this.inventory)
        }
    }

    override fun requiresPlayerUpdate(): Boolean = !firstPaint && titleState.hasChanged

    override fun requiresNewInventory(): Boolean = firstPaint || titleState.hasChanged
}
