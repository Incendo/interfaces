package org.incendo.interfaces.next.interfaces

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.incendo.interfaces.next.click.ClickHandler
import org.incendo.interfaces.next.pane.CombinedPane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.utilities.currentOpenInterface
import org.incendo.interfaces.next.view.CombinedInterfaceView

public class CombinedInterface internal constructor(
    override val rows: Int,
    override val initialTitle: Component?,
    override val transforms: Collection<AppliedTransform<CombinedPane>>,
    override val clickPreprocessors: Collection<ClickHandler>
) : Interface<CombinedPane>, TitledInterface {

    override fun createPane(): CombinedPane {
        return CombinedPane(rows)
    }

    override suspend fun open(player: Player): CombinedInterfaceView {
        val view = CombinedInterfaceView(player, this, currentOpenInterface(player))
        view.setup()
        view.open()

        return view
    }
}
