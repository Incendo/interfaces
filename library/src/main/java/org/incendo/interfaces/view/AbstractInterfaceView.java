package org.incendo.interfaces.view;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.incendo.interfaces.interfaces.Interface;
import org.incendo.interfaces.inventory.InterfacesInventory;
import org.incendo.interfaces.pane.CompletedPane;
import org.incendo.interfaces.pane.Pane;
import org.incendo.interfaces.transform.AppliedTransform;
import org.incendo.interfaces.utilities.CollapsablePaneMap;
import org.incendo.interfaces.utilities.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collection;

public abstract class AbstractInterfaceView<I extends InterfacesInventory, P extends Pane> implements InterfaceView {

    public static final int COLUMNS_IN_CHEST = 9;
    private static final Logger logger = LoggerFactory.getLogger(AbstractInterfaceView.class);

    private final Player player;
    private final Interface<P> backingInterface;
    private final InterfaceView parentView;

    private final CollapsablePaneMap paneLayers;
    private CompletedPane combinedPane;
    private I inventory;

    private boolean isProcessingClick = false;

    protected AbstractInterfaceView(final Player player, final Interface<P> backingInterface, final InterfaceView parentView) {
        this.player = player;
        this.backingInterface = backingInterface;
        this.parentView = parentView;
        this.paneLayers = CollapsablePaneMap.create(backingInterface.rows());
    }

    public final void open() {
        this.initializeInventory();
        this.openInventory();
        this.startApplyingTransforms();
    }

    private void initializeInventory() {
        if (this.inventory == null) {
            this.inventory = this.createInventory();
        }
    }

    private void startApplyingTransforms() {
        Collection<AppliedTransform<P>> transforms = this.backingInterface.transforms();
        transforms.forEach(this::applyTransformAsync);
        transforms.forEach(this::setupTransformListeners);
    }

    private void applyTransformAsync(final AppliedTransform<P> transform) {
        if (!transform.async()) {
            this.applyTransform(transform);
            return;
        }

        Thread.startVirtualThread(() -> {
            if (Bukkit.isStopping() || !this.player.isOnline()) {
                return;
            }
            try {
                this.applyTransform(transform);
            } catch (Exception e) {
                logger.error("Error applying transform: {}", transform, e);
            }
        });
    }

    private void setupTransformListeners(final AppliedTransform<P> transform) {
        transform.triggers().forEach(trigger -> {
            trigger.addListener(this, (value) -> {
                this.applyTransformAsync(transform);
            });
        });
    }

    private void applyTransform(final AppliedTransform<P> transform) {
        P pane = this.backingInterface.createPane();
        transform.apply(pane, this);
        CompletedPane completedPane = CompletedPane.complete(pane, this.player);
        this.paneLayers.put(transform.priority(), completedPane);
        this.scheduleInventoryUpdate();
    }

    private void scheduleInventoryUpdate() {
        ThreadUtils.runSync(this::updateInventory);
    }

    private void updateInventory() {
        this.combinePanes();
        this.drawCombinedPaneToInventory();
    }

    private void combinePanes() {
        this.combinedPane = this.paneLayers.collapse();
    }

    private void drawCombinedPaneToInventory() {
        this.combinedPane.forEach((point, element) -> {
            if (element.itemStack() != null) {
                this.inventory.set(point.x(), point.y(), element.itemStack());
            }
        });
    }

    public final void close() {
        if (this.isOpen(this.player)) {
            ThreadUtils.runSync(this.player::closeInventory);
        }
    }

    public final void back() {
        if (this.parentView == null) {
            this.close();
        } else {
            this.parentView.open();
        }
    }

    public abstract I createInventory();

    public abstract void openInventory();

    public abstract boolean isOpen(Player player);

    public final InterfaceView parent() {
        return this.parentView;
    }

    public final Player player() {
        return this.player;
    }

    public final CompletedPane pane() {
        return this.combinedPane;
    }

    public final I inventory() {
        return this.inventory;
    }

    public final Interface<P> backing() {
        return this.backingInterface;
    }

    @Override
    public final boolean isProcessingClick() {
        return this.isProcessingClick;
    }

    @Override
    public final void setProcessingClick(final boolean processingClick) {
        this.isProcessingClick = processingClick;
    }
}
