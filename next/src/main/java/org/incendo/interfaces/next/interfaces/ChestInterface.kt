package org.incendo.interfaces.next.interfaces

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.incendo.interfaces.next.click.ClickHandler
import org.incendo.interfaces.next.pane.ChestPane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.utilities.currentOpenInterface
import org.incendo.interfaces.next.view.ChestInterfaceView

public class ChestInterface internal constructor(
    override val rows: Int,
    override val initialTitle: Component?,
    override val transforms: Collection<AppliedTransform<ChestPane>>,
    override val clickPreprocessors: Collection<ClickHandler>
) : Interface<ChestPane>, TitledInterface {

    public companion object {
        public const val NUMBER_OF_COLUMNS: Int = 9
    }

    override fun createPane(): ChestPane = ChestPane()

    override suspend fun open(player: Player): ChestInterfaceView {
        val view = ChestInterfaceView(player, this, currentOpenInterface(player))
        view.setup()
        view.open()

        return view
    }
}
