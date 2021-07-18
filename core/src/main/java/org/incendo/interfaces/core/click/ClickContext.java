package org.incendo.interfaces.core.click;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.core.view.InterfaceViewer;

/**
 * Represents the context of a click.
 *
 * @param <T> the pane's type
 * @param <U> the click cause
 * @param <V> the viewer type
 */
@SuppressWarnings("unused")
public interface ClickContext<T extends Pane, U, V extends InterfaceViewer> {

    /**
     * The click cause
     *
     * @return the cause
     */
    @NonNull U cause();

    /**
     * Returns true if this context is cancelling the click.
     *
     * @return true if the click is cancelled, false if not
     */
    boolean cancelled();

    /**
     * Sets the click to cancelled if {@code cancelled} is true.
     *
     * @param cancelled if true, will cancel the click
     */
    void cancel(boolean cancelled);

    /**
     * Returns the view associated with this context.
     *
     * @return the view
     */
    @NonNull InterfaceView<T, V> view();

    /**
     * Returns the viewer (the one who clicked).
     *
     * @return the viewer
     */
    @NonNull V viewer();

    /**
     * Returns the click associated with this context.
     *
     * @return the click
     */
    @NonNull Click<U> click();
}
