package dev.kscott.interfaces.core.pane;

import dev.kscott.interfaces.core.element.Element;
import dev.kscott.interfaces.core.ItemStackElement;
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
     * Returns the amount of rows this pane has.
     *
     * @return the amount of rows
     */
    public int rows() {
        return this.rows;
    }

    @Override
    public @NonNull Collection<Element> elements() {
        final @NonNull List<Element> elementsList = new ArrayList<>();

        for (int x = 0; x < this.elements.length; x++) {
            elementsList.addAll(Arrays.asList(this.elements[x]));
        }

        return List.copyOf(elementsList);
    }

}
