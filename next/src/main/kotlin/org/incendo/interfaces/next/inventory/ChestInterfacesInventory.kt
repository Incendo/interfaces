package org.incendo.interfaces.next.inventory

import net.kyori.adventure.text.Component
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.incendo.interfaces.next.utilities.createBukkitInventory
import org.incendo.interfaces.next.utilities.gridPointToBukkitIndex

public class ChestInterfacesInventory(
    holder: InventoryHolder,
    title: Component?,
    rows: Int,
) : CachedInterfacesInventory() {

    public val chestInventory: Inventory = createBukkitInventory(holder, rows, title)

    override fun get(row: Int, column: Int): ItemStack? {
        val index = gridPointToBukkitIndex(row, column)
        return chestInventory.getItem(index)
    }

    override fun setInternal(row: Int, column: Int, item: ItemStack?) {
        val index = gridPointToBukkitIndex(row, column)
        chestInventory.setItem(index, item)
    }
}
