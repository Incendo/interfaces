package org.incendo.interfaces.core;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.arguments.InterfaceArguments;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.transform.InterfaceProperty;
import org.incendo.interfaces.core.transform.ReactiveTransform;
import org.incendo.interfaces.core.transform.Transform;
import org.incendo.interfaces.core.transform.TransformContext;
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
    @NonNull Interface<T, U> transform(@NonNull Transform<T, U> transform);

    /**
     * Returns an immutable collection of transformations.
     * <p>
     * Note: changes to this list will not apply to the interface
     *
     * @return the transformations
     */
    @NonNull List<TransformContext<T, U>> transformations();

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
            @NonNull InterfaceArguments arguments
    );

    /**
     * Opens an interface with a parent view.
     *
     * @param view      the parent view
     * @param arguments the interface's arguments
     * @return the view
     */
    @NonNull InterfaceView<T, U> open(
            @NonNull InterfaceView<?, U> view,
            @NonNull InterfaceArguments arguments
    );

    /**
     * An interface that represents an interface builder.
     *
     * @param <T> the pane type
     * @param <U> the viewer type
     * @param <V> the interface type
     */
    @SuppressWarnings("unchecked")
    interface Builder<T extends Pane, U extends InterfaceViewer, V extends Interface<T, U>> {

        /**
         * Add a transformation to the Interface
         *
         * @param transform the transformation to add
         * @return the builder
         */
        @NonNull Builder<T, U, V> addTransform(@NonNull Transform<T, U> transform);

        /**
         * Adds a transformation to the Interface that will update automatically when it's {@link InterfaceProperty} is changed.
         * This transformation will have a priority of {@code 1}.
         *
         * @param properties  the interface properties to depend on
         * @param transform the transformation to add
         * @return the builder
         */
        default @NonNull Builder<T, U, V> addTransform(
                @NonNull Transform<T, U> transform,
                @NonNull InterfaceProperty<?>... properties
        ) {
            return this.addTransform(1, transform, properties);
        }

        /**
         * Adds a transformation to the Interface that will update automatically when it's {@link InterfaceProperty} is changed
         * This transformation will use the provided priority.
         *
         * @param properties the interface properties to depend on
         * @param priority the priority of the transformation
         * @param transform the transformation to add
         * @return the builder
         */
        @NonNull Builder<T, U, V> addTransform(
                int priority,
                @NonNull Transform<T, U> transform,
                @NonNull InterfaceProperty<?>... properties
        );

        /**
         * Adds a transformation to the Interface that will update automatically when it's {@link InterfaceProperty} is changed
         * This transformation will use the provided priority.
         *
         * @param priority the priority of the transformation
         * @param transform the transformation to add
         * @return the builder
         */
        default @NonNull Builder<T, U, V> addReactiveTransform(
                int priority,
                @NonNull ReactiveTransform<T, U, ?> transform
        ) {
            return this.addTransform(priority, transform, transform.properties());
        }

        /**
         * Adds a transformation to the Interface that will update automatically when it's {@link InterfaceProperty} is changed
         * This transformation will use the provided priority.
         *
         * @param transform the transformation to add
         * @param <S> the value type the interface property holds
         * @return the builder
         */
        default <S> @NonNull Builder<T, U, V> addReactiveTransform(
                @NonNull ReactiveTransform<T, U, S> transform
        ) {
            return this.addTransform(1, transform, transform.properties());
        }


        /**
         * Builds the interface and returns it.
         *
         * @return the interface
         */
        @NonNull V build();

    }

}
