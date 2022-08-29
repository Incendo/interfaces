package org.incendo.interfaces.next.view

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
import org.incendo.interfaces.next.utilities.gridPointToBukkitIndex

public abstract class InterfaceView<P : Pane>(
    public val player: Player,
    public val backing: Interface<P>
) : InventoryHolder {

    public companion object {
        public const val COLUMNS_IN_CHEST: Int = 9
    }

    protected var firstPaint: Boolean = true

    // todo(josh): reduce internal abuse?
    internal var isProcessingClick = false
    internal var isOpen = true

    private val panes = CollapsablePaneMap()

    private lateinit var currentInventory: Inventory
    public lateinit var pane: Pane

    init {
        update(CompleteUpdate)

        backing.transforms
            .flatMap(AppliedTransform<P>::triggers)
            .forEach { trigger ->
                trigger.addListener {
                    update(TriggerUpdate(trigger))
                }
            }
    }

    public fun update(update: Update) {
        update.apply(this)
        pane = panes.collapse()
    }

    private fun renderAndOpen() {
        val requiresNewInventory = renderToInventory()

        if (requiresNewInventory && isOpen) {
            openInventory()
        }

        firstPaint = false
    }

    public fun open() {
        isOpen = true
        renderAndOpen()
    }

    public fun close() {
        isOpen = false
    }

    public override fun getInventory(): Inventory = currentInventory

    public abstract fun createInventory(): Inventory

    public abstract fun openInventory()

    internal fun applyTransforms(transforms: Collection<AppliedTransform<P>>) {
        for (transform in transforms) {
            val pane = backing.createPane()
            transform(pane)

            panes[transform.priority] = pane
        }
    }

    private fun drawPaneToInventory() {
        pane.forEach { column, row, element ->
            val itemStack = element.drawable().draw(player)
            val bukkitIndex = gridPointToBukkitIndex(column, row)

            if (currentInventory.getItem(bukkitIndex) != itemStack) {
                currentInventory.setItem(bukkitIndex, itemStack)
            }
        }
    }

    protected open fun requiresNewInventory(): Boolean = firstPaint

    protected open fun requiresPlayerUpdate(): Boolean = false

    protected open fun renderToInventory(): Boolean {
        val requiresNewInventory = requiresNewInventory()

        if (requiresNewInventory) {
            currentInventory = createInventory()
        }

        drawPaneToInventory()

        if (requiresPlayerUpdate()) {
            player.updateInventory()
        }

        return requiresNewInventory
    }
}
