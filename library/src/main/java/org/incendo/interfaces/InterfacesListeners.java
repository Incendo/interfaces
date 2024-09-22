package org.incendo.interfaces;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.incendo.interfaces.click.ClickContext;
import org.incendo.interfaces.click.ClickHandler;
import org.incendo.interfaces.click.CompletableClickHandler;
import org.incendo.interfaces.element.CompletedElement;
import org.incendo.interfaces.grid.GridPoint;
import org.incendo.interfaces.interfaces.CloseHandler;
import org.incendo.interfaces.view.AbstractInterfaceView;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public final class InterfacesListeners implements Listener {

    private static InterfacesListeners instance;

    private static final int OUTSIDE_SLOT = -999;
    private static final int INVENTORY_WIDTH = 9;
    private static final long CLICK_THROTTLE_DURATION = 200L;

    private final Plugin plugin;
    private final Cache<UUID, Object> clickThrottleCache = Caffeine.newBuilder()
        .expireAfterWrite(CLICK_THROTTLE_DURATION, TimeUnit.MILLISECONDS)
        .build();


    private InterfacesListeners(final Plugin plugin) {
        this.plugin = plugin;
        instance = this;
    }

    public static void install(final Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new InterfacesListeners(plugin), plugin);
    }

    public static InterfacesListeners instance() {
        return instance;
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        AbstractInterfaceView<?, ?> view = this.getInterfaceView(holder);
        if (view == null) {
            return;
        }
        CloseHandler closeHandler = view.backing().closeHandlers().get(event.getReason());
        if (closeHandler != null) {
            closeHandler.handle(event.getReason(), view);
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        AbstractInterfaceView<?, ?> view = this.getInterfaceView(holder);
        if (view == null) {
            return;
        }
        GridPoint point = this.getClickPosition(event);
        if (point == null) {
            return;
        }
        this.handleInventoryClick(view, point, event.getClick(), event);
    }

    private AbstractInterfaceView<?, ?> getInterfaceView(final InventoryHolder holder) {
        if (holder instanceof AbstractInterfaceView<?, ?>) {
            return (AbstractInterfaceView<?, ?>) holder;
        }
        return null;
    }

    private GridPoint getClickPosition(final InventoryClickEvent event) {
        int slot = event.getRawSlot();
        if (slot == OUTSIDE_SLOT) {
            return null;
        }
        int y = slot % INVENTORY_WIDTH;
        int x = slot / INVENTORY_WIDTH;
        return GridPoint.at(x, y);
    }

    private void handleInventoryClick(
        final AbstractInterfaceView<?, ?> view,
        final GridPoint point,
        final ClickType clickType,
        final Cancellable cancellable
    ) {
        if (this.shouldCancelClick(view)) {
            cancellable.setCancelled(true);
            return;
        }
        view.setProcessingClick(true);
        try {
            ClickContext context = new ClickContext(view.player(), view, clickType);
            this.processClickPreprocessors(view, context);
            CompletedElement element = view.pane().get(point);
            if (element == null) {
                cancellable.setCancelled(true);
                return;
            }
            this.processElementClick(element, context, cancellable);
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, "Error processing click at " + point, e);
            cancellable.setCancelled(true);
        } finally {
            view.setProcessingClick(false);
        }
    }

    private boolean shouldCancelClick(final AbstractInterfaceView<?, ?> view) {
        return view.isProcessingClick() || this.isClickThrottled(view.player());
    }

    private void processClickPreprocessors(final AbstractInterfaceView<?, ?> view, final ClickContext context) {
        view.backing().clickPreprocessors().forEach(handler -> ClickHandler.process(handler, context));
    }

    private void processElementClick(
        final CompletedElement element,
        final ClickContext context,
        final Cancellable cancellable
    ) {
        CompletableClickHandler clickHandler = ClickHandler.process(element.clickHandler(), context);
        if (!clickHandler.completingLater()) {
            clickHandler.complete();
        }
        cancellable.setCancelled(clickHandler.cancelled());
    }

    private boolean isClickThrottled(final Player player) {
        UUID playerId = player.getUniqueId();
        if (this.clickThrottleCache.getIfPresent(playerId) == null) {
            this.clickThrottleCache.put(playerId, new Object());
            return false;
        }
        return true;
    }

    public Plugin plugin() {
        return this.plugin;
    }

}
