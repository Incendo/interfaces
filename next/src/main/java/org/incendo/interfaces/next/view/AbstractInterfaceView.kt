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
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

public abstract class AbstractInterfaceView<I : InterfacesInventory, P : Pane>(
    public val player: Player,
    public val backing: Interface<P>,
    private val parent: InterfaceView?
) : InterfaceView {

    public companion object {
        public const val COLUMNS_IN_CHEST: Int = 9
    }

    private val lock = ReentrantLock()

    protected var firstPaint: Boolean = true

    // todo(josh): reduce internal abuse?
    internal var isProcessingClick = false

    private val panes = CollapsablePaneMap.create(backing.totalRows(), backing.createPane())
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
        renderAndOpen(forceOpen = true)
    }

    public override fun close() {
        if (player.openInventory.topInventory.holder == this) {
            player.closeInventory()
        }
    }

    public override fun parent(): InterfaceView? {
        return parent
    }

    public override fun back() {
        close()
        parent?.open()
    }

    public abstract fun createInventory(): I

    public abstract fun openInventory()

    private fun renderAndOpen(forceOpen: Boolean) = lock.withLock {
        pane = panes.collapse()
        val requiresNewInventory = renderToInventory()

        val topInventory = player.openInventory.topInventory
        val isOpen = topInventory.holder == this

        if (forceOpen || (requiresNewInventory && isOpen)) {
            openInventory()
        }

        firstPaint = false
    }

    internal suspend fun applyTransforms(transforms: Collection<AppliedTransform<P>>) {
        transforms.forEach { transform ->
            SCOPE.launch {
                val pane = backing.createPane()
                transform(pane, this@AbstractInterfaceView)
                val completedPane = pane.complete(player)

                lock.withLock {
                    panes[transform.priority] = completedPane
                }
            }.invokeOnCompletion {
                renderAndOpen(forceOpen = false)
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
