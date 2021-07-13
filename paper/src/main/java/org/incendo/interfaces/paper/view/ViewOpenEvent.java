package org.incendo.interfaces.paper.view;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;
import org.jetbrains.annotations.NotNull;

/**
 * Event emitted when a {@link InterfaceView} is opened
 */
public final class ViewOpenEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final @NotNull InterfaceView<?, PlayerViewer> view;

    /**
     * Construct a new {@code ViewOpenEvent}
     *
     * @param view the view
     */
    public ViewOpenEvent(final @NotNull InterfaceView<?, PlayerViewer> view) {
        super(view.viewer().player());
        this.view = view;
    }

    /**
     * Returns the view
     *
     * @return the view
     */
    public @NonNull InterfaceView<?, PlayerViewer> view() {
        return this.view;
    }

    /**
     * Returns the handler list
     *
     * @return the handler list
     */
    @NotNull
    public static @NonNull HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

}
