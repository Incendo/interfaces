package org.incendo.interfaces.next.interfaces

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.incendo.interfaces.next.click.ClickHandler
import org.incendo.interfaces.next.element.Element
import org.incendo.interfaces.next.pane.CombinedPane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.view.CombinedInterfaceView
import org.incendo.interfaces.next.view.InterfaceView

public class CombinedInterface internal constructor(
    override val rows: Int,
    override val initialTitle: Component?,
    override val transforms: Collection<AppliedTransform<CombinedPane>>,
    override val clickPreprocessors: Collection<ClickHandler>
) : Interface<CombinedPane>, TitledInterface {

    override fun createPane(element: Element?): CombinedPane {
        val pane = CombinedPane(rows)

        if (element != null) {
            pane.fill(element)
        }

        return pane
    }

    override suspend fun open(player: Player, parent: InterfaceView?): CombinedInterfaceView {
        val view = CombinedInterfaceView(player, this, parent)
        view.setup()
        view.open()

        return view
    }
}
