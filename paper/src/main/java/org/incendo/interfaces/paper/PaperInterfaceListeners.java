package org.incendo.interfaces.paper;

import com.google.common.cache.Cache;

import com.google.common.cache.CacheBuilder;

import java.util.UUID;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
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
import org.incendo.interfaces.paper.pane.CombinedPane;
import org.incendo.interfaces.paper.pane.PlayerPane;
import org.incendo.interfaces.paper.type.ChestInterface;
import org.incendo.interfaces.paper.type.CloseHandler;
import org.incendo.interfaces.paper.view.ChestView;
import org.incendo.interfaces.paper.view.CombinedView;
import org.incendo.interfaces.paper.view.PlayerInventoryView;
import org.incendo.interfaces.paper.view.PlayerView;
import org.incendo.interfaces.paper.view.TaskableView;
import org.incendo.interfaces.paper.view.ViewCloseEvent;
import org.incendo.interfaces.paper.view.ViewOpenEvent;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Handles interface-related events.
 * <p>
 * Register this from your plugin if you want event handling to function.
 */
@SuppressWarnings("unused")
public class PaperInterfaceListeners implements Listener {

    private static final @NonNull Set<Action> VALID_ACTIONS = EnumSet.of(
            Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK,
            Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK
    );

    private static final @NonNull Set<InventoryCloseEvent.Reason> VALID_REASON = EnumSet.of(
            InventoryCloseEvent.Reason.PLAYER, InventoryCloseEvent.Reason.UNKNOWN, InventoryCloseEvent.Reason.PLUGIN
    );

    private final @NonNull Plugin plugin;
    private final @NonNull Map<@NonNull SelfUpdatingInterfaceView, @NonNull Integer> updatingRunnables;

    private final @Nullable Cache<UUID, Long> spamPrevention;

    /**
     * Constructs {@code PaperInterfaceListeners}.
     *
     * @param plugin the plugin instance to register against
     */
    public PaperInterfaceListeners(final @NonNull Plugin plugin) {
        this.plugin = plugin;
        this.updatingRunnables = new HashMap<>();
        this.spamPrevention = null;
    }

