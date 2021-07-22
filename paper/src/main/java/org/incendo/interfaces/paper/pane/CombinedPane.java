package org.incendo.interfaces.paper.pane;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.element.Element;
import org.incendo.interfaces.core.pane.GridPane;
import org.incendo.interfaces.core.util.Vector2;
import org.incendo.interfaces.paper.element.ItemStackElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class CombinedPane implements GridPane<CombinedPane, ItemStackElement<CombinedPane>> {

    public static final int PLAYER_INVENTORY_ROWS = 4;

    private final @NonNull Map<@NonNull Vector2, @NonNull ItemStackElement<CombinedPane>> elements;

    private final ItemStackElement<CombinedPane>[] hotbar;

    private final int rows;

    /**
     * Constructs {@code CombinePane}.
     *
     * @param rows the amount of rows
     */
    public CombinedPane(final int rows) {
        this.rows = rows;
        this.elements = new HashMap<>();
        this.hotbar = new ItemStackElement[9];

        for (int x = 0; x < ChestPane.MINECRAFT_CHEST_WIDTH; x++) {
            for (int y = 0; y < this.rows; y++) {
                this.elements.put(Vector2.at(x, y), ItemStackElement.empty());
            }
        }

        for (int x = 0; x < ChestPane.MINECRAFT_CHEST_WIDTH; x++) {
            this.hotbar[x] = ItemStackElement.empty();
        }
    }

    /**
     * Constructs {@code CombinePane}.
     *
     * @param rows     amount of rows
     * @param elements the chest elements
     * @param hotbar the hotbar elements
     */
    public CombinedPane(
            final int rows,
            final @NonNull Map<@NonNull Vector2, @NonNull ItemStackElement<CombinedPane>> elements,
            final ItemStackElement<CombinedPane>[] hotbar
    ) {
        this.rows = rows;
        this.elements = elements;
        this.hotbar = hotbar;
    }

    /**
     * Returns the inventory elements as a vector map.
     *
     * @return the elements
     */
    public @NonNull Map<@NonNull Vector2, @NonNull ItemStackElement<CombinedPane>> inventoryElements() {
        return this.elements;
    }

    /**
     * Returns the hotbar elements as a 2d array.
     *
     * @return the elements
     */
    public @NonNull ItemStackElement<CombinedPane>[] hotbarElements() {
        return this.hotbar;
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
        return new ArrayList<>(this.elements.values());
    }

    @Override
    public @NonNull CombinedPane element(
            final @NonNull ItemStackElement<CombinedPane> element,
            final int x,
            final int y
    ) {
        final Map<Vector2, ItemStackElement<CombinedPane>> newElements = new HashMap<>(this.elements);
        newElements.put(Vector2.at(x, y), element);

        return new CombinedPane(this.rows, newElements, this.hotbar);
    }

    /**
     * Create a new combined pane with a new element in the hotbar
     *
     * @param element the element to use
     * @param x the slot in the hotbar
     * @return a new combined pane
     */
    public @NonNull CombinedPane hotbar(
            final @NonNull ItemStackElement<CombinedPane> element,
            final int x
    ) {
        final ItemStackElement<CombinedPane>[] newHotbar = Arrays.copyOf(this.hotbar, this.hotbar.length);
        newHotbar[x] = element;

        return new CombinedPane(this.rows, this.elements, newHotbar);
    }

    @Override
    public @NonNull ItemStackElement<CombinedPane> element(final int x, final int y) {
        return this.elements.get(Vector2.at(x, y));
    }

    /**
     * Retrieve an element from the hotbar
     *
     * @param x the slot in the hotbar
     * @return the element
     */
    public @NonNull ItemStackElement<CombinedPane> hotbar(
            final int x
    ) {
        return this.hotbar[x];
    }

}
