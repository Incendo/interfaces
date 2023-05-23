package org.incendo.interfaces.next.interfaces

import org.incendo.interfaces.next.pane.PlayerPane

public class PlayerInterfaceBuilder :
    AbstractInterfaceBuilder<PlayerPane, PlayerInterface>() {

    public override fun build(): PlayerInterface = PlayerInterface(
        closeHandlers,
        transforms,
        clickPreprocessors
    )
}