    /**
     * Constructs {@code PaperInterfaceListeners}.
     *
     * @param plugin        the plugin instance to register against
     * @param clickThrottle the minimum amount of ticks between every accepted click
     */
    public PaperInterfaceListeners(final @NonNull Plugin plugin, final long clickThrottle) {
        this.plugin = plugin;
        this.updatingRunnables = new HashMap<>();
        this.spamPrevention = CacheBuilder.newBuilder().expireAfterWrite(
                50L * clickThrottle,
                TimeUnit.MILLISECONDS
        ).build();
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
     * Handles the plugin disable event.
     *
     * @param event the event
     */
    @EventHandler
    public void onDisable(final @NonNull PluginDisableEvent event) {
        if (event.getPlugin() != this.plugin) {
            return;
        }

        for (final PlayerInventoryView view : PlayerInventoryView.getAllAndClear()) {
            view.close();
        }
    }

    /**
     * Handles the open inventory event.
     *
     * @param event the event
     */
    @EventHandler
    public void onInventoryOpen(final @NonNull InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

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
                    runnable.runTaskTimerAsynchronously(this.plugin, updatingInterface.updateDelay(), updatingInterface.updateDelay());
                    this.updatingRunnables.put(selfUpdating, runnable.getTaskId());
                } else {
                    runnable.runTaskLaterAsynchronously(this.plugin, updatingInterface.updateDelay());
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
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        if (holder == null) {
            return;
        }

        if (holder instanceof PlayerView) {
            PlayerView<?> playerView = (PlayerView<?>) holder;

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

        if (!(holder instanceof Player)) {
            Player player = (Player) event.getPlayer();
            PlayerInventoryView playerInventoryView = PlayerInventoryView.forPlayer(player);

            if (playerInventoryView != null && VALID_REASON.contains(event.getReason())) {
                playerInventoryView.open();
            }
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
     * Handles the player death event.
     *
     * @param event the event
     */
    @EventHandler
    public void onPlayerDeath(final @NonNull PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final PlayerInventoryView view = PlayerInventoryView.forPlayer(player);

        if (view != null) {
            event.getDrops().clear();
            event.setKeepInventory(true);
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

    /**
     * Handles the player interact event.
     *
     * @param event the event
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(final @NonNull PlayerInteractEvent event) {
        if (!VALID_ACTIONS.contains(event.getAction())) {
            return;
        }

        Player player = event.getPlayer();
        PlayerInventoryView view = PlayerInventoryView.forPlayer(player);

        if (view == null) {
            return;
        }

        ItemStack item = event.getItem();
        int hotbarSlot = player.getInventory().getHeldItemSlot();
        ItemStackElement<PlayerPane> hotbar = view.pane().hotbar(hotbarSlot);

        if (hotbar.equals(ItemStackElement.empty())) {
            return;
        }

        // Mimic bukkit inventory click event for hotbar interactions
        InventoryClickEvent inventoryClickEvent = new InventoryClickEvent(
                player.getOpenInventory(),
                InventoryType.SlotType.QUICKBAR,
                hotbarSlot,
                this.clickTypeFromAction(event.getAction(), player.isSneaking()),
                InventoryAction.NOTHING
        );

        InventoryClickContext<PlayerPane, PlayerInventoryView> fakeContext = new InventoryClickContext<>(
                inventoryClickEvent,
                true,
                true
        );

        hotbar.clickHandler().accept(fakeContext);

        if (fakeContext.cancelled()) {
            event.setCancelled(true);
            event.setUseItemInHand(Event.Result.DENY);
        }
    }

    private void cleanUpView(final @NonNull InterfaceView<?, PlayerViewer> view) {
        if (view instanceof SelfUpdatingInterfaceView) {
            SelfUpdatingInterfaceView selfUpdating = (SelfUpdatingInterfaceView) view;

            if (selfUpdating.updates()) {
                Bukkit.getScheduler().cancelTask(this.updatingRunnables.get(selfUpdating));
                this.updatingRunnables.remove(selfUpdating);
            }
        }

        if (view instanceof TaskableView) {
            TaskableView taskableView = (TaskableView) view;

            for (final Integer task : taskableView.taskIds()) {
                Bukkit.getScheduler().cancelTask(task);
            }
        }

        if (view instanceof CombinedView) {
            //todo: Other ways we could handle this?
            view.viewer().player().getInventory().clear();
        }

        if (view instanceof PlayerInventoryView) {
            PlayerInventoryView.removeForPlayer(view.viewer().player());
        }
    }

    private boolean shouldThrottle(final @NonNull Player player) {
        if (this.spamPrevention == null) {
            return false;
        }

        if (this.spamPrevention.getIfPresent(player.getUniqueId()) != null) {
            return true;
        } else {
            this.spamPrevention.put(player.getUniqueId(), System.currentTimeMillis());
        }

        return false;
    }

    /**
     * Handles an inventory click.
     *
     * @param event the event
     */
    @EventHandler
    public void onInventoryClick(final @NonNull InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        if (holder instanceof ChestView) {
            if (this.shouldThrottle((Player) event.getWhoClicked())) {
                event.setCancelled(true);
            } else {
                this.handleChestViewClick(event, holder);
            }
        } else if (holder instanceof CombinedView) {
            if (this.shouldThrottle((Player) event.getWhoClicked())) {
                event.setCancelled(true);
            } else {
                this.handleCombinedViewClick(event, holder);
            }
        } else if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof Player) {
            this.handlePlayerViewClick(event);
        }
    }

    private void handleChestViewClick(final @NonNull InventoryClickEvent event, final @NonNull InventoryHolder holder) {
        if (event.getSlot() != event.getRawSlot()) {
            this.handlePlayerViewClick(event);
            return;
        }

        InventoryClickContext<ChestPane, ChestView> context = new InventoryClickContext<>(
                event,
                false,
                false
        );

        ChestView chestView = (ChestView) holder;
        // Handle parent interface click event
        chestView.backing().clickHandler().accept(context);

        // Handle element click event
        if (event.getSlotType() == InventoryType.SlotType.CONTAINER) {
            int slot = event.getSlot();
            int x = slot % 9;
            int y = slot / 9;

            chestView.pane()
                    .element(x, y)
                    .clickHandler()
                    .accept(context);
        }
    }

    private void handleCombinedViewClick(final @NonNull InventoryClickEvent event, final @NonNull InventoryHolder holder) {
        InventoryClickContext<CombinedPane, CombinedView> context = new InventoryClickContext<>(
                event,
                false,
                false
        );

        CombinedView combinedView = (CombinedView) holder;
        // Handle parent interface click event
        combinedView.backing().clickHandler().accept(context);

        // Handle element click event
        if (event.getSlotType() == InventoryType.SlotType.CONTAINER) {
            int slot = event.getSlot();
            int x = slot % 9;
            int y = slot / 9;

            if (event.getSlot() != event.getRawSlot()) {
                y += combinedView.backing().chestRows() - 1;
            }

            combinedView.pane()
                    .element(x, y)
                    .clickHandler()
                    .accept(context);
        }

        if (event.getSlotType() == InventoryType.SlotType.QUICKBAR) {
            combinedView.pane().hotbar(event.getSlot()).clickHandler().accept(context);
        }
    }

    private void handlePlayerViewClick(final @NonNull InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        boolean isCraftGrid = clickedInventory instanceof CraftingInventory;

        if (PlayerInventoryView.forPlayer((Player) event.getWhoClicked()) == null) {
            return;
        }

        if (this.shouldThrottle((Player) event.getWhoClicked())) {
            event.setCancelled(true);
            return;
        }

        final InventoryClickContext<PlayerPane, PlayerInventoryView> context = new InventoryClickContext<>(
                event,
                true,
                false
        );

        PlayerInventoryView playerInventoryView = context.view();
        playerInventoryView.backing().clickHandler().accept(context);

        playerInventoryView.pane()
                .element(event.getSlot())
                .clickHandler()
                .accept(context);
    }

    private ClickType clickTypeFromAction(final @NonNull Action action, final boolean sneaking) {
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (sneaking) {
                return ClickType.SHIFT_RIGHT;
            }

            return ClickType.RIGHT;
        }

        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            if (sneaking) {
                return ClickType.SHIFT_LEFT;
            }

            return ClickType.LEFT;
        }

        return ClickType.UNKNOWN;
    }

}
