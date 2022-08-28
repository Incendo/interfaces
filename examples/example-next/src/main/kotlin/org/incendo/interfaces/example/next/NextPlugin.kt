package org.incendo.interfaces.example.next

import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator
import cloud.commandframework.kotlin.extension.buildAndRegister
import cloud.commandframework.paper.PaperCommandManager
import java.util.concurrent.CompletableFuture
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.interfaces.next.InterfacesListeners
import org.incendo.interfaces.next.drawable.Drawable.Companion.drawable
import org.incendo.interfaces.next.element.StaticElement
import org.incendo.interfaces.next.interfaces.buildChestInterface
import org.incendo.interfaces.next.interfaces.buildPlayerInterface
import org.incendo.interfaces.next.open
import org.incendo.interfaces.next.properties.interfaceProperty
import org.incendo.interfaces.next.utilities.forEachInGrid

public class NextPlugin : JavaPlugin(), Listener {

    private val counterProperty = interfaceProperty(5)
    private var counter by counterProperty

    override fun onEnable() {
        val commandManager = PaperCommandManager.createNative(
            this,
            AsynchronousCommandExecutionCoordinator.newBuilder<CommandSender>().build()
        )

        commandManager.buildAndRegister("interfaces") {
            registerCopy {
                literal("simple")

                handler {
                    val player = it.sender as Player
                    val simpleInterface = simpleInterface()

                    player.open(simpleInterface)
                }
            }
        }

        InterfacesListeners.install(this)

        this.server.pluginManager.registerEvents(this, this)

        Bukkit.getScheduler().runTaskTimerAsynchronously(
            this,
            Runnable {
                counter++
            },
            0, 1
        )
    }

    @EventHandler
    public fun onJoin(e: PlayerJoinEvent) {
        Bukkit.getScheduler().runTaskAsynchronously(
            this,
            Runnable {
                playerInterface().open(e.player)
            }
        )
    }

    private fun simpleInterface() = buildChestInterface {
        rows = 6

        withTransform { pane ->
            forEachInGrid(9, 6) { column, row ->
                val item = ItemStack(Material.WHITE_STAINED_GLASS_PANE)
                    .name("col: $column, row: $row")

                pane[column, row] = StaticElement.syncHandler(drawable(item))
            }
        }

        withTransform(counterProperty) { pane ->
            val item = ItemStack(Material.BEE_NEST)
                .name("it's been $counter's ticks")
                .description("click to see the ticks now")

            pane[3, 3] = StaticElement.syncHandler(drawable(item)) {
                it.player.sendMessage("it's been $counter's ticks")
            }
        }

        withTransform { pane ->
            val item = ItemStack(Material.BEE_NEST)
                .name("block the interface")
                .description("block interaction and message in 5 seconds")

            pane[5, 3] = StaticElement.asyncHandler(drawable(item)) {
                val future = CompletableFuture<Unit>()

                runAsync(5) {
                    println("thing")
                    it.player.sendMessage("after blocking, it has been $counter's ticks")
                    future.complete(Unit)
                }

                return@asyncHandler future
            }
        }
    }

    private fun playerInterface() = buildPlayerInterface {
        withTransform { pane ->
            val item = ItemStack(Material.COMPASS).name("interfaces example")

            pane[5, 0] = StaticElement.syncHandler(drawable(item))
        }
    }

    private fun ItemStack.name(name: String): ItemStack {
        itemMeta = itemMeta.also { meta ->
            meta.displayName(Component.text(name))
        }
        return this
    }

    private fun ItemStack.description(description: String): ItemStack {
        itemMeta = itemMeta.also { meta ->
            meta.lore(listOf(Component.text(description)))
        }
        return this
    }

    private fun runAsync(delay: Int, runnable: Runnable) {
        println(delay * 20L)
        Bukkit.getScheduler().runTaskLaterAsynchronously(this, runnable, delay * 20L)
    }

}
