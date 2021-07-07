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
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.interfaces.core.UpdatingInterface;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.core.view.SelfUpdatingInterfaceView;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.type.ChestInterface;
import org.incendo.interfaces.paper.type.CloseHandler;
import org.incendo.interfaces.paper.view.ChestView;
import org.incendo.interfaces.paper.view.InventoryView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Handles interface-related events.
 * <p>
 * Register this from your plugin if you want event handling to function.
 */
public class PaperInterfaceListeners implements Listener {

    private final @NonNull Set<@NonNull InterfaceView<?, PlayerViewer>> openViews;
    private final @NonNull Map<@NonNull SelfUpdatingInterfaceView, @NonNull Integer> updatingRunnables;
    private final @NonNull Plugin plugin;

    /**
     * Constructs {@code PaperInterfaceListeners}.
     *
     * @param plugin the plugin instance to register against
     */
    public PaperInterfaceListeners(final @NonNull Plugin plugin) {
        this.openViews = new HashSet<>();
        this.updatingRunnables = new HashMap<>();
        this.plugin = plugin;
    }

    /**
     * Installs the listeners for the given plugin.
     *
     * @param plugin owning plugin
     */
    public static void install(final @NonNull Plugin plugin) {
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
                    BukkitRunnable runnable = new BukkitRunnable() {
                        @Override
                        public void run() {
                            view.update();
                        }
                    };

                    if (view instanceof SelfUpdatingInterfaceView) {
                        SelfUpdatingInterfaceView selfUpdating = (SelfUpdatingInterfaceView) view;
                        runnable.runTaskTimer(this.plugin, updatingInterface.updateDelay(), updatingInterface.updateDelay());
                        this.updatingRunnables.put(selfUpdating, runnable.getTaskId());
                    } else {
                        runnable.runTaskLater(this.plugin, updatingInterface.updateDelay());
                    }
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
            InventoryView inventoryView = (InventoryView) holder;
            this.openViews.remove(inventoryView);

            if (inventoryView.parent() instanceof ChestInterface) {
                ChestInterface chestInterface = (ChestInterface) inventoryView.parent();

                for (final CloseHandler<ChestPane> closeHandler : chestInterface.closeHandlers()) {
                    closeHandler.accept(event, inventoryView);
                }
            }

            if (inventoryView instanceof SelfUpdatingInterfaceView) {
                SelfUpdatingInterfaceView selfUpdating = (SelfUpdatingInterfaceView) inventoryView;

                Bukkit.getScheduler().cancelTask(this.updatingRunnables.get(selfUpdating));
                this.updatingRunnables.remove(selfUpdating);
            }
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
