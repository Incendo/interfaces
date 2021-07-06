package org.incendo.interfaces.paper.view;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a view which can hold a parent.
 */
public interface ChildView {

    /**
     * Returns true if this view has a parent, false if not.
     *
     * @return true if this view has a parent, false if not
     */
    boolean hasParent();

    /**
     * Returns the parent view, if any.
     *
     * @return the parent view, if any
     */
    @Nullable PlayerView<?> parent();

    /**
     * Returns to the parent view.
     *
     * @return the parent view
     * @throws NullPointerException if there is no parent view
     * @see #hasParent()
     * @see #parent()
     */
    @NonNull PlayerView<?> back();

}
