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

}
