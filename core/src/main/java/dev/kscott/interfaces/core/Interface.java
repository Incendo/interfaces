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
public interface Interface<T extends Pane> {

    /**
     * Adds a transformation to this interface.
     *
     * @param transform the transformation
     * @return this interface
     */
    @NonNull <U extends Interface<T>> U transform(final @NonNull Transform<T> transform);

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
    @NonNull <U extends InterfaceView<T, Interface<T>>> T open(final @NonNull InterfaceViewer viewer);

    /**
     * Opens this interface to the viewer.
     *
     * @param viewer the viewer
     * @param arguments the interface's arguments
     * @return the view
     */
    @NonNull <U extends InterfaceView<T, Interface<T>>> T open(final @NonNull InterfaceViewer viewer,
                                final @NonNull InterfaceArgument arguments);

    /**
     * An interface that represents an interface builder.
     */
    interface Builder<U extends Interface> {

//        void addTransform(final @NonNull Transform<?>)

        /**
         * Builds the interface and returns it.
         *
         * @return the interface
         */
        @NonNull U build();

    }
}
