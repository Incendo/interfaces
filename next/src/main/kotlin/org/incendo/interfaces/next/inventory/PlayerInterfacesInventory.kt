package org.incendo.interfaces.next.inventory

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.incendo.interfaces.next.utilities.gridPointToBukkitIndex

public class PlayerInterfacesInventory(
    private val player: Player
) : CachedInterfacesInventory() {

    private val playerInventory = player.inventory

    override fun get(row: Int, column: Int): ItemStack? {
        val index = gridPointToBukkitIndex(row, column)
        return playerInventory.getItem(index)
    }

    override fun setInternal(row: Int, column: Int, item: ItemStack?) {
        val index = gridPointToBukkitIndex(row, column)
        return playerInventory.setItem(index, item)
    }

    // Nothing in here counts as the player inventory since this is a fake
    // inventory. The purpose of this method is to find which slots should
    // not be drawn to until the InventoryOpenEvent, but since this is a fake
    // inventory it is never opened this this needs to be false.
    override fun isPlayerInventory(row: Int, column: Int): Boolean = false
}
