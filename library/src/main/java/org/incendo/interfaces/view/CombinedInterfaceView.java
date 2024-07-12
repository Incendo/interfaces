package org.incendo.interfaces.view;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.incendo.interfaces.interfaces.CombinedInterface;
import org.incendo.interfaces.inventory.CombinedInterfacesInventory;
import org.incendo.interfaces.pane.CombinedPane;
import org.incendo.interfaces.utilities.TitleState;

public final class CombinedInterfaceView
    extends AbstractInterfaceView<CombinedInterfacesInventory, CombinedPane> implements InventoryHolder {

    private final TitleState titleState;

    public CombinedInterfaceView(final Player player, final CombinedInterface backing, final InterfaceView parent) {
        super(player, backing, parent);
        this.titleState = new TitleState(backing.initialTitle());
    }

    @Override
    public void title(final Component value) {
        this.titleState.current(value);
    }

    @Override
    public CombinedInterfacesInventory createInventory() {
        return new CombinedInterfacesInventory(this, this.player(), this.titleState.current(), this.backing().rows());
    }

    @Override
    public void openInventory() {
        this.player().openInventory(this.getInventory());
    }

    @Override
    public boolean requiresPlayerUpdate() {
        return false;
    }

    @Override
    public boolean requiresNewInventory() {
        return this.titleState.hasChanged();
    }

    @Override
    public Inventory getInventory() {
        return this.inventory().chestInventory();
    }

    @Override
    public boolean isOpen(final Player player) {
        return player.getOpenInventory().getTopInventory().getHolder() == this;
    }
}
