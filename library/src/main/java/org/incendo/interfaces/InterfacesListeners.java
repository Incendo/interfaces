package org.incendo.interfaces;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCloseEvent.Reason;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.incendo.interfaces.click.ClickContext;
import org.incendo.interfaces.click.ClickHandler;
import org.incendo.interfaces.click.CompletableClickHandler;
import org.incendo.interfaces.element.CompletedElement;
import org.incendo.interfaces.grid.GridPoint;
import org.incendo.interfaces.interfaces.CloseHandler;
import org.incendo.interfaces.pane.PlayerPane;
import org.incendo.interfaces.view.AbstractInterfaceView;
import org.incendo.interfaces.view.InterfaceView;
import org.incendo.interfaces.view.PlayerInterfaceView;

import java.util.EnumSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class InterfacesListeners implements Listener {

    // Tho current instance for interface listeners class.
    private static InterfacesListeners instance;

    // Returns the current instance for interface listeners class.
    public static InterfacesListeners instance() {
        return instance;
    }

    // Installs interfaces into this plugin.
    public static void install(final Plugin plugin) {
        if (instance != null) {
            throw new IllegalStateException("Already installed!");
        }

        instance = new InterfacesListeners(plugin);
        Bukkit.getPluginManager().registerEvents(instance, plugin);
    }

    private static final EnumSet<Reason> VALID_REASON = EnumSet.of(
        Reason.PLAYER,
        Reason.UNKNOWN,
        Reason.PLUGIN
    );

    private static final EnumSet<Action> VALID_INTERACT = EnumSet.of(
        Action.LEFT_CLICK_AIR,
        Action.LEFT_CLICK_BLOCK,
        Action.RIGHT_CLICK_AIR,
        Action.RIGHT_CLICK_BLOCK
    );

    private static final int PLAYER_INVENTORY_MIN = 0;
    private static final int PLAYER_INVENTORY_MAX = 40;
    private static final int OUTSIDE_CHEST_INDEX = -999;

    private final Plugin plugin;

    public InterfacesListeners(final Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin plugin() {
        return this.plugin;
    }

    private final Cache<UUID, Integer> spamPrevention = Caffeine.newBuilder()
        .expireAfterWrite(200, TimeUnit.MILLISECONDS)
        .build();

    /**
     * A cache of open player interface views, with weak values.
     */
    private final Cache<UUID, PlayerInterfaceView> openPlayerInterfaceViews = Caffeine.newBuilder()
        .weakValues()
        .build();

    /**
     * Returns the currently open interface for [playerId].
     */
    public PlayerInterfaceView getOpenInterface(final UUID playerId) {
        return this.openPlayerInterfaceViews.getIfPresent(playerId);
    }

    /**
     * Updates the currently open interface for [playerId] to [view].
     */
    public void setOpenInterface(final UUID playerId, final PlayerInterfaceView view) {
        if (view == null) {
            this.openPlayerInterfaceViews.invalidate(playerId);
        } else {
            this.openPlayerInterfaceViews.put(playerId, view);
        }
    }

    @EventHandler
    public void onClose(final InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        Reason reason = event.getReason();

        if (!(holder instanceof InterfaceView)) {
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            AbstractInterfaceView<?, ?> view = this.convertHolderToInterfaceView(holder);
            if (view != null) {
                CloseHandler closeHandler = view.backing().closeHandlers().get(reason);

                if (closeHandler != null) {
                    closeHandler.handle(reason, view);
                }
            }

            if (!VALID_REASON.contains(reason)) {
                return;
            }

            this.getOpenInterface(event.getPlayer().getUniqueId()).open();
        });
    }

    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        AbstractInterfaceView<?, ?> view = this.convertHolderToInterfaceView(holder);

        if (view == null) {
            return;
        }

        GridPoint clickedPoint = this.clickedPoint(event);

        if (clickedPoint == null) {
            return;
        }

        this.handleClick(view, clickedPoint, event.getClick(), event);
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        this.setOpenInterface(event.getPlayer().getUniqueId(), null);
    }

    public @Nullable GridPoint clickedPoint(final InventoryClickEvent event) {
        // Not really sure why this special handling is required,
        // the ordered pane system should solve this but this is the only
        // place where it's become an issue.
        if (event.getInventory().getHolder() instanceof Player) {
            int index = event.getSlot();

            if (index < PLAYER_INVENTORY_MIN || index > PLAYER_INVENTORY_MAX) {
                return null;
            }

            int x = index / 9;
            int adjustedX = PlayerPane.PANE_ORDERING.indexOf(x);
            return GridPoint.at(adjustedX, index % 9);
        }

        int index = event.getRawSlot();

        if (index == OUTSIDE_CHEST_INDEX) {
            return null;
        }

        return GridPoint.at(index / 9, index % 9);
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        if (!VALID_INTERACT.contains(event.getAction())) {
            return;
        }

        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Player player = event.getPlayer();
        AbstractInterfaceView<?, ?> view = this.getOpenInterface(player.getUniqueId());

        if (view == null) {
            return;
        }

        int slot = player.getInventory().getHeldItemSlot();
        GridPoint clickedPoint = GridPoint.at(3, slot);

        ClickType click = this.convertAction(event.getAction(), player.isSneaking());

        this.handleClick(view, clickedPoint, click, event);
    }

    /**
     * Converts an inventory holder to an [AbstractInterfaceView] if possible. If the holder is a player
     * their currently open player interface is returned.
     */
    public AbstractInterfaceView<?, ?> convertHolderToInterfaceView(final InventoryHolder holder) {
        return switch (holder) {
            // If it's an abstract view use that one.
            case AbstractInterfaceView<?, ?> abstractInterfaceView -> abstractInterfaceView;

            // If it's the player's own inventory use the held one.
            case HumanEntity humanEntity -> this.getOpenInterface(humanEntity.getUniqueId());

            case null, default -> null;
        };
    }

    public void handleClick(
        final AbstractInterfaceView<?, ?> view,
        final GridPoint clickedPoint,
        final ClickType click,
        final Cancellable event
    ) {
        if (view.isProcessingClick() || this.shouldThrottle(view.player())) {
            event.setCancelled(true);
            return;
        }

        view.setProcessingClick(true);

        ClickContext clickContext = new ClickContext(view.player(), view, click);

        view.backing().clickPreprocessors().forEach(handler -> ClickHandler.process(handler, clickContext));

        CompletedElement completedElement = view.pane().getRaw(clickedPoint.x(), clickedPoint.y());

        if (completedElement == null) {
            view.setProcessingClick(false);
            event.setCancelled(true);
            return;
        }

        CompletableClickHandler completedClickHandler = ClickHandler.process(completedElement.clickHandler(), clickContext)
            .onComplete(() -> view.setProcessingClick(false));

        if (!completedClickHandler.completingLater()) {
            completedClickHandler.complete();
        }

        event.setCancelled(completedClickHandler.cancelled());
    }

    public ClickType convertAction(final Action action, final boolean sneaking) {
        if (action.isRightClick()) {
            return sneaking ? ClickType.SHIFT_RIGHT : ClickType.RIGHT;
        }

        if (action.isLeftClick()) {
            return sneaking ? ClickType.SHIFT_LEFT : ClickType.LEFT;
        }

        return ClickType.UNKNOWN;
    }

    public boolean shouldThrottle(final Player player) {
        if (this.spamPrevention.getIfPresent(player.getUniqueId()) == null) {
            this.spamPrevention.put(player.getUniqueId(), null);
            return false;
        }

        return true;
    }

}
