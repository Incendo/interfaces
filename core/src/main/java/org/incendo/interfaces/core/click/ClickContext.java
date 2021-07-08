package org.incendo.interfaces.core.click;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.view.InterfaceView;

/**
 * Represents the context of a click.
 *
 * @param <T> the pane's type
 */
public interface ClickContext<T extends Pane> {

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
    @NonNull InterfaceView<T, ?> view();

    /**
     * Returns true if this click was a left click.
     *
     * @return true if this click was a left click
     */
    boolean leftClick();

    /**
     * Returns true if this click was a right click.
     *
     * @return true if this click was a right click
     */
    boolean rightClick();

    /**
     * Returns true if this click was a shift click.
     *
     * @return true if this click was a shift click
     */
    boolean shiftClick();

    /**
     * Returns true if this click was a middle click.
     *
     * @return true if this click was a middle click
     */
    boolean middleClick();

}
