package org.incendo.interfaces.core.view;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a target that can view an interface.
 */
public interface InterfaceViewer {

    /**
     * Displays a view to the viewer.
     *
     * @param pane the pane
     */
    void open(@NonNull InterfaceView<?, ?> pane);

    /**
     * Closes the open view, if any.
     */
    void close();

}
