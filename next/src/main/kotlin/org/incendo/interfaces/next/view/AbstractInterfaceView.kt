package org.incendo.interfaces.next.view

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.withTimeout
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.incendo.interfaces.next.Constants.SCOPE
import org.incendo.interfaces.next.event.DrawPaneEvent
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
import java.util.concurrent.atomic.AtomicInteger
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

    /**
     * An override boolean that stores whether this menu is currently open or closed. If closed it can
     * absolutely not make any edits whatsoever!
     */
    protected var opened: Boolean = false

    private val logger = LoggerFactory.getLogger(AbstractInterfaceView::class.java)
    private val semaphore = Semaphore(1)
    private val queue = AtomicInteger(0)

    protected var firstPaint: Boolean = true

    internal var isProcessingClick = false

    private val panes = CollapsablePaneMap.create(backing.totalRows(), backing.createPane())
    internal lateinit var pane: CompletedPane

    protected lateinit var currentInventory: I

    private suspend fun setup() {
        backing.transforms
            .flatMap(AppliedTransform<P>::triggers)
            .forEach { trigger ->
                trigger.addListener(this) {
                    // Handle all trigger updates asynchronously
                    SCOPE.launch {
                        // If the first paint has not completed we do not perform any updates
                        if (firstPaint) return@launch
                        TriggerUpdate(trigger).apply(this@addListener)
                    }
                }
            }

        // Run a complete update which draws all transforms
        // and then opens the menu again
        CompleteUpdate.apply(this)
    }

    override suspend fun open() {
        // Store that the menu has been opened
        opened = true

        // If this menu overlaps the player inventory we always
        // need to do a brand new first paint every time!
        if (firstPaint || overlapsPlayerInventory()) {
            firstPaint = true
            setup()
            firstPaint = false
        } else {
            renderAndOpen(openIfClosed = true)
        }
    }

    override fun close() {
        opened = false
        if (isOpen(player)) {
            // Ensure we always close on the main thread!
            runSync {
                player.closeInventory()
            }
        }
    }

    override fun parent(): InterfaceView? {
        return parent
    }

    override suspend fun back() {
        if (parent == null) {
            close()
        } else {
            parent.open()
        }
    }

    public abstract fun createInventory(): I

    public abstract fun openInventory()

    public abstract fun isOpen(player: Player): Boolean

    internal suspend fun renderAndOpen(openIfClosed: Boolean) {
        // If there is already queue of 2 renders we don't bother!
        if (queue.get() >= 2) return

        // Await to acquire a semaphore before starting the render
        queue.incrementAndGet()
        semaphore.acquire()
        try {
            withTimeout(6.seconds) {
                val isOpen = isOpen(player)

                if (!openIfClosed && !isOpen) {
                    return@withTimeout
                }

                pane = panes.collapse()
                val createdNewInventory = renderToInventory().await()

                // send an update packet if necessary
                if (!createdNewInventory && requiresPlayerUpdate()) {
                    player.updateInventory()
                }

                if ((openIfClosed && !isOpen) || createdNewInventory) {
                    openInventory()
                }
            }
        } finally {
            semaphore.release()
            queue.decrementAndGet()
        }
    }

    internal fun applyTransforms(transforms: Collection<AppliedTransform<P>>): List<Deferred<Unit>> {
        if (Bukkit.isStopping()) {
            return listOf()
        }

        return transforms.map { transform ->
            SCOPE.async {
                try {
                    withTimeout(6.seconds) {
                        runTransformAndApplyToPanes(transform)
                    }
                } catch (exception: Exception) {
                    logger.error("Failed to run and apply transform: $transform", exception)
                    return@async
                }
                return@async
            }
        }
    }

    private suspend fun runTransformAndApplyToPanes(transform: AppliedTransform<P>) {
        val pane = backing.createPane()
        transform(pane, this@AbstractInterfaceView)
        val completedPane = pane.complete(player)

        // Access to the pane has to be shared through a semaphore
        semaphore.acquire()
        panes[transform.priority] = completedPane
        semaphore.release()
    }

    protected open fun drawPaneToInventory() {
        // NEVER draw to the player's inventory if it's not allowed!
        if (overlapsPlayerInventory() && !opened) return

        pane.forEach { row, column, element ->
            currentInventory.set(row, column, element.itemStack.apply { this?.let { backing.itemPostProcessor?.invoke(it) } })
        }
        Bukkit.getPluginManager().callEvent(DrawPaneEvent(player))
    }

    protected open fun requiresNewInventory(): Boolean = firstPaint

    protected open fun requiresPlayerUpdate(): Boolean = false

    protected open fun overlapsPlayerInventory(): Boolean = false

    protected open suspend fun renderToInventory(): Deferred<Boolean> {
        // If a new inventory is required we create one
        // and mark that the current one is not to be used!
        val createdInventory = if (firstPaint || requiresNewInventory()) {
            currentInventory = createInventory()
            true
        } else {
            false
        }

        // Draw the contents of the inventory synchronously because
        // we don't want it to happen in between ticks and show
        // a half-finished inventory.
        val deferred = CompletableDeferred<Boolean>()
        runSync {
            drawPaneToInventory()
            deferred.complete(createdInventory)
        }
        return deferred
    }
}
