package org.incendo.interfaces.paper.type;

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
import org.incendo.interfaces.paper.pane.PlayerPane;
import org.incendo.interfaces.paper.utils.DefaultInterfacesUpdateExecutor;
import org.incendo.interfaces.paper.utils.InterfacesUpdateExecutor;
import org.incendo.interfaces.paper.view.PlayerInventoryView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PlayerInterface implements
        Interface<PlayerPane, PlayerViewer>,
        UpdatingInterface,
        ClickableInterface<PlayerPane, InventoryClickEvent, PlayerViewer> {

    private final @NonNull List<TransformContext<PlayerPane, PlayerViewer>> transformationList;
    private final boolean updates;
    private final int updateDelay;
    private final @NonNull ClickHandler<PlayerPane, InventoryClickEvent,
            PlayerViewer, InventoryClickContext<PlayerPane, PlayerInventoryView>> clickHandler;
    private final @NonNull InterfacesUpdateExecutor updateExecutor;

    /**
     * Constructs {@code PlayerInterface}.
     *
     * @param transforms   the transformations to apply
     * @param updates      {@code true} if the interface is an updating interface
     * @param updateDelay  the update delay
     * @param clickHandler the handler to run on click
     * @param updateExecutor executor in which to run update tasks from
     */
    public PlayerInterface(
            final @NonNull List<TransformContext<PlayerPane, PlayerViewer>> transforms,
            final boolean updates,
            final int updateDelay,
            final @NonNull ClickHandler<PlayerPane, InventoryClickEvent, PlayerViewer, InventoryClickContext<PlayerPane,
                    PlayerInventoryView>> clickHandler,
            final @NonNull InterfacesUpdateExecutor updateExecutor
    ) {
        this.transformationList = transforms;
        this.updates = updates;
        this.updateDelay = updateDelay;
        this.clickHandler = clickHandler;
        this.updateExecutor = updateExecutor;
    }

    /**
     * Returns a new PlayerInterface builder
     *
     * @return the builder
     */
    public static @NonNull Builder builder() {
        return new Builder();
    }

    @Override
    public @NonNull Interface<PlayerPane, PlayerViewer> transform(
            @NonNull final Transform<PlayerPane, PlayerViewer> transform
    ) {
        this.transformationList.add(
                TransformContext.of(
                        1,
                        transform
                )
        );
        return this;
    }

    @Override
    public @NonNull List<TransformContext<PlayerPane, PlayerViewer>> transformations() {
        return List.copyOf(this.transformationList);
    }

    @Override
    public @NonNull InterfaceView<PlayerPane, PlayerViewer> open(
            @NonNull final PlayerViewer viewer
    ) {
        return this.open(viewer, HashMapInterfaceArguments.empty());
    }

    @Override
    public @NonNull InterfaceView<PlayerPane, PlayerViewer> open(
            @NonNull final InterfaceView<?, PlayerViewer> parent,
            @NonNull final InterfaceArguments arguments
    ) {
        return this.open(parent.viewer(), arguments);
    }

    @Override
    public @NonNull InterfaceView<PlayerPane, PlayerViewer> open(
            @NonNull final PlayerViewer viewer,
            @NonNull final InterfaceArguments arguments
    ) {
        final @NonNull PlayerInventoryView view = new PlayerInventoryView(
                this,
                viewer,
                arguments
        );

        view.open();

        return view;
    }

    @Override
    public boolean updates() {
        return this.updates;
    }

    @Override
    public int updateDelay() {
        return this.updateDelay;
    }

    @Override
    public @NonNull ClickHandler<
            PlayerPane,
            InventoryClickEvent,
            PlayerViewer,
            InventoryClickContext<PlayerPane, PlayerInventoryView>> clickHandler() {
        return this.clickHandler;
    }

    /**
     * The executor in which to run update tasks on.
     * @return the executor
     */
    public InterfacesUpdateExecutor updateExecutor() {
        return this.updateExecutor;
    }

    /**
     * A class that builds a player interface.
     */
    public static final class Builder implements Interface.Builder<PlayerPane, PlayerViewer, PlayerInterface> {

        /**
         * The list of transformations.
         */
        private final @NonNull List<@NonNull TransformContext<PlayerPane, PlayerViewer>> transformsList;

        /**
         * True if updating interface, false if not.
         */
        private final boolean updates;

        /**
         * How many ticks to wait between interface updates.
         */
        private final int updateDelay;

        private final InterfacesUpdateExecutor updateExecutor;

        /**
         * The top click handler.
         */
        private final @NonNull ClickHandler<PlayerPane, InventoryClickEvent, PlayerViewer, InventoryClickContext<PlayerPane,
                PlayerInventoryView>> clickHandler;

        /**
         * Constructs {@code Builder}.
         */
        public Builder() {
            this.transformsList = new ArrayList<>();
            this.updates = false;
            this.updateDelay = 1;
            this.clickHandler = ClickHandler.cancel();
            this.updateExecutor = new DefaultInterfacesUpdateExecutor();
        }

        private Builder(
                final @NonNull List<TransformContext<PlayerPane, PlayerViewer>> transformsList,
                final boolean updates,
                final int updateDelay,
                final @NonNull ClickHandler<PlayerPane, InventoryClickEvent, PlayerViewer, InventoryClickContext<PlayerPane,
                        PlayerInventoryView>> clickHandler,
                final @NonNull InterfacesUpdateExecutor updateExecutor
        ) {
            this.transformsList = Collections.unmodifiableList(transformsList);
            this.updates = updates;
            this.updateDelay = updateDelay;
            this.clickHandler = clickHandler;
            this.updateExecutor = updateExecutor;
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
                final @NonNull Transform<PlayerPane, PlayerViewer> transform,
                final @NonNull InterfaceProperty<?>... properties
        ) {
            final List<TransformContext<PlayerPane, PlayerViewer>> transforms = new ArrayList<>(this.transformsList);
            transforms.add(
                    TransformContext.of(
                            priority,
                            transform,
                            properties
                    )
            );

            return new Builder(
                    transforms,
                    this.updates,
                    this.updateDelay,
                    this.clickHandler,
                    this.updateExecutor
            );
        }

        /**
         * Adds a transformation to the interface.
         *
         * @param transform the transformation
         * @return new builder instance.
         */
        @Override
        public @NonNull Builder addTransform(final @NonNull Transform<PlayerPane, PlayerViewer> transform) {
            return this.addTransform(1, transform, InterfaceProperty.dummy());
        }

        /**
         * Returns the click handler.
         *
         * @return click handler
         */
        public @NonNull ClickHandler<PlayerPane, InventoryClickEvent,
                PlayerViewer, InventoryClickContext<PlayerPane, PlayerInventoryView>> clickHandler() {
            return this.clickHandler;
        }

        /**
         * Sets the click handler.
         *
         * @param handler the handler
         * @return new builder instance
         */
        public @NonNull Builder clickHandler(
                final @NonNull ClickHandler<PlayerPane, InventoryClickEvent,
                        PlayerViewer, InventoryClickContext<PlayerPane, PlayerInventoryView>> handler
        ) {
            return new Builder(
                    this.transformsList,
                    this.updates,
                    this.updateDelay,
                    handler,
                    this.updateExecutor
            );
        }

        /**
         * Set your own update executor.
         * @see org.incendo.interfaces.paper.utils.SynchronousInterfacesUpdateExecutor
         *
         * @param updateExecutor the executor
         * @return new builder instance
         */
        public @NonNull Builder setUpdateExecutor(final @NonNull InterfacesUpdateExecutor updateExecutor) {
            return new Builder(
                    this.transformsList,
                    this.updates,
                    this.updateDelay,
                    this.clickHandler,
                    updateExecutor
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
                    updates,
                    updateDelay,
                    this.clickHandler,
                    this.updateExecutor
            );
        }

        /**
         * Constructs and returns the interface.
         *
         * @return the interface
         */
        @Override
        public @NonNull PlayerInterface build() {
            return new PlayerInterface(
                    this.transformsList,
                    this.updates,
                    this.updateDelay,
                    this.clickHandler,
                    this.updateExecutor
            );
        }

    }

}
