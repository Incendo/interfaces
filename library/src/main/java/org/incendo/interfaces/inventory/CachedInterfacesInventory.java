package org.incendo.interfaces.inventory;

import org.bukkit.inventory.ItemStack;

public abstract class CachedInterfacesInventory implements InterfacesInventory {

    @Override
    public final boolean set(final int row, final int column, final ItemStack item) {
        ItemStack current = this.get(row, column);

        if (current == item) {
            return false;
        }

        this.setInternal(row, column, item);
        return true;
    }

    protected abstract void setInternal(int row, int column, ItemStack item);

}
