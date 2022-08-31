package org.incendo.interfaces.next.view

import org.bukkit.entity.Player
import org.incendo.interfaces.next.interfaces.PlayerInterface
import org.incendo.interfaces.next.inventory.PlayerInterfacesInventory
import org.incendo.interfaces.next.pane.PlayerPane
import org.incendo.interfaces.next.utilities.PlayerDataMap

public class PlayerInterfaceView(
    player: Player,
    backing: PlayerInterface
) : InterfaceView<PlayerInterfacesInventory, PlayerPane>(
    player,
    backing
) {
    internal companion object {
        internal val OPEN_VIEWS = PlayerDataMap()
    }

    init {
        OPEN_VIEWS[player] = this
    }

    override fun createInventory(): PlayerInterfacesInventory = PlayerInterfacesInventory(player)

    override fun openInventory() {
        // stub: do we want the open inventory method to be abstract?
    }
}
