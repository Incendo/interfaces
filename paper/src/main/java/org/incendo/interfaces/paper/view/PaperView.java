package org.incendo.interfaces.paper.view;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.PluginClassLoader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.Interface;
import org.incendo.interfaces.core.arguments.InterfaceArguments;
import org.incendo.interfaces.core.element.Element;
import org.incendo.interfaces.core.pane.Pane;
import org.incendo.interfaces.core.transform.InterfaceProperty;
import org.incendo.interfaces.core.transform.InterruptUpdateException;
import org.incendo.interfaces.core.util.Vector2;
import org.incendo.interfaces.core.view.InterfaceView;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.type.ChildTitledInterface;
import org.incendo.interfaces.paper.type.PaperInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class PaperView<I extends PaperInterface<P, PlayerViewer>, P extends Pane, B extends Inventory> implements PlayerView<P> {

    private final Plugin plugin = ((PluginClassLoader) this.getClass().getClassLoader()).getPlugin();

    protected final @NonNull Map<Vector2, Element> current = new HashMap<>();

    protected final PlayerViewer viewer;
    protected final InterfaceArguments arguments;
    protected final I backing;

    protected List<ContextCompletedPane<P>> panes = new ArrayList<>();

    protected P pane;

    protected B inventory;

    protected boolean viewing = true;

    PaperView(
            final PlayerViewer viewer,
            final InterfaceArguments arguments,
            final I backing
    ) {
        this.viewer = viewer;
        this.arguments = arguments;
        this.backing = backing;
    }

    //todo(josh): find another way to do this
    protected void finishConstruction() {
        this.pane = this.updatePane(true);

        this.runSync(() -> {
            this.inventory = this.createInventory();
            this.applyInventory(true);
        });
    }

    @Override
    public final @NonNull I backing() {
        return this.backing;
    }

    @Override
    public final @NonNull InterfaceArguments arguments() {
        return this.arguments;
    }

    @Override
    public final @NonNull PlayerViewer viewer() {
        return this.viewer;
    }


    @Override
    public final void update() {
        if (!this.viewing) {
            return;
        }

        if (!this.viewer.player().isOnline()) {
            return;
        }

        this.backing.updateExecutor().execute(this.plugin, this::actuallyUpdate);
    }

    private void updateByProperty(final @NonNull InterfaceProperty<?> interfaceProperty) {
        try {
            this.pane = this.updatePaneByProperty(interfaceProperty);
        } catch (final InterruptUpdateException ignored) {
            return;
        }
        this.reApplySync();
    }

    private P updatePaneByProperty(final @NonNull InterfaceProperty<?> interfaceProperty) {
        List<ContextCompletedPane<P>> updatedPanes = new ArrayList<>(this.panes);

        for (final var transform : this.backing.transformations()) {
            if (!transform.properties().contains(interfaceProperty)) {
                continue;
            }

            P newPane = transform.transform().apply(this.createPane(), this);

            updatedPanes.removeIf(completedPane -> completedPane.context().equals(transform));
            updatedPanes.add(new ContextCompletedPane<>(transform, newPane));
        }

        this.panes = updatedPanes;
        return this.mergePanes();
    }


    private void actuallyUpdate() {
        try {
            this.pane = this.updatePane(false);
        } catch (final InterruptUpdateException ignored) {
            return;
        }

        this.reApplySync();
    }

    private void reApplySync() {
        this.runSync(() -> {
            this.applyInventory(false);
        });
    }

    protected final @NonNull P updatePane(final boolean firstApply) {
        for (final var transform : this.backing.transformations()) {
            P newPane = transform.transform().apply(this.createPane(), this);
            // If it's the first time we apply the transform, then
            // we add update listeners to all the dependent properties
            if (firstApply) {
                for (final InterfaceProperty<?> property : transform.properties()) {
                    property.addListener((oldValue, newValue) -> this.updateByProperty(property));
                }
            }

            this.panes.removeIf(completedPane -> completedPane.context().equals(transform));
            this.panes.add(new ContextCompletedPane<>(transform, newPane));
        }

        return this.mergePanes();
    }

    private void runSync(final Runnable runnable) {
        if (Bukkit.isPrimaryThread()) {
            runnable.run();
        } else {
            Bukkit.getScheduler().callSyncMethod(this.plugin, () -> {
                runnable.run();
                return null;
            });
        }
    }

    @Override
    public final @NonNull <C extends PlayerView<?>> C openChild(
            final @NonNull Interface<?, PlayerViewer> backing,
            final @NonNull InterfaceArguments argument
    ) {
        InterfaceView<?, PlayerViewer> view = backing.open(this, argument);

        @SuppressWarnings("unchecked")
        C typedView = (C) view;
        return typedView;
    }

    @Override
    public final <C extends PlayerView<?>> @NonNull C openChild(
            @NonNull final ChildTitledInterface<?, PlayerViewer> backing,
            @NonNull final InterfaceArguments argument,
            @NonNull final Component title
    ) {
        InterfaceView<?, PlayerViewer> view = backing.open(this, argument, title);

        @SuppressWarnings("unchecked")
        C typedView = (C) view;
        return typedView;
    }

    protected abstract void applyInventory(boolean firstApply);

    protected abstract P createPane();

    protected abstract B createInventory();

    protected abstract P mergePanes();

}
