package org.incendo.interfaces.next.interfaces

import net.kyori.adventure.text.Component
import org.incendo.interfaces.next.pane.CombinedPane

public class CombinedInterfaceBuilder : AbstractInterfaceBuilder<CombinedPane, CombinedInterface>() {
    public var rows: Int = 0
    public var initialTitle: Component? = null

    override fun build(): CombinedInterface =
        CombinedInterface(
            rows,
            initialTitle,
            closeHandlers,
            transforms,
            clickPreprocessors,
            itemPostProcessor,
        )
}
