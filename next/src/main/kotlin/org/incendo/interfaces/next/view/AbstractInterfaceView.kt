package org.incendo.interfaces.next.view

import com.google.common.collect.HashMultimap
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
import org.incendo.interfaces.next.properties.Trigger
import org.incendo.interfaces.next.transform.AppliedTransform
import org.incendo.interfaces.next.utilities.CollapsablePaneMap
import org.incendo.interfaces.next.utilities.runSync
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import kotlin.Exception
import kotlin.time.Duration.Companion.seconds

public abstract class AbstractInterfaceView<I : InterfacesInventory, P : Pane>(
    public val player: Player,
    public val backing: Interface<P>,
    private val parent: InterfaceView?
) : InterfaceView {

    public companion object {
        public const val COLUMNS_IN_CHEST: Int = 9
    }

    private val logger = LoggerFactory.getLogger(AbstractInterfaceView::class.java)
    private val semaphore = Semaphore(1)
    private val queue = AtomicInteger(0)

    protected var firstPaint: Boolean = true
    internal var isProcessingClick = false
    private var openIfClosed = false

    private val pendingTransforms = ConcurrentHashMap.newKeySet<AppliedTransform<P>>()
    private val debouncedTransforms = ConcurrentHashMap.newKeySet<AppliedTransform<P>>()

    private val panes = CollapsablePaneMap.create(backing.totalRows(), backing.createPane())
    internal lateinit var pane: CompletedPane

    protected lateinit var currentInventory: I

    private fun setup() {
        // Determine for each trigger what transforms it updates
        val triggers = HashMultimap.create<Trigger, AppliedTransform<P>>()
        for (transform in backing.transforms) {
            for (trigger in transform.triggers) {
                triggers.put(trigger, transform)
            }
        }

        // Add listeners to all triggers and update its transforms
        for ((trigger, transforms) in triggers.asMap()) {
            trigger.addListener(this) {
                // If the first paint has not completed we do not perform any updates
                if (firstPaint) return@addListener

                // Apply the transforms for the new ones
                applyTransforms(transforms)
            }
        }

        // Run a complete update which draws all transforms
        // and then opens the menu again
        redrawComplete()
    }

    /**
     * Redraws all transforms in this view.
     */
    public fun redrawComplete() {
        applyTransforms(backing.transforms)
    }

    override suspend fun open() {
        // Indicate that the menu should be opened after the next time rendering completes
        openIfClosed = true

        // If this menu overlaps the player inventory we always
        // need to do a brand new first paint every time!
        if (firstPaint || this !is ChestInterfaceView) {
            firstPaint = true
            setup()
            firstPaint = false
        } else {
            renderAndOpen()
        }
    }

    override fun close() {
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

    internal suspend fun renderAndOpen() {
        // Don't update if closed
        if (!openIfClosed && !isOpen(player)) return

        // If there is already queue of 2 renders we don't bother!
        if (queue.get() >= 2) return

        // Await to acquire a semaphore before starting the render
        queue.incrementAndGet()
        semaphore.acquire()
        try {
            withTimeout(6.seconds) {
                pane = panes.collapse()
                renderToInventory { createdNewInventory ->
                    // send an update packet if necessary
                    if (!createdNewInventory && requiresPlayerUpdate()) {
                        player.updateInventory()
                    }
                }
            }
        } finally {
            semaphore.release()
            queue.decrementAndGet()
        }
    }

    internal fun applyTransforms(transforms: Collection<AppliedTransform<P>>): Boolean {
        // Remove all these from the debounced transforms so we can try running
        // them again!
        debouncedTransforms -= transforms.toSet()

        // Check if the player is offline or the server stopping
        if (Bukkit.isStopping() || !player.isOnline) return false

        transforms.forEach { transform ->
            // If the transform is already pending we debounce it
            if (transform in pendingTransforms) {
                debouncedTransforms += transform
                return@forEach
            }

            // Indicate this transform is running which prevents the menu
            // from rendering until all transforms are done!
            pendingTransforms += transform

            SCOPE.launch {
                try {
                    // Don't run transforms for an offline player!
                    if (!Bukkit.isStopping() && player.isOnline) {
                        withTimeout(6.seconds) {
                            runTransformAndApplyToPanes(transform)
                        }
                    }
                } catch (exception: Exception) {
                    logger.error("Failed to run and apply transform: $transform", exception)
                } finally {
                    // Update that this transform has finished and check if
                    // we are ready to draw the screen finally!
                    pendingTransforms -= transform

                    if (transform in debouncedTransforms && applyTransforms(listOf(transform))) {
                        // Simply run the transform again here and do nothing else
                    } else {
                        // If all transforms are done we can finally draw and open the menu
                        if (pendingTransforms.isEmpty()) {
                            renderAndOpen()
                        }
                    }
                }
            }
        }

        // In the case that transforms was empty we might be able to open the menu already
        if (pendingTransforms.isEmpty()) {
            SCOPE.launch {
                renderAndOpen()
            }
        }
        return true
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

    protected open fun drawPaneToInventory(opened: Boolean) {
        var madeChanges = false
        pane.forEach { row, column, element ->
            // We defer drawing of any elements in the player inventory itself
            // for later unless the inventory is already open.
            if (!opened && currentInventory.isPlayerInventory(row, column)) return@forEach
            currentInventory.set(row, column, element.itemStack.apply { this?.let { backing.itemPostProcessor?.invoke(it) } })
            madeChanges = true
        }
        if (madeChanges) {
            Bukkit.getPluginManager().callEvent(DrawPaneEvent(player))
        }
    }

    override fun onOpen() {
        // Whenever we open the inventory we draw all elements in the player inventory
        // itself. We do this in this hook because it runs after InventoryCloseEvent so
        // it properly happens as the last possible action.
        var madeChanges = false
        pane.forEach { row, column, element ->
            if (!currentInventory.isPlayerInventory(row, column)) return@forEach
            currentInventory.set(row, column, element.itemStack.apply { this?.let { backing.itemPostProcessor?.invoke(it) } })
            madeChanges = true
        }
        if (madeChanges) {
            Bukkit.getPluginManager().callEvent(DrawPaneEvent(player))
        }
    }

    protected open fun requiresNewInventory(): Boolean = firstPaint

    protected open fun requiresPlayerUpdate(): Boolean = false

    protected open suspend fun renderToInventory(callback: (Boolean) -> Unit) {
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
        runSync {
            // Determine if the inventory is currently open or being opened immediately,
            // otherwise we never draw to player inventories. This ensures lingering
            // updates on menus that have closed do not affect future menus that actually
            // ended up being opened.
            val isOpen = isOpen(player)
            drawPaneToInventory(isOpen)
            callback(createdInventory)

            if ((openIfClosed && !isOpen) || createdInventory) {
                openInventory()
                openIfClosed = false
            }
        }
    }
}
