package org.incendo.interfaces.next.view

import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.incendo.interfaces.next.Constants.SCOPE
import org.incendo.interfaces.next.interfaces.Interface
import org.incendo.interfaces.next.inventory.InterfacesInventory
import org.incendo.interfaces.next.pane.CompletedPane
import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.pane.complete
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.update.CompleteUpdate
import org.incendo.interfaces.next.update.TriggerUpdate
import org.incendo.interfaces.next.utilities.CollapsablePaneMap

public abstract class AbstractInterfaceView<I : InterfacesInventory, P : Pane>(
    public val player: Player,
    public val backing: Interface<P>,
    private val parent: InterfaceView?
) : InterfaceView {

    public companion object {
        public const val COLUMNS_IN_CHEST: Int = 9
    }

    protected var firstPaint: Boolean = true

    // todo(josh): reduce internal abuse?
    internal var isProcessingClick = false
    private var isOpen = true

    private val panes = CollapsablePaneMap.create()
    internal lateinit var pane: CompletedPane

    protected lateinit var currentInventory: I

    internal suspend fun setup() {
        CompleteUpdate.apply(this)

        backing.transforms
            .flatMap(AppliedTransform<P>::triggers)
            .forEach { trigger ->
                trigger.addListener {
                    SCOPE.launch {
                        TriggerUpdate(trigger).apply(this@AbstractInterfaceView)
                    }
                }
            }
    }

    public override fun open() {
        isOpen = true
        renderAndOpen()
    }

    public override fun close() {
        isOpen = false
    }

    public override fun back() {
        close()
        parent?.open()
    }

    public abstract fun createInventory(): I

    public abstract fun openInventory()

    private fun renderAndOpen() {
        pane = panes.collapse()
        val requiresNewInventory = renderToInventory()

        if (requiresNewInventory && isOpen) {
            openInventory()
        }

        firstPaint = false
    }

    internal suspend fun applyTransforms(transforms: Collection<AppliedTransform<P>>) {
        // todo(josh): could be improved? make sure renderAndOpen only happens once per tick?
        transforms.forEach { transform ->
            SCOPE.launch {
                val pane = backing.createPane()
                transform(pane, this@AbstractInterfaceView)

                panes[transform.priority] = pane.complete(player)
            }.invokeOnCompletion {
                renderAndOpen()
            }
        }
    }

    private fun drawPaneToInventory() {
        pane.forEach { row, column, element ->
            currentInventory.set(row, column, element.itemStack)
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
