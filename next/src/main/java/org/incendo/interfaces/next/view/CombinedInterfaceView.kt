package org.incendo.interfaces.next.view

import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.incendo.interfaces.next.interfaces.CombinedInterface
import org.incendo.interfaces.next.inventory.CombinedInterfacesInventory
import org.incendo.interfaces.next.pane.CombinedPane
import org.incendo.interfaces.next.utilities.TitleState
import org.incendo.interfaces.next.utilities.runSync

public class CombinedInterfaceView(
    player: Player,
    backing: CombinedInterface
) : InterfaceView<CombinedInterfacesInventory, CombinedPane>(
    player,
    backing
),
    InventoryHolder {
    private val titleState = TitleState(backing.initialTitle)

    override fun createInventory(): CombinedInterfacesInventory = CombinedInterfacesInventory(
        this,
        player,
        titleState.current,
        backing.rows
    )

    override fun openInventory() {
        titleState.hasChanged = false

        runSync {
            player.openInventory(this.inventory)
        }
    }

    override fun requiresPlayerUpdate(): Boolean = !firstPaint && titleState.hasChanged

    override fun requiresNewInventory(): Boolean = firstPaint || titleState.hasChanged

    public override fun getInventory(): Inventory = currentInventory.chestInventory
}
