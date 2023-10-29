package org.incendo.interfaces.inventory

import org.bukkit.inventory.ItemStack

public abstract class CachedInterfacesInventory : InterfacesInventory {

    final override fun set(row: Int, column: Int, item: ItemStack?): Boolean {
        val current = get(row, column)

        if (current == item) {
            return false
        }

        setInternal(row, column, item)
        return true
    }

    protected abstract fun setInternal(row: Int, column: Int, item: ItemStack?)
}
