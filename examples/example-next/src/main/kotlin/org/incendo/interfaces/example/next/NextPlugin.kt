package org.incendo.interfaces.example.next

import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator
import cloud.commandframework.kotlin.coroutines.extension.suspendingHandler
import cloud.commandframework.kotlin.extension.buildAndRegister
import cloud.commandframework.paper.PaperCommandManager
import kotlinx.coroutines.runBlocking
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
import org.incendo.interfaces.next.interfaces.buildCombinedInterface
import org.incendo.interfaces.next.interfaces.buildPlayerInterface
import org.incendo.interfaces.next.open
import org.incendo.interfaces.next.properties.interfaceProperty
import org.incendo.interfaces.next.utilities.forEachInGrid

public class NextPlugin : JavaPlugin(), Listener {

    private companion object {
        private val INTERFACES = listOf(
            DelayedRequestExampleInterface(),
            ChangingTitleExampleInterface(),
            CatalogueExampleInterface()
        )
    }

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

                suspendingHandler {
                    val player = it.sender as Player
                    val simpleInterface = simpleInterface()

                    player.open(simpleInterface)
                }
            }

            registerCopy {
                literal("combined")

                suspendingHandler {
                    val player = it.sender as Player
                    val combinedInterface = combinedInterface()

                    player.open(combinedInterface)
                }
            }

            for (registrableInterface in INTERFACES) {
                registerCopy {
                    literal(registrableInterface.subcommand)

                    suspendingHandler {
                        val player = it.sender as Player
                        val builtInterface = registrableInterface.create()

                        player.open(builtInterface)
                    }
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
            0,
            1
        )
    }

    @EventHandler
    public fun onJoin(e: PlayerJoinEvent) {
        Bukkit.getScheduler().runTaskAsynchronously(
            this,
            Runnable {
                runBlocking {
                    playerInterface().open(e.player)
                }
            }
        )
    }

    private fun simpleInterface() = buildChestInterface {
        rows = 6

        withTransform { pane, _ ->
            forEachInGrid(6, 9) { row, column ->
                val item = ItemStack(Material.WHITE_STAINED_GLASS_PANE)
                    .name("row: $row, column: $column")

                pane[row, column] = StaticElement(drawable(item))
            }
        }

        withTransform(counterProperty) { pane, _ ->
            val item = ItemStack(Material.BEE_NEST)
                .name("it's been $counter's ticks")
                .description("click to see the ticks now")

            pane[3, 3] = StaticElement(drawable(item)) {
                it.player.sendMessage("it's been $counter's ticks")
            }
        }

        withTransform { pane, _ ->
            val item = ItemStack(Material.BEE_NEST)
                .name("block the interface")
                .description("block interaction and message in 5 seconds")

            pane[5, 3] = StaticElement(drawable(item)) {
                completingLater = true

                runAsync(5) {
                    it.player.sendMessage("after blocking, it has been $counter's ticks")
                    complete()
                }
            }
        }
    }

    private fun playerInterface() = buildPlayerInterface {
        withTransform { pane, _ ->
            val item = ItemStack(Material.COMPASS).name("interfaces example")

            pane.hotbar[3] = StaticElement(drawable(item)) { (player) ->
                player.sendMessage("hello")
            }
        }
    }

    private fun combinedInterface() = buildCombinedInterface {
        rows = 6

        withTransform { pane, _ ->
            forEachInGrid(10, 9) { row, column ->
                val item = ItemStack(Material.WHITE_STAINED_GLASS_PANE)
                    .name("row: $row, column: $column")

                pane[row, column] = StaticElement(drawable(item)) { (player) ->
                    player.sendMessage("row: $row, column: $column")
                }
            }
        }
    }

    private fun runAsync(delay: Int, runnable: Runnable) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(this, runnable, delay * 20L)
    }
}
