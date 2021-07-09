package org.incendo.interfaces.paper.click;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.click.Click;
import org.incendo.interfaces.core.click.ClickContext;
import org.incendo.interfaces.core.click.clicks.Clicks;
import org.incendo.interfaces.core.view.InterfaceViewer;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.pane.ChatPane;
import org.incendo.interfaces.paper.view.ChatView;
import org.incendo.interfaces.paper.view.ChestView;

/**
 * The click context of a chat element.
 */
@SuppressWarnings("unused")
public final class ChatClickContext implements ClickContext<ChatPane, PlayerCommandPreprocessEvent> {

    private final @NonNull PlayerCommandPreprocessEvent event;
    private final @NonNull ChatView view;
    private final @NonNull Click<PlayerCommandPreprocessEvent> click;

    /**
     * Constructs {@code ChatClickContext}.
     *
     * @param view  the view
     * @param event the event
     */
    public ChatClickContext(
            final @NonNull ChatView view,
            final @NonNull PlayerCommandPreprocessEvent event
    ) {
        this.event = event;
        this.view = view;

        this.click = Clicks.leftClick(this.event, false);
    }

    @Override
    public @NonNull PlayerCommandPreprocessEvent cause() {
        return this.event;
    }

    @Override
    public boolean cancelled() {
        return this.event.isCancelled();
    }

    @Override
    public void cancel(final boolean cancelled) {
        this.event.setCancelled(cancelled);
    }

    @Override
    public @NonNull ChatView view() {
        return this.view;
    }

    @Override
    public @NonNull InterfaceViewer viewer() {
        return this.view.viewer();
    }

    @Override
    public @NonNull Click<PlayerCommandPreprocessEvent> click() {
        return this.click;
    }

}
