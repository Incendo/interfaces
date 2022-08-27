package org.incendo.interfaces.next.view

import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.incendo.interfaces.next.interfaces.PlayerInterface
import org.incendo.interfaces.next.pane.PlayerPane

public class PlayerInterfaceView(
    player: Player,
    backing: PlayerInterface
) : InterfaceView<PlayerPane>(
    player,
    backing
) {
    override fun createInventory(): Inventory {
        return player.inventory
    }

    override fun openInventory() {
        // stub: do we want the open inventory method to be abstract?
    }
}
