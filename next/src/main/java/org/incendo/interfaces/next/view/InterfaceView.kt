package org.incendo.interfaces.next.view

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.incendo.interfaces.next.element.component1
import org.incendo.interfaces.next.interfaces.Interface
import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.update.CompleteUpdate
import org.incendo.interfaces.next.update.TriggerUpdate
import org.incendo.interfaces.next.update.Update
import org.incendo.interfaces.next.utilities.CollapsablePaneMap
import org.incendo.interfaces.next.utilities.gridPointToBukkitIndex

public abstract class InterfaceView<P : Pane>(
    public val player: Player,
    public val backing: Interface<P>
) {

    private companion object {
        private const val COLUMNS = 9
    }

    private var titleChanged = false
    private var title: Component? = backing.initialTitle
        private set(value) {
            titleChanged = true
            field = value
        }

    private lateinit var currentInventory: Inventory

    private val panes = CollapsablePaneMap()
    private lateinit var pane: Pane

    init {
        update(CompleteUpdate, true)

        for (transform in backing.transforms) {
            for (trigger in transform.triggers) {
                trigger.addListener {
                    update(TriggerUpdate(trigger))
                }
            }
        }
    }

    public fun update(update: Update, firstPaint: Boolean = false) {
        update.apply(this)
        pane = panes.collapse()

        val requiresNewInventory = renderToInventory(firstPaint)

        if (requiresNewInventory) {
            open()
        }
    }

    public fun open(): Unit = runSync {
        player.openInventory(currentInventory)
    }

    private fun createInventory(): Inventory {
        val currentTitle = title
        val rows = backing.rows * COLUMNS

        return if (currentTitle != null) {
            Bukkit.createInventory(player, rows, currentTitle)
        } else {
            Bukkit.createInventory(player, rows)
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
        val requiresNewInventory = firstPaint || titleChanged

        if (requiresNewInventory) {
            currentInventory = createInventory()
        }

        pane.forEach { column, row, element ->
            val (itemStack) = element
            val bukkitIndex = gridPointToBukkitIndex(column, row)

            if (currentInventory.getItem(bukkitIndex) != itemStack) {
                currentInventory.setItem(bukkitIndex, itemStack)
            }
        }

        if (titleChanged && !firstPaint) {
            player.updateInventory()
        }

        titleChanged = true

        return requiresNewInventory
    }
}
