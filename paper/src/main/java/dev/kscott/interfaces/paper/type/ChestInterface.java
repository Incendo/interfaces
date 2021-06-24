package dev.kscott.interfaces.paper.type;

import dev.kscott.interfaces.core.Interface;
import dev.kscott.interfaces.core.arguments.InterfaceArgument;
import dev.kscott.interfaces.core.transform.Transform;
import dev.kscott.interfaces.core.view.InterfaceView;
import dev.kscott.interfaces.core.view.InterfaceViewer;
import dev.kscott.interfaces.paper.pane.ChestPane;
import dev.kscott.interfaces.paper.view.ChestView;
import dev.kscott.interfaces.paper.view.PlayerViewer;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * An interface using a chest.
 */
public class ChestInterface implements Interface<ChestPane>, Titled {

    /**
     * The interface's rows.
     */
    private final int rows;

    /**
     * The list of transformations.
     */
    private final @NonNull List<Transform<ChestPane>> transformationList;

    /**
     * The title.
     */
    private @NonNull Component title;

    /**
     * Constructs {@code ChestInterface}.
     *
     * @param rows the rows
     */
    public ChestInterface(final int rows) {
        this.title = Component.empty();
        this.transformationList = new ArrayList<>();
        this.rows = rows;
    }

    /**
     * Returns the amount of rows.
     *
     * @return the rows
     */
    public int rows() {
        return this.rows;
    }

    /**
     * Adds a transformation to the list.
     *
     * @param transform the transformation
     * @return the transformation
     */
    @Override
    public @NonNull Interface<ChestPane> transform(final @NonNull Transform<ChestPane> transform) {
        this.transformationList.add(transform);
        return this;
    }

    /**
     * Returns the list of transformations.
     *
     * @return the transformations
     */
    @Override
    public @NonNull List<Transform<ChestPane>> transformations() {
        return List.copyOf(this.transformationList);
    }

    /**
     * Opens the interface to the viewer.
     *
     * @param viewer the viewer
     * @return the view
     */
    @Override
    public @NonNull InterfaceView open(final @NonNull InterfaceViewer viewer) {
        return this.open(viewer, InterfaceArgument.empty());
    }

    /**
     * Opens the interface to the viewer.
     *
     * @param viewer    the viewer
     * @param arguments the interface's arguments
     * @return the view
     */
    @Override
    public @NonNull InterfaceView open(final @NonNull InterfaceViewer viewer, final @NonNull InterfaceArgument arguments) {
        if (!(viewer instanceof PlayerViewer)) {
            throw new UnsupportedOperationException("This interface only supports the PlayerViewer class.");
        }

        final @NonNull ChestView view = new ChestView(this, (PlayerViewer) viewer, arguments);

        view.open();

        return view;
    }

    /**
     * Returns the title of this interface.
     *
     * @param title the title
     */
    @Override
    public void title(@NonNull final Component title) {
        this.title = title;
    }

    /**
     * Sets the title of this interface.
     *
     * @return the title
     */
    @Override
    public @NonNull Component title() {
        return this.title;
    }

}
