package org.incendo.interfaces.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.incendo.interfaces.click.ClickHandler;
import org.incendo.interfaces.pane.PlayerPane;
import org.incendo.interfaces.transform.AppliedTransform;
import org.incendo.interfaces.view.InterfaceView;
import org.incendo.interfaces.view.PlayerInterfaceView;

import java.util.Collection;
import java.util.Map;

public record PlayerInterface(
    Map<InventoryCloseEvent.Reason, CloseHandler> closeHandlers,
    Collection<AppliedTransform<PlayerPane>> transforms,
    Collection<ClickHandler> clickPreprocessors
) implements Interface<PlayerPane> {

    public static final int NUMBER_OF_COLUMNS = 9;

    @Override
    public int rows() {
        return 4;
    }

    @Override
    public PlayerPane createPane() {
        return new PlayerPane();
    }

    @Override
    public PlayerInterfaceView open(final Player player, final InterfaceView parent) {
        PlayerInterfaceView view = new PlayerInterfaceView(player, this, parent);
        view.open();

        return view;
    }

}
