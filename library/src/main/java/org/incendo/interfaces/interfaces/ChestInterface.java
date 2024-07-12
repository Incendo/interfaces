package org.incendo.interfaces.interfaces;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.incendo.interfaces.click.ClickHandler;
import org.incendo.interfaces.pane.ChestPane;
import org.incendo.interfaces.transform.AppliedTransform;
import org.incendo.interfaces.view.ChestInterfaceView;
import org.incendo.interfaces.view.InterfaceView;

import java.util.Collection;
import java.util.Map;

public record ChestInterface(
    int rows,
    Component initialTitle,
    Map<InventoryCloseEvent.Reason, CloseHandler> closeHandlers,
    Collection<AppliedTransform<ChestPane>> transforms,
    Collection<ClickHandler> clickPreprocessors
) implements Interface<ChestPane>, TitledInterface {

    public static final int NUMBER_OF_COLUMNS = 9;

    @Override
    public ChestPane createPane() {
        return new ChestPane();
    }

    public ChestInterfaceView open(final Player player, final InterfaceView parent) {
        ChestInterfaceView view = new ChestInterfaceView(player, this, parent);
        view.open();
        return view;
    }
}
