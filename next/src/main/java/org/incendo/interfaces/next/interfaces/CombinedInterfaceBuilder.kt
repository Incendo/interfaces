package org.incendo.interfaces.next.interfaces

import net.kyori.adventure.text.Component
import org.incendo.interfaces.next.pane.CombinedPane

public class CombinedInterfaceBuilder internal constructor() :
    AbstractInterfaceBuilder<CombinedPane, CombinedInterface>() {

    public var rows: Int = 0
    public var initialTitle: Component? = null

    public override fun build(): CombinedInterface = CombinedInterface(
        rows,
        initialTitle,
        transforms,
        clickPreprocessors
    )
}
