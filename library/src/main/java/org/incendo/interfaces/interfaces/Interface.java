package org.incendo.interfaces.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.incendo.interfaces.InterfacesListeners;
import org.incendo.interfaces.click.ClickHandler;
import org.incendo.interfaces.next.transform.AppliedTransform;
import org.incendo.interfaces.pane.Pane;
import org.incendo.interfaces.view.InterfaceView;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public interface Interface<P extends Pane> {

    int rows();

    Map<InventoryCloseEvent.Reason, CloseHandler> closeHandlers();

    Collection<AppliedTransform<P>> transforms();

    Collection<ClickHandler> clickPreprocessors();

    Consumer<ItemStack> itemPostProcessor();

    default int totalRows() {
        return this.rows();
    }

    P createPane();

    InterfaceView open(Player player, InterfaceView parent);
}
