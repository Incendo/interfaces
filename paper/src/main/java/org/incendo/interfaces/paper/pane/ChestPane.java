package org.incendo.interfaces.paper.pane;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.element.Element;
import org.incendo.interfaces.core.pane.GridPane;
import org.incendo.interfaces.core.util.Vector2;
import org.incendo.interfaces.paper.element.ItemStackElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A pane based off of a Minecraft chest inventory.
 */
public final class ChestPane implements GridPane<ChestPane, ItemStackElement<ChestPane>> {

    public static final int MINECRAFT_CHEST_WIDTH = 9;

    private final @NonNull Map<@NonNull Vector2, @NonNull ItemStackElement<ChestPane>> elements;

    private final int rows;

    /**
     * Constructs {@code ChestPane}.
     *
     * @param rows the amount of rows
     */
    public ChestPane(final int rows) {
        this.rows = rows;

        this.elements = new HashMap<>();

        // Fill the arrays with empty elements.
        for (int x = 0; x < MINECRAFT_CHEST_WIDTH; x++) {
            for (int y = 0; y < this.rows; y++) {
                this.elements.put(Vector2.at(x, y), ItemStackElement.empty());
            }
        }
    }

    /**
     * Constructs {@code ChestPane}.
     *
     * @param rows     amount of rows
     * @param elements the elements
     */
    public ChestPane(
            final int rows,
            final @NonNull Map<@NonNull Vector2, @NonNull ItemStackElement<ChestPane>> elements
    ) {
        this.rows = rows;
        this.elements = elements;
    }

    /**
     * Returns the elements of the chest as a 2d array.
     *
     * @return the elements
     */
    public @NonNull Map<@NonNull Vector2, @NonNull ItemStackElement<ChestPane>> chestElements() {
        return this.elements;
    }

    /**
     * Returns the amount of rows this pane has.
     *
     * @return the amount of rows
     */
    public int rows() {
        return this.rows;
    }

    @Override
    public @NonNull Collection<Element> elements() {
        final @NonNull List<Element> tempElements = new ArrayList<>();

        // Fill the temp elements list with the elements.
        for (int x = 0; x < MINECRAFT_CHEST_WIDTH; x++) {
            for (int y = 0; y < this.rows; y++) {
                tempElements.add(this.elements.get(Vector2.at(x, y)));
            }
        }

        return tempElements;
    }

    @Override
    public @NonNull ChestPane element(
            final @NonNull ItemStackElement<ChestPane> element,
            final int x,
            final int y
    ) {
        final Map<@NonNull Vector2, @NonNull ItemStackElement<ChestPane>> newElements = new HashMap<>(this.elements);

        newElements.put(Vector2.at(x, y), element);

        return new ChestPane(this.rows, newElements);
    }

    @Override
    public @NonNull ItemStackElement<ChestPane> element(final int x, final int y) {
        if (y >= this.rows || x >= 8) {
            throw new IllegalArgumentException("Cannot access element outside of the bounds of this chest pane.");
        }

        return this.elements.get(Vector2.at(x, y));
    }

}
