package org.incendo.interfaces.paper.utils;

/**
 * A collection of utility methods.
 */
public final class PaperUtils {

    /**
     * Converts a Bukkit slot index to an x/y position.
     *
     * @param slot the slot
     * @return the x/y position
     */
    public static int[] slotToGrid(final int slot) {
        return new int[]{slot % 9, slot / 9};
    }

    /**
     * Converts the x/y position to a Bukkit slot index.
     *
     * @param x the x position
     * @param y the y position
     * @return the slot
     */
    public static int gridToSlot(final int x, final int y) {
        return y * 9 + x;
    }

    private PaperUtils() {}
}
