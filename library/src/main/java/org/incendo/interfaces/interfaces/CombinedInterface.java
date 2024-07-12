package org.incendo.interfaces.interfaces;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.incendo.interfaces.click.ClickHandler;
import org.incendo.interfaces.pane.CombinedPane;
import org.incendo.interfaces.transform.AppliedTransform;
import org.incendo.interfaces.view.CombinedInterfaceView;
import org.incendo.interfaces.view.InterfaceView;

import java.util.Collection;
import java.util.Map;

public record CombinedInterface(
    int rows,
    Component initialTitle,
    Map<InventoryCloseEvent.Reason, CloseHandler> closeHandlers,
    Collection<AppliedTransform<CombinedPane>> transforms,
    Collection<ClickHandler> clickPreprocessors
) implements Interface<CombinedPane>, TitledInterface {

    @Override
    public int totalRows() {
        return this.rows + 4;
    }

    @Override
    public CombinedPane createPane() {
        return new CombinedPane(this.rows);
    }

    @Override
    public InterfaceView open(final Player player, final InterfaceView parent) {
        CombinedInterfaceView view = new CombinedInterfaceView(player, this, parent);
        view.open();

        return view;
    }
}
