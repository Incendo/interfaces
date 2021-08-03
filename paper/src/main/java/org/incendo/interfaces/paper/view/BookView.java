package org.incendo.interfaces.paper.view;

import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.arguments.InterfaceArguments;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.text.TextElement;
import org.incendo.interfaces.paper.pane.BookPane;
import org.incendo.interfaces.paper.type.BookInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The view of a Book-based interface.
 */
public final class BookView implements TextView<BookPane> {

    private final @NonNull UUID uuid;
    private final @NonNull BookInterface parent;
    private final @NonNull PlayerViewer viewer;
    private final @NonNull InterfaceArguments argument;
    private final @NonNull BookPane pane;
    private final @NonNull Book book;

    /**
     * Constructs {@code BookView}.
     *
     * @param parent   the parent
     * @param viewer   the viewer
     * @param argument the argument
     * @param title    the title
     */
    public BookView(
            final @NonNull BookInterface parent,
            final @NonNull PlayerViewer viewer,
            final @NonNull InterfaceArguments argument,
            final @NonNull Component title
    ) {
        this.parent = parent;
        this.viewer = viewer;
        this.argument = argument;

        @NonNull BookPane pane = new BookPane();

        for (final var transform : this.parent.transformations()) {
            pane = transform.transform().apply(pane, this);
        }

        this.pane = pane;

        final @NonNull List<Component> pages = new ArrayList<>();

        for (final @NonNull TextElement element : this.pane.textElements()) {
            pages.add(element.text());
        }

        this.book = Book.book(title, Component.empty(), pages);

        this.uuid = UUID.randomUUID();
    }

    @Override
    public boolean viewing() {
        // Not sure how to properly implement this, or even if it can be done reliably. Editing a book sends an event,
        // but just closing the book with the ESC key doesn't.
        return false;
    }

    @Override
    public @NonNull BookInterface backing() {
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
    public @NonNull BookPane pane() {
        return this.pane;
    }

    @Override
    public @NonNull UUID uuid() {
        return this.uuid;
    }

    /**
     * Returns the book.
     *
     * @return the book
     */
    public @NonNull Book book() {
        return this.book;
    }

}
