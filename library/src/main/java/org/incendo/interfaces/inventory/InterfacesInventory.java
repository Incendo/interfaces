package org.incendo.interfaces.inventory;

import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface InterfacesInventory {

    boolean isPlayerInventory(int row, int column);

    boolean set(int row, int column, @Nullable ItemStack item);

    @Nullable ItemStack get(int row, int column);
}
