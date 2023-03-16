package org.incendo.interfaces.next.view

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.incendo.interfaces.next.interfaces.PlayerInterface
import org.incendo.interfaces.next.inventory.PlayerInterfacesInventory
import org.incendo.interfaces.next.pane.PlayerPane
import org.incendo.interfaces.next.utilities.PlayerDataMap

public class PlayerInterfaceView internal constructor(
    player: Player,
    backing: PlayerInterface
) : AbstractInterfaceView<PlayerInterfacesInventory, PlayerPane>(
    player,
    backing,
    // todo(josh): should player interface views hold a parent?
    null
) {
    internal companion object {
        internal val OPEN_VIEWS = PlayerDataMap()
    }

    init {
        OPEN_VIEWS[player] = this
    }

    override fun title(value: Component) {
        error("PlayerInventoryView's cannot have a title")
    }

    override fun createInventory(): PlayerInterfacesInventory = PlayerInterfacesInventory(player)

    override fun openInventory() {
        // stub: do we want the open inventory method to be abstract?
    }

    override fun isOpen(player: Player): Boolean {
        return player.openInventory.type == InventoryType.CRAFTING
    }
}
