package org.incendo.interfaces.next.interfaces

import org.bukkit.entity.Player
import org.incendo.interfaces.next.click.ClickHandler
import org.incendo.interfaces.next.pane.PlayerPane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.view.InterfaceView
import org.incendo.interfaces.next.view.PlayerInterfaceView

public class PlayerInterface internal constructor(
    override val transforms: Collection<AppliedTransform<PlayerPane>>,
    override val clickPreprocessors: Collection<ClickHandler>
) : Interface<PlayerPane> {

    public companion object {
        public const val NUMBER_OF_COLUMNS: Int = 9
    }

    override val rows: Int = 4

    override fun createPane(): PlayerPane = PlayerPane()

    override fun open(player: Player): InterfaceView<PlayerPane> {
        val view = PlayerInterfaceView(player, this)
        view.open()

        return view
    }
}
