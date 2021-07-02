package org.incendo.interfaces.core.pane;

import org.incendo.interfaces.core.element.Element;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

/**
 * A pane represents the method of viewing the interface. A pane
 * is just a collection of {@link Element}s.
 * <p>
 * For example, a pane may be text-only (using elements holding text), or a pane may be a grid of icon elements.
 */
public interface Pane {

    /**
     * Returns a collection containing all the elements of this pane.
     *
     * @return the element collection
     */
    @NonNull Collection<Element> elements();

}
