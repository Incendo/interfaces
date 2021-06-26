package dev.kscott.interfaces.core;

import dev.kscott.interfaces.core.arguments.InterfaceArgument;
import dev.kscott.interfaces.core.pane.Pane;
import dev.kscott.interfaces.core.transform.Transform;
import dev.kscott.interfaces.core.view.InterfaceView;
import dev.kscott.interfaces.core.view.InterfaceViewer;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * Represents an interface of a given pane type.
 *
 * @param <T> the pane type
 * @see InterfaceView
 */
public interface Interface<T extends Pane, U extends InterfaceViewer> {

    /**
     * Adds a transformation to this interface.
     *
     * @param transform the transformation
     * @return this interface
     */
    @NonNull Interface<T, U> transform(final @NonNull Transform<T> transform);

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
    @NonNull InterfaceView<T, U, Interface<T, U>> open(final @NonNull U viewer);

    /**
     * Opens this interface to the viewer.
     *
     * @param viewer    the viewer
     * @param arguments the interface's arguments
     * @return the view
     */
    @NonNull InterfaceView<T, U, Interface<T, U>> open(
            final @NonNull U viewer,
            final @NonNull InterfaceArgument arguments
    );

    /**
     * An interface that represents an interface builder.
     */
    interface Builder<T extends Pane, U extends InterfaceViewer, V extends Interface<T, U>> {

        Builder<T, U, V> addTransform(final @NonNull Transform<T> transform);

        /**
         * Builds the interface and returns it.
         *
         * @return the interface
         */
        @NonNull V build();

    }

}
