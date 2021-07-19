package org.incendo.interfaces.core.pane;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.element.Element;

/**
 * A pane with a 2D grid.
 *
 * @param <T> the pane type
 * @param <U> the element type
 */
public interface GridPane<T extends Pane, U extends Element> extends Pane {

    /**
     * Sets an element at the given position.
     * <p>
     * This method returns an updated instance of this pane with the new element.
     *
     * @param element the element
     * @param x       the x coordinate
     * @param y       the y coordinate
     * @return a new pane
     */
    @NonNull T element(@NonNull U element, int x, int y);

    /**
     * Returns the element at the given position.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the element
     */
    @NonNull U element(int x, int y);

}
