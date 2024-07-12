package org.incendo.interfaces.view;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.incendo.interfaces.InterfacesListeners;
import org.incendo.interfaces.interfaces.PlayerInterface;
import org.incendo.interfaces.inventory.PlayerInterfacesInventory;
import org.incendo.interfaces.pane.PlayerPane;
import org.incendo.interfaces.utilities.ThreadUtils;

public final class PlayerInterfaceView extends AbstractInterfaceView<PlayerInterfacesInventory, PlayerPane> {

    public PlayerInterfaceView(final Player player, final PlayerInterface backing, final InterfaceView parent) {
        super(player, backing, parent);
    }

    @Override
    public void title(final Component value) {
        throw new UnsupportedOperationException("PlayerInventoryView's cannot have a title");
    }

    @Override
    public PlayerInterfacesInventory createInventory() {
        return new PlayerInterfacesInventory(this.player());
    }

    @Override
    public void openInventory() {
        // Close whatever inventory the player has open so they can look at their normal inventory!
        // This will only continue if the menu hasn't been closed yet.
        if (!this.isOpen(this.player())) {
            // First we close then we set the interface so we don't double open!
            InterfacesListeners.instance().setOpenInterface(this.player().getUniqueId(), null);
            this.player().closeInventory();
            InterfacesListeners.instance().setOpenInterface(this.player().getUniqueId(), this);
        }

        // Double-check that this inventory is open now!
        if (this.isOpen(this.player())) {
            // Clear the player's inventory!
            this.player().getInventory().clear();
            if (this.player().getOpenInventory().getTopInventory().getType() == InventoryType.CRAFTING) {
                this.player().getOpenInventory().getTopInventory().clear();
            }
            this.player().getOpenInventory().setCursor(null);

            // Trigger onOpen manually because there is no real inventory being opened,
            // this will also re-draw the player inventory parts!
            onOpen();
        }
    }

    @Override
    public void close() {
        // Ensure we update the interface state in the main thread!
        // Even if the menu is not currently on the screen.
        ThreadUtils.runSync(() -> {
            InterfacesListeners.instance().setOpenInterface(this.player().getUniqueId(), null);
        });
    }

    @Override
    public boolean isOpen(final Player player) {
        return player.getOpenInventory().getType() == InventoryType.CRAFTING
            && InterfacesListeners.instance().getOpenInterface(player.getUniqueId()) == this;
    }

}
