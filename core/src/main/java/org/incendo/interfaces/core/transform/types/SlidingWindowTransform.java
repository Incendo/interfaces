package org.incendo.interfaces.core.transform.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.function.Function;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.interfaces.core.element.Element;
import org.incendo.interfaces.core.pane.GridPane;
import org.incendo.interfaces.core.transform.InterfaceProperty;
import org.incendo.interfaces.core.transform.ReactiveTransform;
import org.incendo.interfaces.core.util.Vector2;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.core.view.InterfaceViewer;

/**
 * A transform that shows elements in a sliding window.
 *
 * @param <S> the element type
 * @param <T> the pane type
 * @param <U> the viewer type
 */
public final class SlidingWindowTransform<S extends Element, T extends GridPane<T, S>, U extends InterfaceViewer>
        implements ReactiveTransform<T, U, Integer> {

    private final InterfaceProperty<Integer> offsetProperty = InterfaceProperty.of(Integer.MAX_VALUE / 2);

    private final Vector2 min;
    private final Vector2 max;
    private final Vector2 dim;

    private final Map<Integer, List<@NonNull S>> elements;

    private @NonNull Vector2 backwardElementPosition = Vector2.at(-1, -1);
    private @NonNull Vector2 forwardElementPosition = Vector2.at(-1, -1);

    private @NonNull Function<@NonNull SlidingWindowTransform<S, T, U>, @Nullable S> backwardElementBuilder = transform -> null;
    private @NonNull Function<@NonNull SlidingWindowTransform<S, T, U>, @Nullable S> forwardElementBuilder = transform -> null;

    /**
     * Constructs a new sliding window transform.
     *
     * @param min      the coordinates for the minimum (inclusive) point where the elements are rendered
     * @param max      the coordinates for the maximum (inclusive) point where the elements are rendered
     * @param elements the elements
     */
    public SlidingWindowTransform(
            final @NonNull Vector2 min,
            final @NonNull Vector2 max,
            final @NonNull List<@NonNull S> elements
    ) {
        this.min = min;
        this.max = max;
        this.dim = Vector2.at(max.x() - min.x() + 1, max.y() - min.y() + 1);
        // Map the elements to their columns.
        this.elements = new HashMap<>();
        final int columns = (int) Math.round((double) elements.size() / (double) this.dim.y());
        int index = 0;
        for (int column = 0; column < columns; column++) {
            final List<S> row = new ArrayList<>();
            for (int i = 0; i < this.dim.y(); i++) {
                final int elementIndex = index++ % elements.size();
                row.add(elements.get(elementIndex));
            }
            this.elements.put(column, row);
        }
    }

    @Override
    public @NonNull InterfaceProperty<?>[] properties() {
        return new InterfaceProperty[]{this.offsetProperty};
    }

    @Override
    @SuppressWarnings("ALL")
    public T apply(
            final T originalPane,
            final InterfaceView<T, U> view
    ) {
        // Pane that we're updating.
        T pane = originalPane;
        // Calculate the columns that we are displaying.
        final int columns = this.elements.size();
        final int startColumn = this.offsetProperty.get() % columns;
        // Render the columns.
        for (int i = 0; i < this.dim.x(); i++) {
            final int column = (this.offsetProperty.get() + i) % columns;
            final List<S> elements = this.elements.get(column);

            final int x = i + this.min.x();
            for (int j = 0; j < this.dim.y(); j++) {
                final int y = this.min.y() + j;

                pane = pane.element(elements.get(j), x, y);
            }
        }
        // Add the backward element, if one should exist.
        final @Nullable S backwardElement = this.backwardElementBuilder.apply(this);
        if (backwardElement != null) {
            pane = pane.element(backwardElement, this.backwardElementPosition.x(), this.backwardElementPosition.y());
        }
        // Add the forward element, if one should exist.
        final @Nullable S forwardElement = this.forwardElementBuilder.apply(this);
        if (forwardElement != null) {
            pane = pane.element(forwardElement, this.forwardElementPosition.x(), this.forwardElementPosition.y());
        }
        // Return the updated pane.
        return pane;
    }

    /**
     * Sets the backward element.
     *
     * @param position the position of the element
     * @param builder  the builder that builds the element
     */
    public void backwardElement(
            final @NonNull Vector2 position,
            final @NonNull Function<@NonNull SlidingWindowTransform<S, T, U>, @Nullable S> builder
    ) {
        this.backwardElementPosition = position;
        this.backwardElementBuilder = builder;
    }

    /**
     * Sets the forward element.
     *
     * @param position the position of the element
     * @param builder  the builder that builds the element
     */
    public void forwardElement(
            final @NonNull Vector2 position,
            final @NonNull Function<@NonNull SlidingWindowTransform<S, T, U>, @Nullable S> builder
    ) {
        this.forwardElementPosition = position;
        this.forwardElementBuilder = builder;
    }

    /**
     * Slide the window one backward one step.
     */
    public void slideBack() {
        if (this.offsetProperty.get() == 0) {
            this.offsetProperty.set(Integer.MAX_VALUE);
        } else {
            this.offsetProperty.set(this.offsetProperty.get() - 1);
        }
    }

    /**
     * Slide the window view forward one step.
     */
    public void slideForward() {
        if (this.offsetProperty.get() == Integer.MAX_VALUE) {
            this.offsetProperty.set(0);
        } else {
            this.offsetProperty.set(this.offsetProperty.get() + 1);
        }
    }

}
