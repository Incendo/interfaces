package org.incendo.interfaces.next.inventory

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.incendo.interfaces.next.utilities.gridPointToBukkitIndex

public class PlayerInterfacesInventory(
    private val player: Player,
) : CachedInterfacesInventory() {
    private val playerInventory = player.inventory

    override fun get(
        row: Int,
        column: Int,
    ): ItemStack? {
        val index = gridPointToBukkitIndex(row, column)
        return playerInventory.getItem(index)
    }

    override fun setInternal(
        row: Int,
        column: Int,
        item: ItemStack?,
    ) {
        val index = gridPointToBukkitIndex(row, column)
        return playerInventory.setItem(index, item)
    }

    override fun isPlayerInventory(
        row: Int,
        column: Int,
    ): Boolean = true
}
