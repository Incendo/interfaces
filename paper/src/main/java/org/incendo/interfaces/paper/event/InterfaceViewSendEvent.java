package org.incendo.interfaces.paper.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;

/**
 * An event that is fired when a view is sent to a {@link PlayerViewer}.
 */
public class InterfaceViewSendEvent extends Event {

    private static final @NonNull HandlerList handlers = new HandlerList();

    private final @NonNull InterfaceView<?, PlayerViewer> view;

    public InterfaceViewSendEvent(final @NonNull InterfaceView<?, PlayerViewer> view) {
        this.view = view;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public InterfaceView<?, PlayerViewer> view() {
        return view;
    }

    public @NonNull HandlerList getHandlers() {
        return handlers;
    }

}
