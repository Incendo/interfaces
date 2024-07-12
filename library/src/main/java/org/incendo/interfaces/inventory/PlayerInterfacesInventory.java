package org.incendo.interfaces.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.incendo.interfaces.utilities.BukkitInventoryUtilities;

public final class PlayerInterfacesInventory extends CachedInterfacesInventory {

    private final Player player;

    public PlayerInterfacesInventory(final Player player) {
        this.player = player;
    }

    @Override
    public ItemStack get(final int row, final int column) {
        int index = BukkitInventoryUtilities.gridPointToBukkitIndex(row, column);
        return this.player.getInventory().getItem(index);
    }

    @Override
    public void setInternal(final int row, final int column, final ItemStack item) {
        int index = BukkitInventoryUtilities.gridPointToBukkitIndex(row, column);
        this.player.getInventory().setItem(index, item);
    }

    @Override
    public boolean isPlayerInventory(final int row, final int column) {
        return true;
    }
}

