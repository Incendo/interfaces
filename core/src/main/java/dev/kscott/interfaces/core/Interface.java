package dev.kscott.interfaces.core;

import dev.kscott.interfaces.core.arguments.InterfaceArguments;
import dev.kscott.interfaces.core.pane.Pane;
import dev.kscott.interfaces.core.view.InterfaceView;
import dev.kscott.interfaces.core.view.InterfaceViewer;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents an interface of a given pane type.
 *
 * @param <T> the pane type
 * @see InterfaceView
 */
public interface Interface<T extends Pane> {

    /**
     * Opens this interface to the viewer.
     *
     * @param viewer the viewer
     * @return the view
     */
    @NonNull InterfaceView open(final @NonNull InterfaceViewer viewer);

    /**
     * Opens this interface to the viewer.
     *
     * @param viewer the viewer
     * @param arguments the interface's arguments
     * @return the view
     */
    @NonNull InterfaceView open(final @NonNull InterfaceViewer viewer,
                                final @NonNull InterfaceArguments arguments);

}
