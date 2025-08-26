package org.incendo.interfaces.next.view

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.incendo.interfaces.next.InterfacesListeners
import org.incendo.interfaces.next.interfaces.PlayerInterface
import org.incendo.interfaces.next.inventory.PlayerInterfacesInventory
import org.incendo.interfaces.next.pane.PlayerPane
import org.incendo.interfaces.next.utilities.runSync

public class PlayerInterfaceView internal constructor(
    player: Player,
    backing: PlayerInterface,
) : AbstractInterfaceView<PlayerInterfacesInventory, PlayerPane>(
        player,
        backing,
        // todo(josh): should player interface views hold a parent?
        null,
    ) {
    override fun title(value: Component) {
        error("PlayerInventoryView's cannot have a title")
    }

    override fun createInventory(): PlayerInterfacesInventory = PlayerInterfacesInventory(player)

    override fun openInventory() {
        // Close whatever inventory the player has open so they can look at their normal inventory!
        // This will only continue if the menu hasn't been closed yet.
        if (!isOpen(player)) {
            // First we close then we set the interface so we don't double open!
            InterfacesListeners.instance.setOpenInterface(player.uniqueId, null)
            player.closeInventory()
            InterfacesListeners.instance.setOpenInterface(player.uniqueId, this)
        }

        // Double-check that this inventory is open now!
        if (isOpen(player)) {
            // Clear the player's inventory!
            player.inventory.clear()
            if (player.openInventory.topInventory.type == InventoryType.CRAFTING) {
                player.openInventory.topInventory.clear()
            }
            player.openInventory.cursor = null

            // Trigger onOpen manually because there is no real inventory being opened,
            // this will also re-draw the player inventory parts!
            onOpen()
        }
    }

    override fun close() {
        // Ensure we update the interface state in the main thread!
        // Even if the menu is not currently on the screen.
        runSync {
            InterfacesListeners.instance.setOpenInterface(player.uniqueId, null)
        }
    }

    override fun isOpen(player: Player): Boolean =
        player.openInventory.type == InventoryType.CRAFTING &&
            InterfacesListeners.instance.getOpenInterface(player.uniqueId) == this
}
