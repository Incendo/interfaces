package org.incendo.interfaces.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.incendo.interfaces.utilities.BukkitInventoryUtilities;

public final class ChestInterfacesInventory extends CachedInterfacesInventory {

    private final Inventory chestInventory;

    public ChestInterfacesInventory(final InventoryHolder holder, final Component title, final int rows) {
        this.chestInventory = BukkitInventoryUtilities.createBukkitInventory(holder, rows, title);
    }

    public Inventory chestInventory() {
        return this.chestInventory;
    }

    @Override
    public ItemStack get(final int row, final int column) {
        int index = BukkitInventoryUtilities.gridPointToBukkitIndex(row, column);
        return this.chestInventory.getItem(index);
    }

    @Override
    public void setInternal(final int row, final int column, final ItemStack item) {
        int index = BukkitInventoryUtilities.gridPointToBukkitIndex(row, column);
        this.chestInventory.setItem(index, item);
    }

    @Override
    public boolean isPlayerInventory(final int row, final int column) {
        return false;
    }
}
