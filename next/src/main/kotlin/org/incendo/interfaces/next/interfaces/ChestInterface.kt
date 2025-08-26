package org.incendo.interfaces.next.interfaces

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.incendo.interfaces.next.click.ClickHandler
import org.incendo.interfaces.next.pane.ChestPane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.view.ChestInterfaceView
import org.incendo.interfaces.next.view.InterfaceView

public class ChestInterface internal constructor(
    override val rows: Int,
    override val initialTitle: Component?,
    override val closeHandlers: MutableMap<InventoryCloseEvent.Reason, CloseHandler>,
    override val transforms: Collection<AppliedTransform<ChestPane>>,
    override val clickPreprocessors: Collection<ClickHandler>,
    override val itemPostProcessor: ((ItemStack) -> Unit)?,
) : Interface<ChestPane>,
    TitledInterface {
    public companion object {
        public const val NUMBER_OF_COLUMNS: Int = 9
    }

    override fun createPane(): ChestPane = ChestPane()

    override suspend fun open(
        player: Player,
        parent: InterfaceView?,
    ): ChestInterfaceView {
        val view = ChestInterfaceView(player, this, parent)
        view.open()

        return view
    }
}
