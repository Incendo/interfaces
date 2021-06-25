package dev.kscott.interfaces.paper.view;

import dev.kscott.interfaces.core.Interface;
import dev.kscott.interfaces.core.arguments.InterfaceArgument;
import dev.kscott.interfaces.core.view.InterfaceView;
import dev.kscott.interfaces.paper.PlayerViewer;
import dev.kscott.interfaces.paper.element.TextElement;
import dev.kscott.interfaces.paper.pane.BookPane;
import dev.kscott.interfaces.paper.type.BookInterface;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * The view of a Book-based interface.
 */
public class BookView implements InterfaceView<BookPane, PlayerViewer, Interface<BookPane, PlayerViewer>> {

    /**
     * The parent interface.
     */
    private final @NonNull BookInterface parent;

    /**
     * The viewer.
     */
    private final @NonNull PlayerViewer viewer;

    /**
     * The argument.
     */
    private final @NonNull InterfaceArgument argument;

    /**
     * The pane.
     */
    private final @NonNull BookPane pane;

    /**
     * The book.
     */
    private final @NonNull Book book;

    /**
     * Constructs {@code BookView}.
     *
     * @param parent   the parent
     * @param viewer   the viewer
     * @param argument the argument
     */
    public BookView(
            final @NonNull BookInterface parent,
            final @NonNull PlayerViewer viewer,
            final @NonNull InterfaceArgument argument
    ) {
        this.parent = parent;
        this.viewer = viewer;
        this.argument = argument;

        @NonNull BookPane pane = new BookPane();

        for (final var transform : this.parent.transformations()) {
            pane = transform.apply(pane, this);
        }

        this.pane = pane;

        final @NonNull List<Component> pages = new ArrayList<>();

        for (final @NonNull TextElement element : this.pane.pages()) {
            pages.add(element.text());
        }

        this.book = Book.book(Component.empty(), Component.empty(), pages);
    }

    /**
     * Always returns false.
     *
     * @return false
     */
    @Override
    public boolean viewing() {
        // Not sure how to properly implement this, or even if it can be done reliably. Editing a book sends an event,
        // but just closing the book with the ESC key doesn't.
        return false;
    }

    /**
     * Returns the parent.
     *
     * @return the parent
     */
    @Override
    public @NonNull BookInterface parent() {
        return this.parent;
    }

    /**
     * Returns the viewer.
     *
     * @return the viewer
     */
    @Override
    public @NonNull PlayerViewer viewer() {
        return this.viewer;
    }

    /**
     * Returns the argument.
     *
     * @return the argument
     */
    @Override
    public @NonNull InterfaceArgument argument() {
        return this.argument;
    }

    /**
     * Opens the view to the viewer.
     */
    @Override
    public void open() {
        this.viewer.open(this);
    }

    /**
     * Returns the pane.
     *
     * @return the pane
     */
    @Override
    public @NonNull BookPane pane() {
        return this.pane;
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
