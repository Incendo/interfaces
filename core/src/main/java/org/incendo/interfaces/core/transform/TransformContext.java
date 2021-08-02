package org.incendo.interfaces.core.transform;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.view.InterfaceViewer;

import java.util.Arrays;
import java.util.Collection;

/**
 * @param <U> the type of the pane that this transform is applied to
 * @param <V> the type of the viewer
 */
public final class TransformContext<U extends Pane, V extends InterfaceViewer> {

    private final int priority;
    private final Transform<U, V> transform;
    private final Collection<InterfaceProperty<?>> properties;

    private TransformContext(
            final int priority,
            final @NonNull Transform<U, V> transform,
            final @NonNull InterfaceProperty<?>[] properties
    ) {
        this.properties = Arrays.asList(properties);
        this.priority = priority;
        this.transform = transform;
    }

    /**
     * Creates a new transform context instance
     *
     * @param priority the priority
     * @param transform the transform
     * @param properties the properties
     * @param <U> the type of the pane that this transform is applied to
     * @param <V> the type of the viewer
     * @return the context
     */
    public static <U extends Pane, V extends InterfaceViewer> @NonNull TransformContext<U, V> of(
            final int priority,
            final @NonNull Transform<U, V> transform,
            final @NonNull InterfaceProperty<?>... properties
    ) {
        return new TransformContext<>(priority, transform, properties);
    }

    /**
     * Returns the priority in which the transformation should be applied
     *
     * @return the priority
     */
    public int priority() {
        return this.priority;
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
     * Returns the interface property of this context
     *
     * @return the property
     */
    public @NonNull Collection<@NonNull  InterfaceProperty<?>> properties() {
        return this.properties;
    }

}
