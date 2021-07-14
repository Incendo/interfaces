package org.incendo.interfaces.paper;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.interfaces.core.UpdatingInterface;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.core.view.SelfUpdatingInterfaceView;
import org.incendo.interfaces.paper.click.InventoryClickContext;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.pane.PlayerPane;
import org.incendo.interfaces.paper.type.ChestInterface;
import org.incendo.interfaces.paper.type.CloseHandler;
import org.incendo.interfaces.paper.view.ChestView;
import org.incendo.interfaces.paper.view.PlayerInventoryView;
import org.incendo.interfaces.paper.view.PlayerView;
import org.incendo.interfaces.paper.view.TaskableView;
import org.incendo.interfaces.paper.view.ViewCloseEvent;
import org.incendo.interfaces.paper.view.ViewOpenEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Handles interface-related events.
 * <p>
 * Register this from your plugin if you want event handling to function.
 */
@SuppressWarnings("unused")
public class PaperInterfaceListeners implements Listener {

    private final @NonNull Set<@NonNull InterfaceView<?, PlayerViewer>> openViews;
    private final @NonNull Map<@NonNull SelfUpdatingInterfaceView, @NonNull Integer> updatingRunnablesIds;
    private final @NonNull Plugin plugin;

    /**
     * Constructs {@code PaperInterfaceListeners}.
     *
     * @param plugin the plugin instance to register against
     */
    public PaperInterfaceListeners(final @NonNull Plugin plugin) {
        this.openViews = new HashSet<>();
        this.updatingRunnablesIds = new HashMap<>();
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

        if (holder instanceof PlayerView) {
            PlayerView<?> view = (PlayerView<?>) holder;
            this.addOpenView(view);
        }
    }

    /**
     * Handles the open view event.
     *
     * @param event the event
     */
    @EventHandler
    public void onViewOpen(final @NonNull ViewOpenEvent event) {
        final @NonNull InterfaceView<?, PlayerViewer> view = event.view();

        if (view instanceof PlayerInventoryView) {
            this.addOpenView(view);
        }
    }

    /**
     * Adds an open view.
     *
     * @param view the view
     */
    public void addOpenView(final @NonNull InterfaceView<?, PlayerViewer> view) {
        this.openViews.add(view);

        if (view.backing() instanceof UpdatingInterface) {
            UpdatingInterface updatingInterface = (UpdatingInterface) view.backing();
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
                    this.updatingRunnablesIds.put(selfUpdating, runnable.getTaskId());
                } else {
                    runnable.runTaskLater(this.plugin, updatingInterface.updateDelay());
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
    @SuppressWarnings("unchecked")
    public void onInventoryClose(final @NonNull InventoryCloseEvent event) {
        final @NonNull Inventory inventory = event.getInventory();
        final @Nullable InventoryHolder holder = inventory.getHolder();

        if (holder == null) {
            return;
        }

        if (holder instanceof PlayerView) {
            final PlayerView<?> playerView = (PlayerView<?>) holder;
            this.openViews.remove(playerView);

            if (playerView.backing() instanceof ChestInterface) {
                final ChestInterface chestInterface = (ChestInterface) playerView.backing();

                for (final CloseHandler<ChestPane> closeHandler : chestInterface.closeHandlers()) {
                    closeHandler.accept(event, (PlayerView<ChestPane>) playerView);
                }
            }
        }

        if (holder instanceof InterfaceView) {
            this.cleanUpView((InterfaceView<?, PlayerViewer>) holder);
        }
    }

    /**
     * Handles the player quit event.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerQuit(final @NonNull PlayerQuitEvent event) {
        final PlayerInventoryView view = PlayerInventoryView.forPlayer(event.getPlayer());

        if (view != null) {
            view.close();
        }
    }

    /**
     * Handles the close view event.
     *
     * @param event the event
     */
    @EventHandler
    public void onViewClose(final @NonNull ViewCloseEvent event) {
        if (event.view() instanceof PlayerInventoryView) {
            this.cleanUpView(event.view());
        }
    }

    private void cleanUpView(final @NonNull InterfaceView<?, PlayerViewer> view) {
        if (view instanceof SelfUpdatingInterfaceView) {
            SelfUpdatingInterfaceView selfUpdating = (SelfUpdatingInterfaceView) view;

            if (selfUpdating.updates()) {
                Bukkit.getScheduler().cancelTask(this.updatingRunnablesIds.get(selfUpdating));
                this.updatingRunnablesIds.remove(selfUpdating);
            }
        }

        if (view instanceof TaskableView) {
            TaskableView taskableView = (TaskableView) view;

            for (final Integer task : taskableView.taskIds()) {
                Bukkit.getScheduler().cancelTask(task);
            }
        }

        if (view instanceof PlayerInventoryView) {
            PlayerInventoryView.removeForPlayer(view.viewer().player());
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

        if (holder instanceof ChestView) {
            final InventoryClickContext<ChestPane, ChestView> context = new InventoryClickContext<>(event, false);

            ChestView chestView = (ChestView) holder;
            // Handle parent interface click event
            chestView.backing().clickHandler().accept(context);

            // Handle element click event
            if (event.getSlotType() == InventoryType.SlotType.CONTAINER) {
                int slot = event.getSlot();
                int x = slot % 9;
                int y = slot / 9;

                final @NonNull ItemStackElement<ChestPane> element = chestView.pane().element(x, y);
                element.clickHandler().accept(context);
            }
        } else if (event.getClickedInventory() != null && event.getClickedInventory() instanceof PlayerInventory) {
            if (PlayerInventoryView.forPlayer((Player) event.getWhoClicked()) == null) {
                return;
            }

            final InventoryClickContext<PlayerPane, PlayerInventoryView> context = new InventoryClickContext<>(
                    event,
                    true
            );

            PlayerInventoryView playerInventoryView = context.view();

            final @NonNull ItemStackElement<PlayerPane> element = playerInventoryView.pane().element(event.getSlot());
            element.clickHandler().accept(context);
        }
    }

}
