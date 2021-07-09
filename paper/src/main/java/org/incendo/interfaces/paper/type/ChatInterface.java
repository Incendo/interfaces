package org.incendo.interfaces.paper.type;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.Interface;
import org.incendo.interfaces.core.arguments.HashMapInterfaceArgument;
import org.incendo.interfaces.core.arguments.InterfaceArgument;
import org.incendo.interfaces.core.transform.InterfaceProperty;
import org.incendo.interfaces.core.transform.Transform;
import org.incendo.interfaces.core.transform.TransformContext;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.pane.ChatPane;
import org.incendo.interfaces.paper.view.ChatView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An interface using Minecraft chat.
 */
public final class ChatInterface implements Interface<ChatPane, PlayerViewer> {

    private final @NonNull List<TransformContext<?, ChatPane>> transforms;

    /**
     * Constructs {@code ChatInterface}.
     */
    public ChatInterface() {
        this(new ArrayList<>());
    }

    /**
     * Constructs {@code ChatInterface}
     *
     * @param transforms the transforms
     */
    public ChatInterface(final @NonNull List<TransformContext<?, ChatPane>> transforms) {
        this.transforms = transforms;
    }

    /**
     * Returns a new {@code ChatInterface.Builder}.
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
    public @NonNull ChatInterface transform(final @NonNull Transform<ChatPane> transform) {
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
    public @NonNull List<TransformContext<?, ChatPane>> transformations() {
        return List.copyOf(this.transforms);
    }

    @Override
    public @NonNull ChatView open(final @NonNull PlayerViewer viewer) {
        return this.open(viewer, HashMapInterfaceArgument.empty());
    }

    @Override
    public @NonNull ChatView open(
            @NonNull final PlayerViewer viewer,
            @NonNull final InterfaceArgument arguments
    ) {
        final @NonNull ChatView view = new ChatView(this, viewer, arguments);

        view.open();
        return view;
    }

    @Override
    public @NonNull ChatView open(
            @NonNull final InterfaceView<?, PlayerViewer> view,
            @NonNull final InterfaceArgument arguments
    ) {
        return this.open(view.viewer(), arguments);
    }

    /**
     * A class that builds a chat interface.
     */
    public static final class Builder implements Interface.Builder<ChatPane, PlayerViewer, ChatInterface> {

        private final @NonNull List<@NonNull TransformContext<?, ChatPane>> transforms;

        /**
         * Constructs {@code Builder}.
         */
        public Builder() {
            this.transforms = new ArrayList<>();
        }

        private Builder(
                final @NonNull List<@NonNull TransformContext<?, ChatPane>> transforms
        ) {
            this.transforms = Collections.unmodifiableList(transforms);
        }

        /**
         * Adds a transformation to the interface.
         *
         * @param transform the transformation
         * @return new builder instance
         */
        @Override
        public @NonNull Builder addTransform(final @NonNull Transform<ChatPane> transform) {
            return this.addTransform(InterfaceProperty.dummy(), transform);
        }

        @Override
        public <S> Builder addTransform(
                @NonNull final InterfaceProperty<S> property,
                @NonNull final Transform<ChatPane> transform
        ) {
            final List<TransformContext<?, ChatPane>> transforms = new ArrayList<>(this.transforms);
            transforms.add(
                    TransformContext.of(
                            property,
                            transform
                    )
            );

            return new Builder(transforms);
        }

        /**
         * Constructs and returns the interface.
         *
         * @return the interface
         */
        @Override
        public @NonNull ChatInterface build() {
            return new ChatInterface(this.transforms);
        }

    }

}
