package org.incendo.interfaces.next.inventory

import org.bukkit.inventory.ItemStack

public interface InterfacesInventory {

    public fun isPlayerInventory(row: Int, column: Int): Boolean

    public fun set(row: Int, column: Int, item: ItemStack?): Boolean

    public fun get(row: Int, column: Int): ItemStack?
}
