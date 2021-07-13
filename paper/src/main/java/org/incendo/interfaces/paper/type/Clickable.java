package org.incendo.interfaces.paper.type;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.click.ClickContext;
import org.incendo.interfaces.core.click.ClickHandler;
import org.incendo.interfaces.core.pane.Pane;

/**
 * Represents a clickable interface.
 * <p>
 * {@code clickHandler} will be called whenever the viewer clicks on the interface.
 *
 * @param <T> the pane type
 * @param <U> the click cause type
 */
public interface Clickable<T extends Pane, U> {

    /**
     * Returns the top click handler.
     *
     * @return the top click handler
     */
    @NonNull ClickHandler<T, U, ? extends ClickContext<T, U>> clickHandler();

}
