package org.incendo.interfaces.next.view

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.incendo.interfaces.next.InterfacesListeners
import org.incendo.interfaces.next.interfaces.PlayerInterface
import org.incendo.interfaces.next.inventory.PlayerInterfacesInventory
import org.incendo.interfaces.next.pane.PlayerPane

public class PlayerInterfaceView internal constructor(
    player: Player,
    backing: PlayerInterface
) : AbstractInterfaceView<PlayerInterfacesInventory, PlayerPane>(
    player,
    backing,
    // todo(josh): should player interface views hold a parent?
    null
) {

    init {
        InterfacesListeners.INSTANCE.openPlayerInterfaceViews.put(player.uniqueId, this)
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
