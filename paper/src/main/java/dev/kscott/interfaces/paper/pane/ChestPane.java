package dev.kscott.interfaces.paper.pane;

import dev.kscott.interfaces.core.element.Element;
import dev.kscott.interfaces.core.pane.Pane;
import dev.kscott.interfaces.paper.element.ItemStackElement;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A pane based off of a Minecraft chest inventory.
 */
public class ChestPane implements Pane {

    /**
     * The 2d elements array.
     */
    private final @NonNull ItemStackElement[][] elements;

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

        this.elements = new ItemStackElement[9][this.rows];

        // Fill the arrays with empty elements.
        for (final ItemStackElement[] element : this.elements) {
            Arrays.fill(element, ItemStackElement.empty());
        }

    }

    /**
     * Returns the elements of the chest as a 2d array.
     *
     * @return the elements
     */
    public @NonNull ItemStackElement[][] chestElements() {
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
        final @NonNull List<Element> elementsList = new ArrayList<>();

        for (final ItemStackElement[] element : this.elements) {
            elementsList.addAll(Arrays.asList(element));
        }

        return List.copyOf(elementsList);
    }

    /**
     * Sets an element at the given position.
     *
     * @param element the element
     * @param x       the x coordinate
     * @param y       the y coordinate
     */
    public void element(final @NonNull ItemStackElement element, final int x, final int y) {
        this.elements[x][y] = element;
    }

    /**
     * Returns the element at the given position.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the element
     */
    public @NonNull ItemStackElement element(final int x, final int y) {
        return this.elements[x][y];
    }

}
