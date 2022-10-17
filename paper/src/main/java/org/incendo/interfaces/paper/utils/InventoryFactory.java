package org.incendo.interfaces.paper.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.lang.reflect.Method;

@DefaultQualifier(NonNull.class)
public final class InventoryFactory {

    private static @MonotonicNonNull Method paperCreateInventory;

    private InventoryFactory() {
    }

    /**
     * Create an inventory with the appropriate method for the platform.
     *
     * @param inventoryHolder holder
     * @param size size
     * @param title title
     * @return inventory
     */
    public static Inventory createInventory(final InventoryHolder inventoryHolder, final int size, final Component title) {
        if (PaperUtils.isPaper()) {
            return paperCreateInventory(inventoryHolder, size, title);
        }
        return legacyCreateInventory(inventoryHolder, size, title);
    }

    @SuppressWarnings("deprecation")
    private static Inventory legacyCreateInventory(final InventoryHolder inventoryHolder, final int size, final Component title) {
        return Bukkit.getServer().createInventory(
                inventoryHolder,
                size,
                LegacyComponentSerializer.legacySection().serialize(title)
        );
    }

    private static Inventory paperCreateInventory(final InventoryHolder inventoryHolder, final int size, final Component title) {
        try {
            return (Inventory) paperCreateInventoryMethod().invoke(
                    Bukkit.getServer(),
                    inventoryHolder,
                    size,
                    Components.toNative(title)
            );
        } catch (final ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static Method paperCreateInventoryMethod() {
        if (paperCreateInventory == null) {
            try {
                paperCreateInventory = Server.class.getMethod(
                        "createInventory",
                        InventoryHolder.class,
                        int.class,
                        Components.nativeAdventureComponentClass()
                );
            } catch (final ReflectiveOperationException ex) {
                throw new RuntimeException(ex);
            }
        }
        return paperCreateInventory;
    }

}
