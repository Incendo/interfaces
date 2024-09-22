package org.incendo.interfaces.view;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.incendo.interfaces.interfaces.ChestInterface;
import org.incendo.interfaces.pane.ChestPane;
import org.incendo.interfaces.inventory.ChestInterfacesInventory;
import org.incendo.interfaces.utilities.ThreadUtils;
import org.incendo.interfaces.utilities.TitleState;
import org.jetbrains.annotations.NotNull;

public final class ChestInterfaceView extends AbstractInterfaceView<ChestInterfacesInventory, ChestPane> implements InventoryHolder {

    private final TitleState titleState;

    public ChestInterfaceView(final Player player, final ChestInterface backing, final InterfaceView parent) {
        super(player, backing, parent);
        this.titleState = new TitleState(backing.initialTitle());
    }

    public void title(final Component value) {
        this.titleState.current(value);
    }

    @Override
    public ChestInterfacesInventory createInventory() {
        return new ChestInterfacesInventory(this, this.titleState.current(), this.backing().rows());
    }

    @Override
    public void openInventory() {
        ThreadUtils.runSync(() -> this.player().openInventory(this.getInventory()));
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory().chestInventory();
    }

    @Override
    public boolean isOpen(final Player player) {
        return player.getOpenInventory().getTopInventory().getHolder() == this;
    }
}
