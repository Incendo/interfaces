package org.incendo.interfaces.interfaces;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.incendo.interfaces.view.InterfaceView;

@FunctionalInterface
public interface CloseHandler {
    void handle(InventoryCloseEvent.Reason reason, InterfaceView view);
}
