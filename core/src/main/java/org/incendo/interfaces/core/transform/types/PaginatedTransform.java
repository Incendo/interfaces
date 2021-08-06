package org.incendo.interfaces.core.transform.types;

import java.util.List;

import java.util.function.Function;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.value.qual.IntRange;
import org.incendo.interfaces.core.element.Element;
import org.incendo.interfaces.core.pane.GridPane;
import org.incendo.interfaces.core.transform.InterfaceProperty;
import org.incendo.interfaces.core.transform.ReactiveTransform;
import org.incendo.interfaces.core.util.Vector2;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.core.view.InterfaceViewer;

/**
 * A transform that shows elements on different "pages".
 *
 * @param <S> the element type
 * @param <T> the pane type
 * @param <U> the viewer type
 */
public final class PaginatedTransform<S extends Element, T extends GridPane<T, S>, U extends InterfaceViewer>
        implements ReactiveTransform<T, U, Integer> {

    private final InterfaceProperty<Integer> pageProperty = InterfaceProperty.of(0);

    private final Vector2 min;
    private final Vector2 max;
    private final Vector2 dim;

    private final int pages;

    private final List<@NonNull S> elements;

    private @NonNull Vector2 backwardElementPosition = Vector2.at(-1, -1);
    private @NonNull Vector2 forwardElementPosition = Vector2.at(-1, -1);

    private @NonNull Function<@NonNull PaginatedTransform<S, T, U>, @Nullable S> backwardElementBuilder = transform -> null;
    private @NonNull Function<@NonNull PaginatedTransform<S, T, U>, @Nullable S> forwardElementBuilder = transform -> null;

    /**
     * Constructs a new paginated transform.
     *
     * @param min      the coordinates for the minimum (inclusive) point where the elements are rendered
     * @param max      the coordinates for the maximum (inclusive) point where the elements are rendered
     * @param elements the elements
     */
    public PaginatedTransform(
            final @NonNull Vector2 min,
            final @NonNull Vector2 max,
            final @NonNull List<@NonNull S> elements
    ) {
        this.min = min;
        this.max = max;
        this.dim = Vector2.at(max.x() - min.x() + 1, max.y() - min.y() + 1);
        this.elements = elements;
        // Calculate the number of available slots.
        final int slots = this.dim.x() * this.dim.y();
        // Calculate the number of elements.
        final int numberOfElements = elements.size();
        // Calculate the number of pages occupied by the elements.
        this.pages = (int) Math.ceil((double) numberOfElements / (double) slots);
    }

    @Override
    public @NonNull InterfaceProperty<?>[] properties() {
        return new InterfaceProperty[]{this.pageProperty};
    }

    @Override
    @SuppressWarnings("ALL")
    public T apply(
            final T originalPane,
            final InterfaceView<T, U> view
    ) {
        if (this.page() < 0 || this.page() > this.maxPage()) {
            throw new IllegalStateException(
                    String.format(
                            "Page number is out of bounds. Must be in the range [%d, %d].",
                            0,
                            this.maxPage()
                    )
            );
        }
        // Pane that we're updating.
        T pane = originalPane;
        // Calculate the number of available slots.
        final int slots = this.dim.x() * this.dim.y();
        // Calculate the offset.
        final int offset = slots * this.page();
        // Calculate the page elements.
        final List<S> elements = this.elements.subList(offset, this.elements.size());
        // Index used to reference the elements.
        int elementIndex = 0;
        // Render the elements.
        for (int y = this.min.y(); y <= this.max.y(); y++) {
            for (int x = this.min.x(); x <= this.max.x() && elementIndex < elements.size(); x++) {
                 pane = pane.element(elements.get(elementIndex), x, y);
                 // Increment the element index.
                elementIndex++;
            }
        }
        // Add the backward element, if one should exist.
        if (this.page() > 0) {
            final @Nullable S backwardElement = this.backwardElementBuilder.apply(this);
            if (backwardElement != null) {
                pane = pane.element(backwardElement, this.backwardElementPosition.x(), this.backwardElementPosition.y());
            }
        }
        // Add the forward element, if one should exist.
        if (this.page() < this.maxPage()) {
            final @Nullable S forwardElement = this.forwardElementBuilder.apply(this);
            if (forwardElement != null) {
                pane = pane.element(forwardElement, this.forwardElementPosition.x(), this.forwardElementPosition.y());
            }
        }
        // Return the updated pane.
        return pane;
    }

    /**
     * Returns the current page number.
     *
     * @return the current page (0-indexed)
     */
    public @IntRange(from = 0) int page() {
        return this.pageProperty.get();
    }

    /**
     * Returns the maximum page number.
     *
     * @return the maximum page (0-indexed)
     */
    public @IntRange(from = 0) int maxPage() {
        return Math.max(this.pages - 1, 0);
    }

    /**
     * Returns the number of pages.
     *
     * @return the number of pages
     */
    public @IntRange(from = 1) int pages() {
        return this.pages;
    }

    /**
     * Returns the dimensions of the paginated view
     *
     * @return dimensions of the paginated view
     */
    public @NonNull Vector2 dimensions() {
        return this.dim;
    }

    /**
     * Sets the backward element.
     *
     * @param position the position of the element
     * @param builder  the builder that builds the element
     */
    public void backwardElement(
            final @NonNull Vector2 position,
            final @NonNull Function<@NonNull PaginatedTransform<S, T, U>, @Nullable S> builder
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
            final @NonNull Function<@NonNull PaginatedTransform<S, T, U>, @Nullable S> builder
    ) {
        this.forwardElementPosition = position;
        this.forwardElementBuilder = builder;
    }

    /**
     * Switch to the previous page.
     *
     * @throws IllegalStateException if the previous page does not exist
     */
    public void previousPage() {
        if (this.page() == 0) {
            throw new IllegalStateException(
                    String.format(
                            "Page number is out of bounds. Must be in the range [%d, %d].",
                            0,
                            this.maxPage()
                    )
            );
        }
        this.pageProperty.set(this.page() - 1);
    }

    /**
     * Switch to the next page.
     *
     * @throws IllegalStateException if the previous next does not exist
     */
    public void nextPage() {
        if (this.page() == this.maxPage()) {
            throw new IllegalStateException(
                    String.format(
                            "Page number is out of bounds. Must be in the range [%d, %d].",
                            0,
                            this.maxPage()
                    )
            );
        }
        this.pageProperty.set(this.page() + 1);
    }

}
