package dev.kscott.interfaces.core.view;

import dev.kscott.interfaces.core.Interface;
import dev.kscott.interfaces.core.pane.Pane;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a target that can view an interface.
 */
public interface InterfaceViewer {

    /**
     * Displays a pane to the viewer.
     *
     * @param pane the pane
     */
    void open(final @NonNull Pane pane);

}
