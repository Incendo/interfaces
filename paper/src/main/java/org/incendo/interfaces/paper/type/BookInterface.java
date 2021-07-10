package org.incendo.interfaces.paper.type;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.Interface;
import org.incendo.interfaces.core.arguments.HashMapInterfaceArguments;
import org.incendo.interfaces.core.arguments.InterfaceArguments;
import org.incendo.interfaces.core.transform.InterfaceProperty;
import org.incendo.interfaces.core.transform.Transform;
import org.incendo.interfaces.core.transform.TransformContext;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.pane.BookPane;
import org.incendo.interfaces.paper.view.BookView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An interface using a book.
 */
public final class BookInterface implements TitledInterface<BookPane, PlayerViewer> {

    private final @NonNull List<TransformContext<?, BookPane>> transforms;
    private final @NonNull Component title;

    /**
     * Constructs {@code BookInterface}.
     */
    public BookInterface() {
        this(new ArrayList<>());
    }

    /**
     * Constructs {@code BookInterface}
     *
     * @param transforms the transforms
     */
    public BookInterface(final @NonNull List<TransformContext<?, BookPane>> transforms) {
        this(transforms, Component.empty());
    }

    /**
     * Constructs {@code BookInterface}
     *
     * @param transforms the transforms
     * @param title      the title
     */
    public BookInterface(
            final @NonNull List<TransformContext<?, BookPane>> transforms,
            final @NonNull Component title
    ) {
        this.transforms = transforms;
        this.title = title;
    }

    /**
     * Returns a new {@code BookInterface} builder.
     *
     * @return the builder
     */
    public static @NonNull Builder builder() {
        return new Builder();
    }

    /**
     * Adds a transform to this interface.
     *
     * @param transform the transformation
     * @return the interface
     */
    @Override
    public @NonNull BookInterface transform(final @NonNull Transform<BookPane> transform) {
        this.transforms.add(
                TransformContext.of(
                        InterfaceProperty.dummy(),
                        transform
                )
        );

        return this;
    }

    /**
     * Returns the list of transformations.
     *
     * @return the list of transformations
     */
    @Override
    public @NonNull List<TransformContext<?, BookPane>> transformations() {
        return List.copyOf(this.transforms);
    }

    /**
     * Opens this interface for a viewer.
     *
     * @param viewer the viewer
     * @return the view
     */
    @Override
    public @NonNull BookView open(final @NonNull PlayerViewer viewer) {
        return this.open(viewer, HashMapInterfaceArguments.empty());
    }

    /**
     * Opens this interface for a viewer.
     *
     * @param viewer    the viewer
     * @param arguments the interface's arguments
     * @return the view
     */
    @Override
    public @NonNull BookView open(
            final @NonNull PlayerViewer viewer,
            final @NonNull InterfaceArguments arguments
    ) {
        return this.open(viewer, arguments, this.title);
    }

    @Override
    public @NonNull BookView open(
            @NonNull final PlayerViewer viewer,
            @NonNull final Component title
    ) {
        return this.open(viewer, HashMapInterfaceArguments.empty(), title);
    }

    @Override
    public @NonNull BookView open(
            @NonNull final PlayerViewer viewer,
            @NonNull final InterfaceArguments arguments,
            @NonNull final Component title
    ) {
        final @NonNull BookView view = new BookView(this, viewer, arguments, title);

        view.open();

        return view;
    }

    @Override
    public @NonNull BookView open(
            @NonNull final InterfaceView<?, PlayerViewer> view,
            @NonNull final InterfaceArguments arguments
    ) {
        return this.open(view.viewer(), arguments);
    }

    /**
     * Returns the title of this interface.
     *
     * @return the title
     */
    @Override
    public @NonNull Component title() {
        return this.title;
    }

    /**
     * A class that builds a book interface.
     */
    public static final class Builder implements Interface.Builder<BookPane, PlayerViewer, BookInterface> {

        private final @NonNull List<@NonNull TransformContext<?, BookPane>> transforms;
        private final @NonNull Component title;

        /**
         * Constructs {@code Builder}.
         */
        public Builder() {
            this.transforms = new ArrayList<>();
            this.title = Component.empty();
        }

        private Builder(
                final @NonNull List<@NonNull TransformContext<?, BookPane>> transforms,
                final @NonNull Component title
        ) {
            this.transforms = Collections.unmodifiableList(transforms);
            this.title = title;
        }

        /**
         * Adds a transformation to the interface.
         *
         * @param transform the transformation
         * @return new builder instance
         */
        @Override
        public @NonNull Builder addTransform(final @NonNull Transform<BookPane> transform) {
            return this.addTransform(InterfaceProperty.dummy(), transform);
        }

        @Override
        public <S> Builder addTransform(
                @NonNull final InterfaceProperty<S> property,
                @NonNull final Transform<BookPane> transform
        ) {
            final List<TransformContext<?, BookPane>> transforms = new ArrayList<>(this.transforms);
            transforms.add(
                    TransformContext.of(
                            property,
                            transform
                    )
            );

            return new Builder(transforms, this.title);
        }

        /**
         * Returns the title of the interface.
         *
         * @return the title
         */
        public @NonNull Component title() {
            return this.title;
        }

        /**
         * Sets the title of the interface.
         *
         * @param title the title
         * @return new builder instance
         */
        public @NonNull Builder title(final @NonNull Component title) {
            return new Builder(this.transforms, title);
        }

        /**
         * Constructs and returns the interface.
         *
         * @return the interface
         */
        @Override
        public @NonNull BookInterface build() {
            return new BookInterface(this.transforms, this.title);
        }

    }

}
