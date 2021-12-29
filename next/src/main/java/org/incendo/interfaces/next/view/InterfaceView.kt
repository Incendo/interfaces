package org.incendo.interfaces.next.view

import net.kyori.adventure.text.Component
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.incendo.interfaces.next.element.component1
import org.incendo.interfaces.next.element.component2
import org.incendo.interfaces.next.interfaces.Interface
import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.update.CompleteUpdate
import org.incendo.interfaces.next.update.Update
import org.incendo.interfaces.next.utilities.CollapsablePaneMap
import org.incendo.interfaces.next.utilities.gridPointToBukkitIndex

public abstract class InterfaceView<P : Pane>(
    public val backing: Interface<P>
) {

    private var titleChanged = false
    private var title = backing.initialTitle
        private set(value) {
            titleChanged = true
            field = value
        }

    private lateinit var currentInventory: Inventory

    private val panes = CollapsablePaneMap()
    private val pane: Pane

    init {
        update(CompleteUpdate)
        pane = panes.collapse()
    }

    internal abstract fun createInventory(): Inventory

    public fun update(update: Update) {
        update.apply(this)
    }

    internal fun applyTransforms(transforms: Collection<AppliedTransform>) {
        for (transform in transforms) {
            val pane = backing.createPane()
            transform(pane)

            panes[transform.priority] = pane
        }
    }

    public fun renderToInventory(firstPaint: Boolean = false) {
        if (firstPaint || titleChanged) {
            currentInventory = createInventory()
        }

        pane.forEach { column, row, element ->
            val (itemStack, clickHandler) = element
            val bukkitIndex = gridPointToBukkitIndex(column, row)

            if (currentInventory.getItem(bukkitIndex) != itemStack) {
                currentInventory.setItem(bukkitIndex, itemStack)
            }
        }

        if (titleChanged && !firstPaint) {
            //todo: update player inventory
        }
    }

}
