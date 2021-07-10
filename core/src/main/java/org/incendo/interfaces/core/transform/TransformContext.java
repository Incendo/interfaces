package org.incendo.interfaces.core.transform;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.pane.Pane;

/**
 *
 * @param <T> the type of the value this context is dependent on
 * @param <U> the type of the pane that this transform is applied to
 */
public final class TransformContext<T, U extends Pane> {

    private final InterfaceProperty<T> property;
    private final Transform<U> transform;
    private final int priority;

    private TransformContext(
            final @NonNull InterfaceProperty<T> property,
            final int priority,
            final @NonNull Transform<U> transform
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
     * @return the context
     */
    public static <T, U extends Pane> @NonNull TransformContext<T, U> of(
            final @NonNull InterfaceProperty<T> property,
            final int priority,
            final @NonNull Transform<U> transform
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
    public @NonNull Transform<U> transform() {
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
