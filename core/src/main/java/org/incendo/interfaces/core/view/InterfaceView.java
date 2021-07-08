package org.incendo.interfaces.core.view;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.Interface;
import org.incendo.interfaces.core.arguments.InterfaceArgument;
import org.incendo.interfaces.core.pane.Pane;

/**
 * Represents a currently open interface (a "view").
 *
 * @param <T> the type of pane this view can view
 * @param <U> the viewer type
 */
public interface InterfaceView<T extends Pane, U extends InterfaceViewer> {

    /**
     * Returns the parent interface.
     *
     * @return the interface
     */
    @NonNull Interface<T, U> backing();

    /**
     * Returns the argument provided to this view.
     *
     * @return the view's argument
     */
    @NonNull InterfaceArgument argument();

    /**
     * Returns the viewer of this view.
     * <p>
     * This will always return a value; every view must be constructed
     * with a viewer. This does not mean that the viewer is currently viewing
     * this view.
     *
     * @return the viewer
     * @see #viewing() check if the viewer is currently viewing this view
     */
    @NonNull U viewer();

    /**
     * Returns true if the viewer is currently viewing this view. False
     * if not.
     *
     * @return true if viewing, false if not
     */
    boolean viewing();

    /**
     * Returns the pane.
     *
     * @return the pane
     */
    T pane();

    /**
     * Opens the view to the viewer.
     *
     * @see #viewer()
     */
    void open();

    /**
     * Triggers a manual update.
     */
    default void update() {
        if (this.viewing()) {
            this.backing().open(this.viewer(), this.argument());
        }
    }

}
