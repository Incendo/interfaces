package org.incendo.interfaces.next.interfaces

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.incendo.interfaces.next.click.ClickHandler
import org.incendo.interfaces.next.pane.CombinedPane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.view.CombinedInterfaceView
import org.incendo.interfaces.next.view.InterfaceView

public class CombinedInterface internal constructor(
    override val rows: Int,
    override val initialTitle: Component?,
    override val closeHandlers: MutableMap<InventoryCloseEvent.Reason, CloseHandler>,
    override val transforms: Collection<AppliedTransform<CombinedPane>>,
    override val clickPreprocessors: Collection<ClickHandler>,
    override val itemPostProcessor: ((ItemStack) -> Unit)?
) : Interface<CombinedPane>, TitledInterface {

    override fun totalRows(): Int = rows + 4

    override fun createPane(): CombinedPane = CombinedPane(rows)

    override suspend fun open(player: Player, parent: InterfaceView?): CombinedInterfaceView {
        val view = CombinedInterfaceView(player, this, parent)
        view.open()

        return view
    }
}
