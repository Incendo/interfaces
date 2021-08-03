package org.incendo.interfaces.paper.element;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.click.ClickHandler;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.view.InterfaceViewer;

import java.util.UUID;

/**
 * Represents an element that can be clicked.
 *
 * @param <T> the pane type
 * @param <U> the click context type
 * @param <V> the viewer type
 */
public interface ClickableElement<T extends Pane, U, V extends InterfaceViewer> {

    /**
     * Returns the UUID of this element.
     *
     * @return the uuid
     */
    @NonNull UUID uuid();

    /**
     * Returns the click handler.
     *
     * @return the click handler
     */
    @NonNull ClickHandler<T, U, V, ?> clickHandler();

}
