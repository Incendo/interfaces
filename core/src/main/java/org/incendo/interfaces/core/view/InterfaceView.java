package org.incendo.interfaces.core.view;

import org.incendo.interfaces.core.Interface;
import org.incendo.interfaces.core.arguments.InterfaceArgument;
import org.incendo.interfaces.core.pane.Pane;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a currently open interface (a "view").
 *
 * @param <T> the type of pane this view can view
 * @param <U> the viewer type
 * @param <V> the interface type
 */
public interface InterfaceView<T extends Pane, U extends InterfaceViewer, V extends Interface<T, U>> {

    /**
     * Returns the parent interface.
     *
     * @return the interface
     */
    @NonNull Interface<T, U> parent();

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
    @NonNull InterfaceViewer viewer();

    /**
     * Returns true if the viewer is currently viewing this view. False
     * if not.
     *
     * @return true if viewing, false if not
     */
    boolean viewing();

    /**
     * Returns the argument provided to this view.
     *
     * @return the view's argument
     */
    @NonNull InterfaceArgument argument();

    /**
     * Opens the view to the viewer.
     *
     * @see #viewer()
     */
    void open();

    /**
     * Returns the pane.
     *
     * @return the pane
     */
    T pane();

}
