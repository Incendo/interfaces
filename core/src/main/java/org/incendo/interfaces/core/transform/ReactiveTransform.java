package org.incendo.interfaces.core.transform;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.view.InterfaceViewer;

/**
 * A reactive {@link Transform} that gets updated whenever the associated {@link #properties()} is updated.
 *
 * @param <T> the pane type
 * @param <U> the viewer type
 * @param <V> the property type
 */
public interface ReactiveTransform<T extends Pane, U extends InterfaceViewer, V> extends Transform<T, U> {

    /**
     * Returns the reactive property that this transform depends on.
     *
     * @return the property
     */
    @NonNull InterfaceProperty<?>[] properties();

}
