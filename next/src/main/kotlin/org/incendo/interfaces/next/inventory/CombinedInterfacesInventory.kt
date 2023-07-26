package org.incendo.interfaces.next.inventory

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.incendo.interfaces.next.utilities.createBukkitInventory
import org.incendo.interfaces.next.utilities.gridPointToBukkitIndex
import org.incendo.interfaces.next.view.AbstractInterfaceView.Companion.COLUMNS_IN_CHEST

public class CombinedInterfacesInventory(
    holder: InventoryHolder,
    player: Player,
    title: Component?,
    private val rows: Int,
) : CachedInterfacesInventory() {

    private val chestSlots = rows * COLUMNS_IN_CHEST

    private val playerInventory = player.inventory
    public val chestInventory: Inventory = createBukkitInventory(holder, rows, title)

    override fun get(row: Int, column: Int): ItemStack? {
        val bukkitIndex = gridPointToBukkitIndex(row, column)

        if (row >= rows) {
            val adjustedIndex = bukkitIndex - chestSlots
            return playerInventory.getItem(adjustedIndex)
        }

        return chestInventory.getItem(bukkitIndex)
    }

    override fun setInternal(row: Int, column: Int, item: ItemStack?) {
        val bukkitIndex = gridPointToBukkitIndex(row, column)

        if (row >= rows) {
            val adjustedIndex = bukkitIndex - chestSlots
            playerInventory.setItem(adjustedIndex, item)
            return
        }

        chestInventory.setItem(bukkitIndex, item)
    }
}
