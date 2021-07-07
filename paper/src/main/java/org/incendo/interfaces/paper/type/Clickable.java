package org.incendo.interfaces.paper.type;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.paper.element.ClickHandler;

/**
 * Represents a clickable interface.
 * <p>
 * {@code clickHandler} will be called whenever the viewer clicks on the interface.
 */
public interface Clickable<T extends Pane> {

    /**
     * Returns the top click handler.
     *
     * @return the top click handler
     */
    @NonNull ClickHandler<T> clickHandler();

}
