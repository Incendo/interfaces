package org.incendo.interfaces.paper.type;

import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.Interface;
import org.incendo.interfaces.core.UpdatingInterface;
import org.incendo.interfaces.core.arguments.HashMapInterfaceArguments;
import org.incendo.interfaces.core.arguments.InterfaceArguments;
import org.incendo.interfaces.core.click.ClickHandler;
import org.incendo.interfaces.core.transform.InterfaceProperty;
import org.incendo.interfaces.core.transform.Transform;
import org.incendo.interfaces.core.transform.TransformContext;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.click.InventoryClickContext;
import org.incendo.interfaces.paper.pane.CombinedPane;
import org.incendo.interfaces.paper.view.CombinedView;
import org.incendo.interfaces.paper.view.PlayerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An interface using a chest.
 */
public final class CombinedInterface implements
        TitledInterface<CombinedPane, PlayerViewer>,
        UpdatingInterface,
        Clickable<CombinedPane, InventoryClickEvent, PlayerViewer> {

    private final int rows;
    private final @NonNull List<TransformContext<CombinedPane, PlayerViewer>> transformationList;
    private final @NonNull List<CloseHandler<CombinedPane>> closeHandlerList;
    private final @NonNull Component title;
    private final boolean updates;
    private final int updateDelay;
    private final @NonNull ClickHandler<CombinedPane, InventoryClickEvent, PlayerViewer, InventoryClickContext<CombinedPane,
            CombinedView>> clickHandler;

    /**
     * Constructs {@code ChestInterface}.
     *
     * @param chestRows          the rows
     * @param title         the interfaces title
     * @param transforms    the transformations to apply
     * @param closeHandlers the close handlers to apply
     * @param updates       {@code true} if the interface is an updating interface
     * @param updateDelay   the update delay
     * @param clickHandler  the handler to run on click
     */
    public CombinedInterface(
            final int chestRows,
            final @NonNull Component title,
            final @NonNull List<TransformContext<CombinedPane, PlayerViewer>> transforms,
            final @NonNull List<CloseHandler<CombinedPane>> closeHandlers,
            final boolean updates,
            final int updateDelay,
            final @NonNull ClickHandler<CombinedPane, InventoryClickEvent, PlayerViewer, InventoryClickContext<CombinedPane,
                    CombinedView>> clickHandler
    ) {
        this.title = title;
        this.transformationList = transforms;
        this.closeHandlerList = closeHandlers;
        this.updates = updates;
        this.updateDelay = updateDelay;
        this.rows = chestRows + CombinedPane.PLAYER_INVENTORY_ROWS;
        this.clickHandler = clickHandler;
    }

    /**
     * Returns a new ChestInterface builder.
     *
     * @return the builder
     */
    public static @NonNull Builder builder() {
        return new CombinedInterface.Builder();
    }

    /**
     * Returns the amount of rows.
     *
     * @return the rows
     */
    public int totalRows() {
        return this.rows;
    }

    /**
     * Returns the amount of rows.
     *
     * @return the rows
     */
    public int chestRows() {
        return this.rows - CombinedPane.PLAYER_INVENTORY_ROWS;
    }

    @Override
    public @NonNull ClickHandler<CombinedPane, InventoryClickEvent, PlayerViewer,
            InventoryClickContext<CombinedPane, CombinedView>> clickHandler() {
        return this.clickHandler;
    }

    @Override
    public @NonNull CombinedInterface transform(final @NonNull Transform<CombinedPane, PlayerViewer> transform) {
        this.transformationList.add(
                TransformContext.of(
                        1,
                        transform
                )
        );
        return this;
    }

    @Override
    public @NonNull List<TransformContext<CombinedPane, PlayerViewer>> transformations() {
        return List.copyOf(this.transformationList);
    }

    /**
     * Returns the list of close handlers.
     *
     * @return the close handlers
     */
    public @NonNull List<CloseHandler<CombinedPane>> closeHandlers() {
        return List.copyOf(this.closeHandlerList);
    }

    @Override
    public @NonNull CombinedView open(final @NonNull PlayerViewer viewer) {
        return this.open(viewer, HashMapInterfaceArguments.empty());
    }

    @Override
    public @NonNull CombinedView open(
            final @NonNull PlayerViewer viewer,
            final @NonNull InterfaceArguments arguments
    ) {
        return this.open(viewer, arguments, this.title);
    }

    @Override
    public @NonNull CombinedView open(
            final @NonNull PlayerViewer viewer,
            final @NonNull Component title
    ) {
        return this.open(viewer, HashMapInterfaceArguments.empty(), title);
    }

    @Override
    public @NonNull CombinedView open(
            final @NonNull PlayerViewer viewer,
            final @NonNull InterfaceArguments arguments,
            final @NonNull Component title
    ) {
        final @NonNull CombinedView view = new CombinedView(this, viewer, arguments, title);

        view.open();

        return view;
    }

    @Override
    public @NonNull CombinedView open(
            final @NonNull InterfaceView<?, PlayerViewer> parent,
            final @NonNull InterfaceArguments arguments
    ) {
        final @NonNull CombinedView view = new CombinedView((PlayerView<?>) parent, this, parent.viewer(), arguments, this.title);

        view.open();

        return view;
    }


    /**
     * Sets the title of the interface.
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
    public static final class Builder implements Interface.Builder<CombinedPane, PlayerViewer, CombinedInterface> {

        /**
         * The list of transformations.
         */
        private final @NonNull List<@NonNull TransformContext<CombinedPane, PlayerViewer>> transformsList;

        /**
         * The list of close handlers.
         */
        private final @NonNull List<@NonNull CloseHandler<CombinedPane>> closeHandlerList;

        /**
         * The amount of rows.
         */
        private final int chestRows;

        /**
         * The title.
         */
        private final @NonNull Component title;

        /**
         * True if updating interface, false if not.
         */
        private final boolean updates;

        /**
         * How many ticks to wait between interface updates.
         */
        private final int updateDelay;

        /**
         * The top click handler.
         */
        private final @NonNull ClickHandler<CombinedPane, InventoryClickEvent, PlayerViewer, InventoryClickContext<CombinedPane,
                CombinedView>> clickHandler;

        /**
         * Constructs {@code Builder}.
         */
        public Builder() {
            this.transformsList = new ArrayList<>();
            this.closeHandlerList = new ArrayList<>();
            this.chestRows = 1;
            this.title = Component.empty();
            this.updates = false;
            this.updateDelay = 1;
            this.clickHandler = ClickHandler.cancel();
        }

        private Builder(
                final @NonNull List<TransformContext<CombinedPane, PlayerViewer>> transformsList,
                final @NonNull List<CloseHandler<CombinedPane>> closeHandlerList,
                final int chestRows,
                final @NonNull Component title,
                final boolean updates,
                final int updateDelay,
                final @NonNull ClickHandler<CombinedPane, InventoryClickEvent, PlayerViewer, InventoryClickContext<CombinedPane,
                        CombinedView>> clickHandler
        ) {
            this.transformsList = Collections.unmodifiableList(transformsList);
            this.closeHandlerList = Collections.unmodifiableList(closeHandlerList);
            this.chestRows = chestRows;
            this.title = title;
            this.updates = updates;
            this.updateDelay = updateDelay;
            this.clickHandler = clickHandler;
        }

        /**
         * Returns the number of rows for the interface.
         *
         * @return the number of rows
         */
        public int chestRows() {
            return this.chestRows;
        }

        /**
         * Sets the number of rows for the interface.
         *
         * @param chestRows the number of chest rows
         * @return new builder instance
         */
        public @NonNull Builder chestRows(final int chestRows) {
            return new Builder(
                    this.transformsList,
                    this.closeHandlerList,
                    chestRows,
                    this.title,
                    this.updates,
                    this.updateDelay,
                    this.clickHandler
            );
        }

        /**
         * Returns the title of the interface.
         *
         * @return the title
         */
        public @NonNull Component title() {
            return this.title;
        }

        /**
         * Sets the title of the interface.
         *
         * @param title the title
         * @return new builder instance
         */
        public @NonNull Builder title(final @NonNull Component title) {
            return new Builder(
                    this.transformsList,
                    this.closeHandlerList,
                    this.chestRows,
                    title,
                    this.updates,
                    this.updateDelay,
                    this.clickHandler
            );
        }

        /**
         * Adds a close handler to the interface.
         *
         * @param closeHandler the close handler
         * @return new builder instance.
         */
        public @NonNull Builder addCloseHandler(final @NonNull CloseHandler<CombinedPane> closeHandler) {
            final List<CloseHandler<CombinedPane>> closeHandlers = new ArrayList<>(this.closeHandlerList);
            closeHandlers.add(closeHandler);

            return new Builder(
                    this.transformsList,
                    closeHandlers,
                    this.chestRows,
                    this.title,
                    this.updates,
                    this.updateDelay,
                    this.clickHandler
            );
        }

        /**
         * Adds a transformation to the interface.
         *
         * @param transform the transformation
         * @return new builder instance.
         */
        @Override
        public @NonNull Builder addTransform(
                final int priority,
                final @NonNull Transform<CombinedPane, PlayerViewer> transform,
                final @NonNull InterfaceProperty<?>... properties
        ) {
            final List<TransformContext<CombinedPane, PlayerViewer>> transforms = new ArrayList<>(this.transformsList);
            transforms.add(
                    TransformContext.of(
                            priority,
                            transform,
                            properties
                    )
            );

            return new Builder(
                    transforms,
                    this.closeHandlerList,
                    this.chestRows,
                    this.title,
                    this.updates,
                    this.updateDelay,
                    this.clickHandler
            );
        }

        /**
         * Adds a transformation to the interface.
         *
         * @param transform the transformation
         * @return new builder instance.
         */
        @Override
        public @NonNull Builder addTransform(final @NonNull Transform<CombinedPane, PlayerViewer> transform) {
            return this.addTransform(1, transform, InterfaceProperty.dummy());
        }

        /**
         * Returns the click handler.
         *
         * @return click handler
         */
        public @NonNull ClickHandler<CombinedPane, InventoryClickEvent, PlayerViewer,
                InventoryClickContext<CombinedPane, CombinedView>> clickHandler() {
            return this.clickHandler;
        }

        /**
         * Sets the click handler.
         *
         * @param handler the handler
         * @return new builder instance
         */
        public @NonNull Builder clickHandler(final @NonNull ClickHandler<CombinedPane, InventoryClickEvent,
                PlayerViewer, InventoryClickContext<CombinedPane, CombinedView>> handler) {
            return new Builder(
                    this.transformsList,
                    this.closeHandlerList,
                    this.chestRows,
                    this.title,
                    this.updates,
                    this.updateDelay,
                    handler
            );
        }

        /**
         * Controls how/if the interface updates.
         *
         * @param updates     true if the interface should update, false if not
         * @param updateDelay how many ticks to wait between updates
         * @return new builder instance
         */
        public @NonNull Builder updates(final boolean updates, final int updateDelay) {
            return new Builder(
                    this.transformsList,
                    this.closeHandlerList,
                    this.chestRows,
                    this.title,
                    updates,
                    updateDelay,
                    this.clickHandler
            );
        }

        /**
         * Constructs and returns the interface.
         *
         * @return the interface
         */
        @Override
        public @NonNull CombinedInterface build() {
            return new CombinedInterface(
                    this.chestRows,
                    this.title,
                    this.transformsList,
                    this.closeHandlerList,
                    this.updates,
                    this.updateDelay,
                    this.clickHandler
            );
        }

    }

}
