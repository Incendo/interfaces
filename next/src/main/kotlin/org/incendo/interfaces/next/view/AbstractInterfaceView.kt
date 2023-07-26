package org.incendo.interfaces.next.view

import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import org.bukkit.Bukkit
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
import org.incendo.interfaces.next.utilities.runSync
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.Exception
import kotlin.time.Duration.Companion.seconds

public abstract class AbstractInterfaceView<I : InterfacesInventory, P : Pane>(
    public val player: Player,
    public val backing: Interface<P>,
    private val parent: InterfaceView?,
) : InterfaceView {

    public companion object {
        public const val COLUMNS_IN_CHEST: Int = 9
    }

    private val logger = LoggerFactory.getLogger(AbstractInterfaceView::class.java)
    private val lock = ReentrantLock()

    protected var firstPaint: Boolean = true

    // todo(josh): reduce internal abuse?
    internal var isProcessingClick = false

    private val panes = CollapsablePaneMap.create(backing.totalRows(), backing.createPane())
    internal lateinit var pane: CompletedPane

    protected lateinit var currentInventory: I

    private fun setup() {
        CompleteUpdate.apply(this)

        backing.transforms
            .flatMap(AppliedTransform<P>::triggers)
            .forEach { trigger ->
                trigger.addListener(this) {
                    TriggerUpdate(trigger).apply(this@addListener)
                }
            }
    }

    public override fun open() {
        renderAndOpen(forceOpen = true)

        if (firstPaint) {
            setup()
        }

        firstPaint = false
    }

    public override fun close() {
        if (isOpen(player)) {
            // Ensure we always close on the main thread!
            runSync {
                player.closeInventory()
            }
        }
    }

    public override fun parent(): InterfaceView? {
        return parent
    }

    public override fun back() {
        if (parent == null) {
            close()
        } else {
            parent.open()
        }
    }

    public abstract fun createInventory(): I

    public abstract fun openInventory()

    public abstract fun isOpen(player: Player): Boolean

    private fun renderAndOpen(forceOpen: Boolean) = lock.withLock(6, TimeUnit.SECONDS) {
        val isOpen = isOpen(player)

        if (!forceOpen && !isOpen) {
            return@withLock
        }

        pane = panes.collapse()
        val requiresNewInventory = renderToInventory()

        if (forceOpen || requiresNewInventory) {
            openInventory()
        }
    }

    internal fun applyTransforms(transforms: Collection<AppliedTransform<P>>) {
        if (Bukkit.isStopping()) {
            return
        }

        transforms.forEach { transform ->
            SCOPE.launch {
                try {
                    withTimeout(6.seconds) {
                        runTransformAndApplyToPanes(transform)
                    }
                } catch (exception: Exception) {
                    logger.error("Failed to run and apply transform: $transform", exception)
                    return@launch
                }

                renderAndOpen(forceOpen = false)
            }
        }
    }

    private suspend fun runTransformAndApplyToPanes(transform: AppliedTransform<P>) {
        val pane = backing.createPane()
        transform(pane, this@AbstractInterfaceView)
        val completedPane = pane.complete(player)

        lock.withLock(6, TimeUnit.SECONDS) {
            panes[transform.priority] = completedPane
        }
    }

    private fun drawPaneToInventory() {
        pane.forEach { row, column, element ->

            currentInventory.set(row, column, element.itemStack.apply { this?.let { backing.itemPostProcessor?.invoke(it) } })
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
