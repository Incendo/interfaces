package org.incendo.interfaces.paper.type;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.Interface;
import org.incendo.interfaces.core.arguments.HashMapInterfaceArguments;
import org.incendo.interfaces.core.arguments.InterfaceArguments;
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
 * An interface inside the Minecraft chat.
 */
@SuppressWarnings("unused")
public final class ChatInterface implements Interface<ChatPane, PlayerViewer> {

    private final @NonNull List<TransformContext<?, ChatPane, PlayerViewer>> transforms;

    /**
     * Constructs {@code ChatInterface}.
     *
     * @param transforms the list of transforms
     */
    public ChatInterface(final @NonNull List<TransformContext<?, ChatPane, PlayerViewer>> transforms) {
        this.transforms = transforms;
    }

    /**
     * Returns a new ChatInterface builder.
     *
     * @return a new builder
     */
    public static @NonNull Builder builder() {
        return new Builder();
    }

    @Override
    public @NonNull ChatInterface transform(@NonNull final Transform<ChatPane, PlayerViewer> transform) {
        this.transforms.add(
                TransformContext.of(
                        InterfaceProperty.dummy(),
                        1,
                        transform
                )
        );

        return this;
    }

    @Override
    public @NonNull List<TransformContext<?, ChatPane, PlayerViewer>> transformations() {
        return List.copyOf(this.transforms);
    }

    @Override
    public @NonNull InterfaceView<ChatPane, PlayerViewer> open(@NonNull final PlayerViewer viewer) {
        return this.open(viewer, HashMapInterfaceArguments.empty());
    }

    @Override
    public @NonNull ChatView open(
            @NonNull final InterfaceView<?, PlayerViewer> view,
            @NonNull final InterfaceArguments arguments
    ) {
        return this.open(view.viewer(), arguments);
    }

    @Override
    public @NonNull ChatView open(
            @NonNull final PlayerViewer viewer,
            @NonNull final InterfaceArguments arguments
    ) {
        final @NonNull ChatView chatView = new ChatView(this, viewer, arguments);

        chatView.open();

        return chatView;
    }

    /**
     * A class that builds a book interface.
     */
    public static final class Builder implements Interface.Builder<ChatPane, PlayerViewer, ChatInterface> {

        private final @NonNull List<@NonNull TransformContext<?, ChatPane, PlayerViewer>> transforms;

        /**
         * Constructs {@code Builder}.
         */
        public Builder() {
            this.transforms = new ArrayList<>();
        }

        private Builder(final @NonNull List<@NonNull TransformContext<?, ChatPane, PlayerViewer>> transforms) {
            this.transforms = Collections.unmodifiableList(transforms);
        }

        /**
         * Adds a transformation to the interface.
         *
         * @param transform the transformation
         * @return new builder instance
         */
        @Override
        public @NonNull Builder addTransform(final @NonNull Transform<ChatPane, PlayerViewer> transform) {
            return this.addTransform(InterfaceProperty.dummy(), 1, transform);
        }

        @Override
        public <S> @NonNull Builder addTransform(
                final @NonNull InterfaceProperty<S> property,
                final int priority,
                final @NonNull Transform<ChatPane, PlayerViewer> transform
        ) {
            final List<TransformContext<?, ChatPane, PlayerViewer>> transforms = new ArrayList<>(this.transforms);
            transforms.add(
                    TransformContext.of(
                            property,
                            priority,
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
