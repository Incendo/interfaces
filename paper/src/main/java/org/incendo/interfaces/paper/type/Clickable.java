package org.incendo.interfaces.paper.type;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.click.ClickContext;
import org.incendo.interfaces.core.click.ClickHandler;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.view.InterfaceViewer;

/**
 * Represents a clickable interface.
 * <p>
 * {@code clickHandler} will be called whenever the viewer clicks on the interface.
 *
 * @param <T> the pane type
 * @param <U> the click cause type
 * @param <V> the viewer type
 */
public interface Clickable<T extends Pane, U, V extends InterfaceViewer> {

    /**
     * Returns the top click handler.
     *
     * @return the top click handler
     */
    @NonNull ClickHandler<T, U, V, ? extends ClickContext<T, U, V>> clickHandler();

}
