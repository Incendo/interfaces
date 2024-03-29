package org.incendo.interfaces.next.interfaces

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.incendo.interfaces.next.click.ClickHandler
import org.incendo.interfaces.next.pane.PlayerPane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.view.InterfaceView
import org.incendo.interfaces.next.view.PlayerInterfaceView

public class PlayerInterface internal constructor(
    override val closeHandlers: MutableMap<InventoryCloseEvent.Reason, CloseHandler>,
    override val transforms: Collection<AppliedTransform<PlayerPane>>,
    override val clickPreprocessors: Collection<ClickHandler>,
    override val itemPostProcessor: ((ItemStack) -> Unit)?
) : Interface<PlayerPane> {

    public companion object {
        public const val NUMBER_OF_COLUMNS: Int = 9
    }

    override val rows: Int = 4

    override fun createPane(): PlayerPane = PlayerPane()

    override suspend fun open(player: Player, parent: InterfaceView?): PlayerInterfaceView {
        val view = PlayerInterfaceView(player, this)
        view.open()

        return view
    }
}
