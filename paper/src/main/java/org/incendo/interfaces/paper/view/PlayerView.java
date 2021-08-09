package org.incendo.interfaces.paper.view;

import org.bukkit.Bukkit;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.Interface;
import org.incendo.interfaces.core.arguments.HashMapInterfaceArguments;
import org.incendo.interfaces.core.arguments.InterfaceArguments;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;

/**
 * An InterfaceView containing a Bukkit inventory.
 *
 * @param <T> the pane type
 */
public interface PlayerView<T extends Pane> extends InterfaceView<T, PlayerViewer>,
        InventoryHolder {

    /**
     * Opens a child interface.
     *
     * @param backing  the backing interface
     * @param argument the argument
     * @param <C>      the type of view
     * @return the view
     */
    @NonNull <C extends PlayerView<?>> C openChild(
            @NonNull Interface<?, PlayerViewer> backing,
            @NonNull InterfaceArguments argument
    );

    /**
     * Opens a child interface.
     *
     * @param backing the backing interface
     * @param <C>     the type of view
     * @return the view
     */

    default @NonNull <C extends PlayerView<?>> C openChild(final @NonNull Interface<?, PlayerViewer> backing) {
        return this.openChild(backing, HashMapInterfaceArguments.empty());
    }

    /**
     * Emits a {@link ViewOpenEvent} for this view
     */
    default void emitEvent() {
        Bukkit.getPluginManager().callEvent(new ViewOpenEvent(this));
    }

}
