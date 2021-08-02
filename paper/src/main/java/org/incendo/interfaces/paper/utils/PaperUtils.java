package org.incendo.interfaces.paper.utils;

import org.incendo.interfaces.core.util.Vector2;

/**
 * A collection of utility methods.
 */
@SuppressWarnings("unused")
public final class PaperUtils {

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

    private PaperUtils() {}
}
