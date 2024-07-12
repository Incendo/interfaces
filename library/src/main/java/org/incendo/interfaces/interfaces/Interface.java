package org.incendo.interfaces.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.incendo.interfaces.click.ClickHandler;
import org.incendo.interfaces.transform.AppliedTransform;
import org.incendo.interfaces.pane.Pane;
import org.incendo.interfaces.view.InterfaceView;

import java.util.Collection;
import java.util.Map;

public interface Interface<P extends Pane> {

    int rows();

    Map<InventoryCloseEvent.Reason, CloseHandler> closeHandlers();

    Collection<AppliedTransform<P>> transforms();

    Collection<ClickHandler> clickPreprocessors();

    default int totalRows() {
        return this.rows();
    }

    P createPane();

    InterfaceView open(Player player, InterfaceView parent);
}
