package org.incendo.interfaces.paper.view;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;

/**
 * An InterfaceView containing a Bukkit inventory.
 *
 * @param <T> the pane type
 */
public interface PlayerView<T extends Pane> extends InterfaceView<T, PlayerViewer>,
        InventoryHolder {}
