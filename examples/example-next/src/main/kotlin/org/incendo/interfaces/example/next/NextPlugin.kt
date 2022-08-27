package org.incendo.interfaces.example.next

import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator
import cloud.commandframework.kotlin.extension.buildAndRegister
import cloud.commandframework.paper.PaperCommandManager
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
import org.incendo.interfaces.next.element.asElement
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

                pane[column, row] = item.asElement()
            }
        }

        withTransform(counterProperty) { pane ->
            pane[3, 3] = ItemStack(Material.BEE_NEST)
                .name("it's been $counter's ticks")
                .asElement {
                    it.player.sendMessage("hi")
                }
        }
    }

    private fun playerInterface() = buildPlayerInterface {
        withTransform { pane ->
            pane[5, 0] = ItemStack(Material.COMPASS)
                .name("interfaces example")
                .asElement()
        }
    }

    private fun ItemStack.name(name: String): ItemStack {
        itemMeta = itemMeta.also { meta ->
            meta.displayName(Component.text(name))
        }
        return this
    }
}
