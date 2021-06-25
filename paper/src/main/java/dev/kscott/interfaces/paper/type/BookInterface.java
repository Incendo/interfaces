package dev.kscott.interfaces.paper.type;

import dev.kscott.interfaces.core.Interface;
import dev.kscott.interfaces.core.arguments.HashMapInterfaceArgument;
import dev.kscott.interfaces.core.arguments.InterfaceArgument;
import dev.kscott.interfaces.core.transform.Transform;
import dev.kscott.interfaces.core.view.InterfaceView;
import dev.kscott.interfaces.core.view.InterfaceViewer;
import dev.kscott.interfaces.paper.PlayerViewer;
import dev.kscott.interfaces.paper.pane.BookPane;
import dev.kscott.interfaces.paper.view.BookView;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * An interface using a book.
 */
public class BookInterface implements
        Interface<BookPane, PlayerViewer>,
        TitledInterface {

    /**
     * The list of transforms.
     */
    private final @NonNull List<Transform<BookPane>> transforms;

    /**
     * The title.
     */
    private @NonNull Component title;

    /**
     * Constructs {@code BookInterface}.
     */
    public BookInterface() {
        this.transforms = new ArrayList<>();
        this.title = title();
    }

    /**
     * Adds a transform to this interface.
     *
     * @param transform the transformation
     * @return the interface
     */
    @Override
    public @NonNull BookInterface transform(final @NonNull Transform<BookPane> transform) {
        this.transforms.add(transform);

        return this;
    }

    /**
     * Returns the list of transformations.
     *
     * @return the list of transformations
     */
    @Override
    public @NonNull List<Transform<BookPane>> transformations() {
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
        return this.open(viewer, HashMapInterfaceArgument.empty());
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
            final @NonNull InterfaceArgument arguments
    ) {
        final @NonNull BookView view = new BookView(this, (PlayerViewer) viewer, arguments);

        view.open();

        return view;
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

}
