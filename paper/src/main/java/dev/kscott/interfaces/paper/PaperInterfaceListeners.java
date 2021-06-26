package dev.kscott.interfaces.paper;

import dev.kscott.interfaces.core.UpdatingInterface;
import dev.kscott.interfaces.core.view.InterfaceView;
import dev.kscott.interfaces.paper.element.ItemStackElement;
import dev.kscott.interfaces.paper.view.ChestView;
import dev.kscott.interfaces.paper.view.InventoryView;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles interface-related events.
 * <p>
 * Register this from your plugin if you want event handling to function.
 */
public class PaperInterfaceListeners implements Listener {

    /**
     * Holds all open views.
     */
    private final @NonNull Set<InterfaceView> openViews;

    /**
     * The plugin.
     */
    private final @NonNull JavaPlugin plugin;

    /**
     * Constructs {@code PaperInterfaceListeners}.
     */
    public PaperInterfaceListeners(final @NonNull JavaPlugin plugin) {
        this.openViews = new HashSet<>();
        this.plugin = plugin;
    }

    /**
     * Handles the open inventory event.
     *
     * @param event the event
     */
    @EventHandler
    public void onInventoryOpen(final @NonNull InventoryOpenEvent event) {

        final @NonNull Inventory inventory = event.getInventory();
        final @Nullable InventoryHolder holder = inventory.getHolder();

        if (holder == null) {
            return;
        }

        if (holder instanceof final @NonNull InventoryView view) {
            this.openViews.add(view);

            if (view.parent() instanceof UpdatingInterface updatingInterface) {
                if (updatingInterface.updates()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (view.viewing()) {
                                view.parent().open(view.viewer(), view.argument());
                            }
                        }
                    }.runTaskLater(this.plugin, updatingInterface.updateDelay());
                }
            }
        }
    }

    /**
     * Handles the close inventory event.
     *
     * @param event the event
     */
    @EventHandler
    public void onInventoryClose(final @NonNull InventoryCloseEvent event) {
        final @NonNull Inventory inventory = event.getInventory();
        final @Nullable InventoryHolder holder = inventory.getHolder();

        if (holder == null) {
            return;
        }

        if (holder instanceof final @NonNull InventoryView view) {
            this.openViews.remove(view);
        }
    }

    /**
     * Handles an inventory click.
     *
     * @param event the event
     */
    @EventHandler
    public void onInventoryClick(final @NonNull InventoryClickEvent event) {
        final @NonNull Inventory inventory = event.getInventory();

        final @Nullable InventoryHolder holder = inventory.getHolder();

        if (holder == null) {
            return;
        }

        if (holder instanceof ChestView chestView) {
            int slot = event.getSlot();
            int x = slot % 9;
            int y = slot / 9;

            chestView.parent().topClickHandler().accept(event, chestView);

            final @NonNull ItemStackElement element = chestView.pane().element(x, y);
            element.handler().accept(event, chestView);
        }
    }

}
