package org.incendo.interfaces.paper;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.interfaces.core.UpdatingInterface;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.view.ChestView;
import org.incendo.interfaces.paper.view.InventoryView;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles interface-related events.
 * <p>
 * Register this from your plugin if you want event handling to function.
 */
public class PaperInterfaceListeners implements Listener {

    private final @NonNull Set<@NonNull InterfaceView<?, PlayerViewer, ?>> openViews;
    private final @NonNull JavaPlugin plugin;

    /**
     * Constructs {@code PaperInterfaceListeners}.
     *
     * @param plugin the plugin instance to register against
     */
    public PaperInterfaceListeners(final @NonNull JavaPlugin plugin) {
        this.openViews = new HashSet<>();
        this.plugin = plugin;
    }

    /**
     * Installs the listeners for the given plugin.
     *
     * @param plugin owning plugin
     */
    public static void install(final @NonNull JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(new PaperInterfaceListeners(plugin), plugin);
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

        if (holder instanceof InventoryView) {
            InventoryView<?> view = (InventoryView<?>) holder;
            this.openViews.add(view);

            if (view.parent() instanceof UpdatingInterface) {
                UpdatingInterface updatingInterface = (UpdatingInterface) view.parent();
                if (updatingInterface.updates()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            view.update();
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

        if (holder instanceof InventoryView) {
            this.openViews.remove((InventoryView) holder);
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

        if (holder instanceof ChestView) {
            ChestView chestView = (ChestView) holder;
            // Handle parent interface click event
            chestView.parent().clickHandler().accept(event, chestView);

            // Handle element click event
            if (event.getSlotType() == InventoryType.SlotType.CONTAINER) {
                int slot = event.getSlot();
                int x = slot % 9;
                int y = slot / 9;

                final @NonNull ItemStackElement element = chestView.pane().element(x, y);
                element.handler().accept(event, chestView);
            }
        }
    }

}
