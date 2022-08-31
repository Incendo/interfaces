package org.incendo.interfaces.paper.transform;


import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.transform.Transform;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.element.text.TextElement;
import org.incendo.interfaces.paper.pane.BookPane;
import org.incendo.interfaces.paper.pane.ChestPane;

/**
 * Utility methods for Paper transformations.
 */
@SuppressWarnings("unused")
public interface PaperTransform {

    /**
     * Returns a {@link ChestPane} {@link Transform} that fills the pane with an ItemStack.
     *
     * @param element the element
     * @return the transform
     */
    static @NonNull Transform<ChestPane, PlayerViewer> chestFill(final @NonNull ItemStackElement<ChestPane> element) {
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
     * @param x       the x coordinate
     * @param y       the y coordinate
     * @return the transform
     */
    static @NonNull Transform<ChestPane, PlayerViewer> chestItem(
            final @NonNull ItemStackElement<ChestPane> element, final int x,
            final int y
    ) {
        return (pane, view) -> pane.element(element, x, y);
    }

    /**
     * Returns a {@link Transform} that adds text to the pane.
     *
     * @param pages the pages as components
     * @return the transform
     */
    static @NonNull Transform<BookPane, PlayerViewer> bookText(final @NonNull Component... pages) {
        return (pane, view) -> {
            @NonNull BookPane bookPane = pane;

            for (final @NonNull Component page : pages) {
                bookPane = bookPane.add(TextElement.text(page));
            }

            return bookPane;
        };
    }

}
