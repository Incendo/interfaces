package org.incendo.interfaces.click

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.incendo.interfaces.next.view.InterfaceView;

import java.util.Objects;

public final class ClickContext {
    private final Player player;
    private final InterfaceView view;
    private final ClickType type;
    private boolean cancelled = true;

    public ClickContext(Player player, InterfaceView view, ClickType type) {
        this.player = player;
        this.view = view;
        this.type = type;
    }

    public Player player() {
        return player;
    }

    public InterfaceView view() {
        return view;
    }

    public ClickType type() {
        return type;
    }

    public boolean cancelled() {
        return cancelled;
    }

    public void cancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

}
