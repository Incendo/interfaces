package org.incendo.interfaces.view;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.incendo.interfaces.interfaces.Interface;
import org.incendo.interfaces.inventory.InterfacesInventory;
import org.incendo.interfaces.pane.Pane;
import org.incendo.interfaces.utilities.CollapsablePaneMap;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public abstract class AbstractInterfaceView<I extends InterfacesInventory, P extends Pane> implements InterfaceView {

    public static final int COLUMNS_IN_CHEST = 9;

    protected Player player;
    protected Interface<P> backing;
    protected InterfaceView parent;

    private final Logger logger = LoggerFactory.getLogger(AbstractInterfaceView.class);
    private final Semaphore semaphore = new Semaphore(1);
    private final AtomicInteger queue = new AtomicInteger(0);

    protected boolean firstPaint = true;
    protected boolean isProcessingClick = false;

    private CollapsablePaneMap panes;
    protected CompletedPane pane;

    protected I currentInventory;

    public AbstractInterfaceView(Player player, Interface<P> backing, InterfaceView parent) {
        this.player = player;
        this.backing = backing;
        this.parent = parent;
        this.panes = CollapsablePaneMap.create(backing.totalRows(), backing.createPane());
    }

    public void setup() {
        backing.getTransforms().stream()
                .flatMap(AppliedTransform<P>::getTriggers)
                .forEach(trigger -> {
                    trigger.addListener(this, () -> {
                        if (!firstPaint) {
                            TriggerUpdate(trigger).apply(this);
                        }
                    });
                });
        CompleteUpdate.apply(this);
    }

    @Override
    public void open() {
        if (firstPaint || overlapsPlayerInventory()) {
            firstPaint = true;
            setup();
            firstPaint = false;
        } else {
            renderAndOpen(true);
        }
    }

    @Override
    public void close() {
        if (isOpen(player)) {
            player.closeInventory();
        }
    }

    public abstract I createInventory();
    public abstract void openInventory();
    public abstract boolean isOpen(Player player);

    protected void renderAndOpen(boolean openIfClosed) {
        if (!openIfClosed && !isOpen(player)) return;

        if (queue.get() >= 2) return;

        queue.incrementAndGet();
        semaphore.acquireUninterruptibly();
        try {
            pane = panes.collapse();
            renderToInventory(openIfClosed, createdNewInventory -> {
                if (!createdNewInventory && requiresPlayerUpdate()) {
                    player.updateInventory();
                }
            });
        } finally {
            semaphore.release();
            queue.decrementAndGet();
        }
    }
}
