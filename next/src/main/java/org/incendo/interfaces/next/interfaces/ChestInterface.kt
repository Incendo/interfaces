package org.incendo.interfaces.next.interfaces

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.incendo.interfaces.next.pane.ChestPane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.view.ChestInterfaceView
import org.incendo.interfaces.next.view.InterfaceView

public class ChestInterface internal constructor(
    override val rows: Int,
    override val transforms: Collection<AppliedTransform>,
    override val initialTitle: Component?
) : Interface<ChestPane> {

    public companion object {
        public const val NUMBER_OF_COLUMNS: Int = 9
    }

    override fun createPane(): ChestPane = ChestPane()

    override fun open(player: Player): InterfaceView<ChestPane> {
        val view = ChestInterfaceView(player, this)
        view.open()

        return view
    }
}
