package org.incendo.interfaces.next.interfaces

import org.incendo.interfaces.next.pane.PlayerPane

public class PlayerInterfaceBuilder internal constructor() : AbstractInterfaceBuilder<PlayerPane, PlayerInterface>() {

    public override fun build(): PlayerInterface = PlayerInterface(
        null,
        transforms,
        clickPreprocessors
    )
}
