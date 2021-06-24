package dev.kscott.interfaces.paper.transform;


import dev.kscott.interfaces.core.transform.Transform;
import dev.kscott.interfaces.paper.element.ItemStackElement;
import dev.kscott.interfaces.paper.pane.ChestPane;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Utility methods for Paper transformations.
 */
public interface PaperTransform {

    /**
     * Returns a {@link ChestPane} {@link Transform} that fills the pane with an ItemStack.
     *
     * @param element the element
     * @return the transform
     */
    static @NonNull Transform<ChestPane> chestFill(final @NonNull ItemStackElement element) {
        return (pane, view) -> {
            final int length = ChestPane.MINECRAFT_CHEST_WIDTH;
            final int height = pane.rows();

            for (int x = 0; x < length; x++) {
                for (int y = 0; y < height; y++) {
                    pane = pane.element(element, x, y);
                }
            }

            return pane;
        };
    }

    /**
     * Returns a {@link ChestPane} {@link Transform} that adds an ItemStack to the pane.
     *
     * @param element the element
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the transform
     */
    static @NonNull Transform<ChestPane> chestItem(final @NonNull ItemStackElement element, final int x, final int y) {
        return (pane, view) -> {
            return pane.element(element, x, y);
        };
    }

}
