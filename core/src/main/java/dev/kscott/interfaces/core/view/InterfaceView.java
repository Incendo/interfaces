package dev.kscott.interfaces.core.view;

import dev.kscott.interfaces.core.arguments.InterfaceArgument;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a currently open interface (a "view").
 *
 * @apiNote take a shot whenever you read "view" in these docs.
 */
public interface InterfaceView {

    /**
     * Returns the viewer of this view.
     * <p>
     * This will always return a value; every view must be constructed
     * with a viewer. This does not mean that the viewer is currently viewing
     * this view.
     *
     * @return the viewer
     * @see  #viewing() check if the viewer is currently viewing this view
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


}
