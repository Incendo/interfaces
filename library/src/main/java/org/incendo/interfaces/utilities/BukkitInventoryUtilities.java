package org.incendo.interfaces.utilities;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.incendo.interfaces.grid.GridPoint;

import java.util.function.BiConsumer;

import static org.incendo.interfaces.view.AbstractInterfaceView.COLUMNS_IN_CHEST;

public final class BukkitInventoryUtilities {

    private BukkitInventoryUtilities() {
    }

    public static int gridPointToBukkitIndex(final int row, final int column) {
        return row * COLUMNS_IN_CHEST + column;
    }

    public static int gridPointToBukkitIndex(final GridPoint gridPoint) {
        return gridPointToBukkitIndex(gridPoint.x(), gridPoint.y());
    }

    public static void forEachInGrid(final int rows, final int columns, final BiConsumer<Integer, Integer> function) {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                function.accept(row, column);
            }
        }
    }

    public static Inventory createBukkitInventory(final InventoryHolder holder, final int rows, final Component title) {
        if (title == null) {
            return Bukkit.createInventory(holder, rows * COLUMNS_IN_CHEST);
        }

        return Bukkit.createInventory(holder, rows * COLUMNS_IN_CHEST, title);
    }

}
