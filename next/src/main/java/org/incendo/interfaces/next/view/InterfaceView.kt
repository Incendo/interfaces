package org.incendo.interfaces.next.view

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.incendo.interfaces.next.interfaces.Interface
import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.update.CompleteUpdate
import org.incendo.interfaces.next.update.TriggerUpdate
import org.incendo.interfaces.next.update.Update
import org.incendo.interfaces.next.utilities.CollapsablePaneMap
import org.incendo.interfaces.next.utilities.TitleState
import org.incendo.interfaces.next.utilities.gridPointToBukkitIndex

public abstract class InterfaceView<P : Pane>(
    public val player: Player,
    public val backing: Interface<P>
) : InventoryHolder {

    private companion object {
        private const val COLUMNS = 9
    }

    private var isOpen = true

    private val titleState = TitleState(backing.initialTitle)
    private val panes = CollapsablePaneMap()

    private lateinit var currentInventory: Inventory
    public lateinit var pane: Pane

    init {
        update(CompleteUpdate, true)

        backing.transforms
            .flatMap(AppliedTransform::triggers)
            .forEach { trigger ->
                trigger.addListener {
                    update(TriggerUpdate(trigger))
                }
            }
    }

    public fun update(update: Update, firstPaint: Boolean = false) {
        update.apply(this)
        pane = panes.collapse()

        val requiresNewInventory = renderToInventory(firstPaint)

        if (!firstPaint && requiresNewInventory && isOpen) {
            openInventory()
        }
    }

    public fun open() {
        isOpen = true
        openInventory()
    }

    public fun close() {
        isOpen = false
    }

    public override fun getInventory(): Inventory = currentInventory

    private fun openInventory(): Unit = runSync {
        player.openInventory(currentInventory)
    }

    private fun createInventory(): Inventory {
        val currentTitle = titleState.current
        val rows = backing.rows * COLUMNS

        return if (currentTitle != null) {
            Bukkit.createInventory(this, rows, currentTitle)
        } else {
            Bukkit.createInventory(this, rows)
        }
    }

    internal fun applyTransforms(transforms: Collection<AppliedTransform>) {
        for (transform in transforms) {
            val pane = backing.createPane()
            transform(pane)

            panes[transform.priority] = pane
        }
    }

    private fun renderToInventory(firstPaint: Boolean = false): Boolean {
        val requiresNewInventory = firstPaint || titleState.hasChanged

        if (requiresNewInventory) {
            currentInventory = createInventory()
        }

        pane.forEach { column, row, element ->
            val itemStack = element.itemStack(player)
            val bukkitIndex = gridPointToBukkitIndex(column, row)

            if (currentInventory.getItem(bukkitIndex) != itemStack) {
                currentInventory.setItem(bukkitIndex, itemStack)
            }
        }

        if (titleState.hasChanged && !firstPaint) {
            player.updateInventory()
        }

        titleState.hasChanged = false

        return requiresNewInventory
    }
}
