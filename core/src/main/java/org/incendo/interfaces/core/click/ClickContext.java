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
     * The status of the click.
     *
     * @return the status of the click
     */
    @NonNull ClickStatus status();

    /**
     * Sets the status of the click.
     *
     * @param status the status
     */
    void status(@NonNull ClickStatus status);

    /**
     * Returns true if this context is cancelling the click.
     *
     * @return true if the click is cancelled, false if not
     */
    default boolean cancelled() {
        return this.status() == ClickStatus.DENY;
    }

    /**
     * Sets the click to cancelled if {@code cancelled} is true.
     *
     * @param cancelled if true, will cancel the click
     */
    default void cancel(boolean cancelled) {
        if (cancelled) {
            this.status(ClickStatus.DENY);
        } else {
            this.status(ClickStatus.ALLOW);
        }
    }

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

    /**
     * The status of a click event.
     */
    enum ClickStatus {
        /** The click is allowed and the triggering event won't be cancelled. */
        ALLOW,
        /**
         * The click status will be inherited from any previous click handlers.
         * If there are no previous click handlers, the default behavior is to allow the click.
         */
        INHERIT,
        /** The click is denied and the triggering event will be cancelled. */
        DENY
    }

}
