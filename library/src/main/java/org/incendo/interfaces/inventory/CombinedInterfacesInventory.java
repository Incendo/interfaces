package org.incendo.interfaces.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.incendo.interfaces.utilities.BukkitInventoryUtilities;

import static org.incendo.interfaces.view.AbstractInterfaceView.COLUMNS_IN_CHEST;

public final class CombinedInterfacesInventory extends CachedInterfacesInventory {

    private final int rows;
    private final int chestSlots;

    private final PlayerInventory playerInventory;
    private final Inventory chestInventory;

    public CombinedInterfacesInventory(
        final InventoryHolder holder,
        final Player player,
        final Component title,
        final int rows
    ) {
        this.rows = rows;
        this.chestSlots = rows * COLUMNS_IN_CHEST;
        this.playerInventory = player.getInventory();
        this.chestInventory = BukkitInventoryUtilities.createBukkitInventory(holder, rows, title);
    }

    public Inventory chestInventory() {
        return this.chestInventory;
    }

    @Override
    public ItemStack get(final int row, final int column) {
        int bukkitIndex = BukkitInventoryUtilities.gridPointToBukkitIndex(row, column);

        if (row >= this.rows) {
            int adjustedIndex = bukkitIndex - this.chestSlots;
            return this.playerInventory.getItem(adjustedIndex);
        }

        return this.chestInventory.getItem(bukkitIndex);
    }

    @Override
    public void setInternal(final int row, final int column, final ItemStack item) {
        int bukkitIndex = BukkitInventoryUtilities.gridPointToBukkitIndex(row, column);

        if (row >= this.rows) {
            int adjustedIndex = bukkitIndex - this.chestSlots;
            this.playerInventory.setItem(adjustedIndex, item);
            return;
        }

        this.chestInventory.setItem(bukkitIndex, item);
    }

    @Override
    public boolean isPlayerInventory(final int row, final int column) {
        return row >= this.rows;
    }
}
