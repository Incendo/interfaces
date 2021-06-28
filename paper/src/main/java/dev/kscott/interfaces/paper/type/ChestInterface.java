package dev.kscott.interfaces.paper.type;

import dev.kscott.interfaces.core.Interface;
import dev.kscott.interfaces.core.UpdatingInterface;
import dev.kscott.interfaces.core.arguments.HashMapInterfaceArgument;
import dev.kscott.interfaces.core.arguments.InterfaceArgument;
import dev.kscott.interfaces.core.transform.Transform;
import dev.kscott.interfaces.paper.PlayerViewer;
import dev.kscott.interfaces.paper.element.ClickHandler;
import dev.kscott.interfaces.paper.pane.ChestPane;
import dev.kscott.interfaces.paper.view.ChestView;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * An interface using a chest.
 */
public class ChestInterface implements
        Interface<ChestPane, PlayerViewer>,
        TitledInterface,
        UpdatingInterface,
        ClickableInterface {

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
    private final @NonNull Component title;
    /**
     * True if this is an updating interface, false if not.
     */
    private final boolean updates;
    /**
     * The amount of ticks between updates.
     */
    private final int updateDelay;
    /**
     * The click handler for the top part of the menu.
     */
    private final @NonNull ClickHandler clickHandler;

    /**
     * Constructs {@code ChestInterface}.
     *
     * @param rows the rows
     */
    public ChestInterface(
            final int rows,
            final @NonNull Component title,
            final @NonNull List<Transform<ChestPane>> transforms,
            final boolean updates,
            final int updateDelay,
            final @NonNull ClickHandler clickHandler
    ) {
        this.title = title;
        this.transformationList = transforms;
        this.updates = updates;
        this.updateDelay = updateDelay;
        this.rows = rows;
        this.clickHandler = clickHandler;
    }

    /**
     * Returns a new ChestInterface builder.
     *
     * @return the builder
     */
    public static @NonNull Builder builder() {
        return new ChestInterface.Builder();
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
     * Returns the click handler.
     *
     * @return the click handler
     */
    public @NonNull ClickHandler clickHandler() {
        return this.clickHandler;
    }

    /**
     * Adds a transformation to the list.
     *
     * @param transform the transformation
     * @return the interface
     */
    @Override
    public @NonNull ChestInterface transform(final @NonNull Transform<ChestPane> transform) {
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
    public @NonNull ChestView open(final @NonNull PlayerViewer viewer) {
        return this.open(viewer, HashMapInterfaceArgument.empty());
    }

    /**
     * Opens the interface to the viewer.
     *
     * @param viewer    the viewer
     * @param arguments the interface's arguments
     * @return the view
     */
    @Override
    public @NonNull ChestView open(
            final @NonNull PlayerViewer viewer,
            final @NonNull InterfaceArgument arguments
    ) {
        final @NonNull ChestView view = new ChestView(this, (PlayerViewer) viewer, arguments);

        view.open();

        return view;
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

    /**
     * Returns true if updating interface, false if not.
     *
     * @return true if updating interface, false if not
     */
    @Override
    public boolean updates() {
        return this.updates;
    }

    /**
     * Returns the update delay.
     *
     * @return the update delay
     */
    @Override
    public int updateDelay() {
        return this.updateDelay;
    }

    /**
     * A class that builds a chest interface.
     */
    public static class Builder implements Interface.Builder<ChestPane, PlayerViewer, ChestInterface> {

        /**
         * The list of transformations.
         */
        private final @NonNull List<Transform<ChestPane>> transformsList;

        /**
         * The amount of rows.
         */
        private int rows;

        /**
         * The title.
         */
        private @NonNull Component title;

        /**
         * True if updating interface, false if not.
         */
        private boolean updates;

        /**
         * How many ticks to wait between interface updates.
         */
        private int updateDelay;

        /**
         * The top click handler.
         */
        private @NonNull ClickHandler clickHandler;

        /**
         * Constructs {@code Builder}.
         */
        public Builder() {
            this.transformsList = new ArrayList<>();
            this.rows = 1;
            this.title = Component.empty();
            this.updates = false;
            this.updateDelay = 1;
            this.clickHandler = ClickHandler.cancel();
        }

        /**
         * Sets the number of rows for this interface.
         *
         * @param rows the number of rows
         * @return this
         */
        public @NonNull Builder rows(final int rows) {
            this.rows = rows;
            return this;
        }

        /**
         * Sets the title of the interface.
         *
         * @param title the title
         * @return this
         */
        public @NonNull Builder title(final @NonNull Component title) {
            this.title = title;
            return this;
        }

        /**
         * Adds a transformation to the interface.
         *
         * @param transform the transformation
         * @return this
         */
        @Override
        public @NonNull Builder addTransform(final @NonNull Transform<ChestPane> transform) {
            this.transformsList.add(transform);

            return this;
        }

        /**
         * Sets the click handler.
         *
         * @param handler the handler
         * @return this
         */
        public @NonNull Builder clickHandler(final @NonNull ClickHandler handler) {
            this.clickHandler = handler;
            return this;
        }

        /**
         * Controls how/if the interface updates.
         *
         * @param updates     true if the interface should update, false if not
         * @param updateDelay how many ticks to wait between updates
         * @return this
         */
        public @NonNull Builder updates(final boolean updates, final int updateDelay) {
            this.updates = updates;
            this.updateDelay = updateDelay;
            return this;
        }

        /**
         * Constructs and returns the interface.
         *
         * @return the interface
         */
        @Override
        public @NonNull ChestInterface build() {
            return new ChestInterface(
                    this.rows,
                    this.title,
                    this.transformsList,
                    this.updates,
                    this.updateDelay,
                    this.clickHandler
            );
        }

    }

}
