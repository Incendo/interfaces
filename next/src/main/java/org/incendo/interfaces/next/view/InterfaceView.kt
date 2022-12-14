package org.incendo.interfaces.next.view

import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.bukkit.entity.Player
import org.incendo.interfaces.next.Constants.SCOPE
import org.incendo.interfaces.next.interfaces.Interface
import org.incendo.interfaces.next.inventory.InterfacesInventory
import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.update.CompleteUpdate
import org.incendo.interfaces.next.update.TriggerUpdate
import org.incendo.interfaces.next.update.Update
import org.incendo.interfaces.next.utilities.CollapsablePaneMap

public abstract class InterfaceView<I : InterfacesInventory, P : Pane>(
    public val player: Player,
    public val backing: Interface<P>
) {

    public companion object {
        public const val COLUMNS_IN_CHEST: Int = 9
    }

    protected var firstPaint: Boolean = true

    // todo(josh): reduce internal abuse?
    internal var isProcessingClick = false
    internal var isOpen = true

    private val panes = CollapsablePaneMap()
    internal lateinit var pane: Pane

    protected lateinit var currentInventory: I

    internal suspend fun setup() {
        applyUpdate(CompleteUpdate)

        backing.transforms
            .flatMap(AppliedTransform<P>::triggers)
            .forEach { trigger ->
                trigger.addListener {
                    SCOPE.launch {
                        applyUpdate(TriggerUpdate(trigger))
                    }
                }
            }
    }

    private suspend fun applyUpdate(update: Update) {
        update.apply(this)
    }

    private fun renderAndOpen() {
        pane = panes.collapse()
        val requiresNewInventory = renderToInventory()

        if (requiresNewInventory && isOpen) {
            openInventory()
        }

        firstPaint = false
    }

    public suspend fun open() {
        isOpen = true
        renderAndOpen()
    }

    public fun close() {
        isOpen = false
    }

    public abstract fun createInventory(): I

    public abstract fun openInventory()

    internal suspend fun applyTransforms(transforms: Collection<AppliedTransform<P>>) {
        // todo(josh): could be improved? make sure renderAndOpen only happens once per tick?
        transforms.forEach { transform ->
            SCOPE.launch {
                val pane = backing.createPane()
                transform(pane)

                panes[transform.priority] = pane
            }.invokeOnCompletion {
                renderAndOpen()
            }
        }
    }

    private fun drawPaneToInventory() {
        pane.forEach { row, column, element ->
            val itemStack = element.drawable().draw(player)
            currentInventory.set(row, column, itemStack)
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
