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

}
