package org.incendo.interfaces.paper.utils;

import com.google.common.base.Suppliers;

import java.util.function.Supplier;

import org.incendo.interfaces.core.util.Vector2;

/**
 * A collection of utility methods.
 */
@SuppressWarnings("unused")
public final class PaperUtils {

    private static final Supplier<Boolean> PAPER = Suppliers.memoize(() -> {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true;
        } catch (final ClassNotFoundException ignore) {
        }
        try {
            Class.forName("io.papermc.paper.configuration.PaperConfigurations");
            return true;
        } catch (final ClassNotFoundException ignore) {
        }
        return false;
    });

    /**
     * Converts a Bukkit slot index to an x/y position.
     *
     * @param slot the slot
     * @return the x/y position
     */
    public static Vector2 slotToGrid(final int slot) {
        return Vector2.at(slot % 9, slot / 9);
    }

    /**
     * Converts the x/y position to a Bukkit slot index.
     *
     * @param vector the vector
     * @return the slot
     */
    public static int gridToSlot(final Vector2 vector) {
        return vector.y() * 9 + vector.x();
    }

    /**
     * Check if the currently running server is Paper-based.
     *
     * @return if the current server is Paper-based
     */
    public static boolean isPaper() {
        return PAPER.get();
    }

    private PaperUtils() {}
}
