package org.incendo.interfaces.paper.pane;

import org.incendo.interfaces.core.element.Element;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A pane based off of a Minecraft chest inventory.
 */
public class ChestPane implements Pane {

    /**
     * The width of a Minecraft chest.
     */
    public static final int MINECRAFT_CHEST_WIDTH = 9;

    /**
     * The 2d elements array.
     */
    private final @NonNull List<List<ItemStackElement>> elements;

    /**
     * The amount of rows this inventory has.
     */
    private final int rows;

    /**
     * Constructs {@code ChestPane}.
     *
     * @param rows the amount of rows
     */
    public ChestPane(final int rows) {
        this.rows = rows;

        this.elements = new ArrayList<>();

        // Fill the arrays with empty elements.
        for (int i = 0; i < MINECRAFT_CHEST_WIDTH; i++) {
            final @NonNull List<ItemStackElement> deepElements = new ArrayList<>();

            for (int j = 0; j < this.rows; j++) {
                deepElements.add(j, ItemStackElement.empty());
            }

            this.elements.add(deepElements);
        }
    }

    /**
     * Constructs {@code ChestPane}.
     *
     * @param rows     amount of rows
     * @param elements the elements
     */
    public ChestPane(final int rows, final @NonNull List<List<ItemStackElement>> elements) {
        this.rows = rows;
        this.elements = elements;
    }

    /**
     * Returns the elements of the chest as a 2d array.
     *
     * @return the elements
     */
    public @NonNull List<List<ItemStackElement>> chestElements() {
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

    /**
     * Returns the elements, where the first 9 elements are row 1, the second 9 elements are row 2, etc...
     *
     * @return the elements collection
     */
    @Override
    public @NonNull Collection<Element> elements() {
        final @NonNull List<Element> tempElements = new ArrayList<>();

        // Fill the temp elements list with the elements.
        for (int i = 0; i < MINECRAFT_CHEST_WIDTH; i++) {
            for (int j = 0; j < this.rows; j++) {
                tempElements.add(this.elements.get(i).get(this.rows));
            }
        }

        return tempElements;
    }

    /**
     * Sets an element at the given position.
     * <p>
     * This method returns an updated instance of ChestPane with the new element.
     *
     * @param element the element
     * @param x       the x coordinate
     * @param y       the y coordinate
     */
    public @NonNull ChestPane element(final @NonNull ItemStackElement element, final int x, final int y) {
        final @NonNull List<List<ItemStackElement>> newElements = new ArrayList<>();

        for (final @NonNull List<ItemStackElement> elements : this.elements) {
            newElements.add(new ArrayList<>(elements));
        }

        newElements.get(x).set(y, element);

        return new ChestPane(this.rows, newElements);
    }

    /**
     * Returns the element at the given position.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the element
     */
    public @NonNull ItemStackElement element(final int x, final int y) {
        return this.elements.get(x).get(y);
    }

}
