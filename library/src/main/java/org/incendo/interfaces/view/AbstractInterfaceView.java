package org.incendo.interfaces.view;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.incendo.interfaces.Constants;
import org.incendo.interfaces.interfaces.Interface;
import org.incendo.interfaces.inventory.InterfacesInventory;
import org.incendo.interfaces.pane.CompletedPane;
import org.incendo.interfaces.pane.Pane;
import org.incendo.interfaces.properties.Trigger;
import org.incendo.interfaces.transform.AppliedTransform;
import org.incendo.interfaces.utilities.CollapsablePaneMap;
import org.incendo.interfaces.utilities.ThreadUtils;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public abstract class AbstractInterfaceView<I extends InterfacesInventory, P extends Pane> implements InterfaceView {

    public static final int COLUMNS_IN_CHEST = 9;

    private final Player player;
    private final Interface<P> backing;
    private final InterfaceView parent;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AbstractInterfaceView.class);
    private final Semaphore semaphore = new Semaphore(1);
    private final AtomicInteger queue = new AtomicInteger(0);

    private boolean firstPaint = true;
    private boolean isProcessingClick = false;
    private boolean openIfClosed = false;

    private final Set<AppliedTransform<P>> pendingTransforms = ConcurrentHashMap.newKeySet();
    private final Set<AppliedTransform<P>> debouncedTransforms = ConcurrentHashMap.newKeySet();

    private final CollapsablePaneMap panes;
    private CompletedPane pane;

    private I currentInventory;

    protected AbstractInterfaceView(final Player player, final Interface<P> backing, final InterfaceView parent) {
        this.player = player;
        this.backing = backing;
        this.parent = parent;
        this.panes = CollapsablePaneMap.create(backing.totalRows(), backing.createPane());
    }

    private void setup() {
        Multimap<Trigger, AppliedTransform<P>> triggers = ArrayListMultimap.create();
        this.backing.transforms().forEach(transform -> transform.triggers().forEach(trigger -> triggers.put(trigger, transform)));

        triggers.asMap().forEach((trigger, transforms) -> {
            transforms.forEach(transform -> this.applyTransforms(transforms));
        });

        this.redrawComplete();
    }

    public final void redrawComplete() {
        this.applyTransforms(this.backing.transforms());
    }

    public final void open() {
        this.openIfClosed = true;

        if (this.firstPaint || !(this instanceof ChestInterfaceView)) {
            this.firstPaint = true;
            this.setup();
        } else {
            this.renderAndOpen();
        }
    }

    public void close() {
        if (this.isOpen(this.player)) {
            ThreadUtils.runSync(this.player::closeInventory);
        }
    }

    public final InterfaceView parent() {
        return this.parent;
    }

    public final Player player() {
        return this.player;
    }

    public final CompletedPane pane() {
        return this.pane;
    }

    public final I inventory() {
        return this.currentInventory;
    }

    public final void back() {
        if (this.parent == null) {
            this.close();
        } else {
            this.parent.open();
        }
    }

    public final Interface<P> backing() {
        return this.backing;
    }

    public abstract I createInventory();

    public abstract void openInventory();

    public abstract boolean isOpen(Player player);

    @Override
    public final boolean isProcessingClick() {
        return this.isProcessingClick;
    }

    @Override
    public final void setProcessingClick(final boolean processingClick) {
        this.isProcessingClick = processingClick;
    }

    protected final void renderAndOpen() {
        if (!this.openIfClosed && !this.isOpen(this.player)) {
            return;
        }
        if (this.queue.get() >= 2) {
            return;
        }

        this.queue.incrementAndGet();
        CompletableFuture.runAsync(() -> {
            try {
                //todo: invesigate
                // this.semaphore.acquire();
                this.pane = this.panes.collapse();
                this.renderToInventory(createdNewInventory -> {
                    if (!createdNewInventory && this.requiresPlayerUpdate()) {
                        ThreadUtils.runSync(this.player::updateInventory);
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                //todo: investigate
                // this.semaphore.release();
                this.queue.decrementAndGet();
            }
        }, Constants.EXECUTOR);
    }

    protected final boolean applyTransforms(final Collection<AppliedTransform<P>> transforms) {
        this.debouncedTransforms.removeAll(transforms);
        if (Bukkit.isStopping() || !this.player.isOnline()) {
            return false;
        }

        transforms.forEach(transform -> {
            if (this.pendingTransforms.contains(transform)) {
                this.debouncedTransforms.add(transform);
                return;
            }
            this.pendingTransforms.add(transform);
            CompletableFuture.runAsync(() -> {
                try {
                    if (!Bukkit.isStopping() && this.player.isOnline()) {
                        this.runTransformAndApplyToPanes(transform);
                    }
                } catch (Exception e) {
                    logger.error("Failed to run and apply transform: {}", transform, e);
                } finally {
                    this.pendingTransforms.remove(transform);
                    if (this.debouncedTransforms.contains(transform) && this.applyTransforms(Set.of(transform))) {
                        // Re-run the transform
                    } else {
                        //todo: investigate
                        if (true || this.pendingTransforms.isEmpty()) {
                            this.renderAndOpen();
                        }
                    }
                }
            }, Constants.EXECUTOR);
        });

        //todo: investigate
        if (true || this.pendingTransforms.isEmpty()) {
            CompletableFuture.runAsync(this::renderAndOpen, Constants.EXECUTOR);
        }
        return true;
    }

    private void runTransformAndApplyToPanes(final AppliedTransform<P> transform) throws InterruptedException {
        P pane = this.backing.createPane();
        transform.apply(pane, this);
        CompletedPane completedPane = CompletedPane.complete(pane, this.player);

        this.semaphore.acquire();
        this.panes.put(transform.priority(), completedPane);
        this.semaphore.release();
    }

    protected final void drawPaneToInventory(final boolean drawNormalInventory, final boolean drawPlayerInventory) {
        this.pane.forEach((point, element) -> {
            // We defer drawing of any elements in the player inventory itself
            // for later unless the inventory is already open.
            boolean isPlayerInventory = this.currentInventory.isPlayerInventory(point.x(), point.y());

            if (!isPlayerInventory && drawNormalInventory || isPlayerInventory && drawPlayerInventory) {
                // todo: reimplement this
                // return;
            }

            //todo: should be air not null? probably.
            if (element.itemStack() == null) {
                return;
            }

            this.currentInventory.set(point.x(), point.y(), element.itemStack());
        });
    }

    public final void onOpen() {
        this.drawPaneToInventory(false, true);
    }

    protected boolean requiresNewInventory() {
        return this.firstPaint;
    }

    protected boolean requiresPlayerUpdate() {
        return false;
    }

    protected final void renderToInventory(final Consumer<Boolean> callback) {
        // If a new inventory is required we create one
        // and mark that the current one is not to be used!
        boolean createdInventory;

        if (this.requiresNewInventory()) {
            this.currentInventory = this.createInventory();
            createdInventory = true;
        } else {
            createdInventory = false;
        }

        // Draw the contents of the inventory synchronously because
        // we don't want it to happen in between ticks and show
        // a half-finished inventory.
        ThreadUtils.runSync(() -> {
            // Determine if the inventory is currently open or being opened immediately,
            // otherwise we never draw to player inventories. This ensures lingering
            // updates on menus that have closed do not affect future menus that actually
            // ended up being opened.
            boolean isOpen = this.isOpen(this.player);
            this.drawPaneToInventory(true, isOpen);
            callback.accept(createdInventory);

            if ((this.openIfClosed && !isOpen) || createdInventory) {
                this.openInventory();
                this.openIfClosed = false;
                this.firstPaint = false;
            }
        });
    }
}
