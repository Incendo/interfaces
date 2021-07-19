package org.incendo.interfaces.core.transform;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.view.InterfaceViewer;

/**
 * @param <T> the type of the value this context is dependent on
 * @param <U> the type of the pane that this transform is applied to
 * @param <V> the type of the viewer
 */
public final class TransformContext<T, U extends Pane, V extends InterfaceViewer> {

    private final InterfaceProperty<T> property;
    private final Transform<U, V> transform;
    private final int priority;

    private TransformContext(
            final @NonNull InterfaceProperty<T> property,
            final int priority,
            final @NonNull Transform<U, V> transform
    ) {
        this.property = property;
        this.priority = priority;
        this.transform = transform;
    }

    /**
     * Creates a new transform context instance
     *
     * @param property the property
     * @param priority the priority
     * @param transform the transform
     * @param <T> the type of the value this context is dependent on
     * @param <U> the type of the pane that this transform is applied to
     * @param <V> the type of the viewer
     * @return the context
     */
    public static <T, U extends Pane, V extends InterfaceViewer> @NonNull TransformContext<T, U, V> of(
            final @NonNull InterfaceProperty<T> property,
            final int priority,
            final @NonNull Transform<U, V> transform
    ) {
        return new TransformContext<>(property, priority, transform);
    }

    /**
     * Returns the interface property of this context
     *
     * @return the property
     */
    public @NonNull InterfaceProperty<T> property() {
        return this.property;
    }

    /**
     * Returns the transform of this context
     *
     * @return the transform
     */
    public @NonNull Transform<U, V> transform() {
        return this.transform;
    }

    /**
     * Returns the priority in which the transformation should be applied
     *
     * @return the priority
     */
    public int priority() {
        return this.priority;
    }

}
