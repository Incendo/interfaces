package org.incendo.interfaces.paper.view;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.arguments.InterfaceArgument;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.pane.ChatPane;
import org.incendo.interfaces.paper.type.ChatInterface;

import java.util.List;

/**
 * The view of a Chat-based interface.
 */
public final class ChatView implements InterfaceView<ChatPane, PlayerViewer> {

    private final @NonNull ChatInterface parent;
    private final @NonNull PlayerViewer viewer;
    private final @NonNull InterfaceArgument argument;
    private final @NonNull ChatPane pane;

    /**
     * Constructs {@code ChatView}.
     *
     * @param parent   the parent
     * @param viewer   the viewer
     * @param argument the argument
     */
    public ChatView(
            final @NonNull ChatInterface parent,
            final @NonNull PlayerViewer viewer,
            final @NonNull InterfaceArgument argument
    ) {
        this.parent = parent;
        this.viewer = viewer;
        this.argument = argument;

        @NonNull ChatPane pane = new ChatPane(List.of());

        for (final var transform : this.parent.transformations()) {
            pane = transform.transform().apply(pane, this);
        }

        this.pane = pane;
    }

    @Override
    public boolean viewing() {
        return false;
    }

    @Override
    public @NonNull ChatInterface backing() {
        return this.parent;
    }

    @Override
    public @NonNull PlayerViewer viewer() {
        return this.viewer;
    }

    @Override
    public @NonNull InterfaceArgument argument() {
        return this.argument;
    }

    @Override
    public void open() {
        this.viewer.open(this);
    }

    @Override
    public @NonNull ChatPane pane() {
        return this.pane;
    }

}
