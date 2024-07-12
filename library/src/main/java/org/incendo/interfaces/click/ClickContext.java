package org.incendo.interfaces.click;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.incendo.interfaces.view.InterfaceView;

public record ClickContext(
    Player player,
    InterfaceView view,
    ClickType type
) {
}
