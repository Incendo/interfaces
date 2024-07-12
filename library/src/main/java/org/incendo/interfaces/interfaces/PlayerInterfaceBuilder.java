package org.incendo.interfaces.interfaces;

import org.incendo.interfaces.pane.PlayerPane;

public final class PlayerInterfaceBuilder extends AbstractInterfaceBuilder<PlayerPane, PlayerInterface> {

    @Override
    public PlayerInterface build() {
        return new PlayerInterface(
            this.closeHandlers(),
            this.transforms(),
            this.clickPreprocessors()
        );
    }

}
