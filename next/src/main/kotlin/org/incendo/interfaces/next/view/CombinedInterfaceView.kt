package org.incendo.interfaces.next.view

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.incendo.interfaces.next.interfaces.CombinedInterface
import org.incendo.interfaces.next.inventory.CombinedInterfacesInventory
import org.incendo.interfaces.next.pane.CombinedPane
import org.incendo.interfaces.next.utilities.TitleState
import org.incendo.interfaces.next.utilities.runSync

public class CombinedInterfaceView internal constructor(
    player: Player,
    backing: CombinedInterface,
    parent: InterfaceView?,
) : AbstractInterfaceView<CombinedInterfacesInventory, CombinedPane>(
    player,
    backing,
    parent,
),
    InventoryHolder {
    private val titleState = TitleState(backing.initialTitle)

    override fun title(value: Component) {
        titleState.current = value
    }

    override fun createInventory(): CombinedInterfacesInventory = CombinedInterfacesInventory(
        this,
        player,
        titleState.current,
        backing.rows,
    )

    override fun openInventory() {
        runSync {
            player.openInventory(this.inventory)
        }
    }

    override fun requiresPlayerUpdate(): Boolean = !firstPaint && titleState.hasChanged

    override fun requiresNewInventory(): Boolean = firstPaint || titleState.hasChanged

    override fun getInventory(): Inventory = currentInventory.chestInventory

    override fun isOpen(player: Player): Boolean {
        return player.openInventory.topInventory.holder == this
    }
}
