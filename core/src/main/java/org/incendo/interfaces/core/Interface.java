package org.incendo.interfaces.core;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.arguments.InterfaceArgument;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.transform.Transform;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.core.view.InterfaceViewer;

import java.util.List;

/**
 * Represents an interface of a given pane type.
 *
 * @param <T> the pane type
 * @param <U> the viewer type
 * @see InterfaceView
 */
public interface Interface<T extends Pane, U extends InterfaceViewer> {

    /**
     * Adds a transformation to this interface.
     *
     * @param transform the transformation
     * @return this interface
     */
    @NonNull Interface<T, U> transform(@NonNull Transform<T> transform);

    /**
     * Returns an immutable collection of transformations.
     * <p>
     * Note: changes to this list will not apply to the interface
     *
     * @return the transformations
     */
    @NonNull List<Transform<T>> transformations();

    /**
     * Opens this interface to the viewer.
     *
     * @param viewer the viewer
     * @return the view
     */
    @NonNull InterfaceView<T, U> open(@NonNull U viewer);

    /**
     * Opens this interface to the viewer.
     *
     * @param viewer    the viewer
     * @param arguments the interface's arguments
     * @return the view
     */
    @NonNull InterfaceView<T, U> open(
            @NonNull U viewer,
            @NonNull InterfaceArgument arguments
    );

    /**
     * An interface that represents an interface builder.
     *
     * @param <T> the pane type
     * @param <U> the viewer type
     * @param <V> the interface type
     */
    interface Builder<T extends Pane, U extends InterfaceViewer, V extends Interface<T, U>> {

        Builder<T, U, V> addTransform(@NonNull Transform<T> transform);

        /**
         * Builds the interface and returns it.
         *
         * @return the interface
         */
        @NonNull V build();

    }

}
