package org.incendo.interfaces.view

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.incendo.interfaces.InterfacesListeners
import org.incendo.interfaces.next.interfaces.PlayerInterface
import org.incendo.interfaces.next.inventory.PlayerInterfacesInventory
import org.incendo.interfaces.next.pane.PlayerPane
import org.incendo.interfaces.next.utilities.runSync

public class PlayerInterfaceView internal constructor(
    player: Player,
    backing: PlayerInterface
) : AbstractInterfaceView<PlayerInterfacesInventory, PlayerPane>(
    player,
    backing,
    // todo(josh): should player interface views hold a parent?
    null
) {

    override fun title(value: Component) {
        error("PlayerInventoryView's cannot have a title")
    }

    override fun overlapsPlayerInventory(): Boolean = true

    override fun requiresNewInventory(): Boolean = false

    override fun createInventory(): PlayerInterfacesInventory = PlayerInterfacesInventory(player)

    override fun openInventory() {
        // Close whatever inventory the player has open so they can look at their normal inventory!
        // This will only continue if the menu hasn't been closed yet.
        if (!isOpen(player)) {
            // First we close then we set the interface so we don't double open!
            org.incendo.interfaces.InterfacesListeners.INSTANCE.setOpenInterface(player.uniqueId, null)
            player.closeInventory()
            org.incendo.interfaces.InterfacesListeners.INSTANCE.setOpenInterface(player.uniqueId, this)
        }
    }

    override fun close() {
        // Ensure we update the interface state in the main thread!
        // Even if the menu is not currently on the screen.
        runSync {
            org.incendo.interfaces.InterfacesListeners.INSTANCE.setOpenInterface(player.uniqueId, null)
        }
    }

    override fun isOpen(player: Player): Boolean =
        player.openInventory.type == InventoryType.CRAFTING &&
            org.incendo.interfaces.InterfacesListeners.INSTANCE.getOpenInterface(player.uniqueId) == this
}
