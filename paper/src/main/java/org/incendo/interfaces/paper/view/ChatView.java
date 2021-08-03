package org.incendo.interfaces.paper.view;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.arguments.InterfaceArguments;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.pane.ChatPane;
import org.incendo.interfaces.paper.type.ChatInterface;

import java.util.UUID;

/**
 * The view of a Book-based interface.
 */
public final class ChatView implements TextView<ChatPane> {

    private final @NonNull UUID uuid;
    private final @NonNull ChatInterface parent;
    private final @NonNull PlayerViewer viewer;
    private final @NonNull InterfaceArguments argument;
    private final @NonNull ChatPane pane;

    /**
     * Constructs {@code BookView}.
     *
     * @param parent   the parent
     * @param viewer   the viewer
     * @param argument the argument
     */
    public ChatView(
            final @NonNull ChatInterface parent,
            final @NonNull PlayerViewer viewer,
            final @NonNull InterfaceArguments argument
    ) {
        this.parent = parent;
        this.viewer = viewer;
        this.argument = argument;
        this.uuid = UUID.randomUUID();

        @NonNull ChatPane pane = new ChatPane();

        for (final var transform : this.parent.transformations()) {
            pane = transform.transform().apply(pane, this);
        }

        this.pane = pane;
    }

    @Override
    public boolean viewing() {
        // Not sure how to properly implement this, or even if it can be done reliably. Editing a book sends an event,
        // but just closing the book with the ESC key doesn't.
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
    public @NonNull InterfaceArguments arguments() {
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

    @Override
    public @NonNull UUID uuid() {
        return this.uuid;
    }

}
