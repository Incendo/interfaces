package org.incendo.interfaces.example.next

import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator
import cloud.commandframework.kotlin.extension.buildAndRegister
import cloud.commandframework.paper.PaperCommandManager
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.interfaces.next.InterfacesListeners
import org.incendo.interfaces.next.element.asElement
import org.incendo.interfaces.next.interfaces.buildChestInterface
import org.incendo.interfaces.next.open
import org.incendo.interfaces.next.properties.interfaceProperty
import org.incendo.interfaces.next.utilities.forEachInGrid

public class NextPlugin : JavaPlugin() {

    private companion object {
        private val COLORS = setOf(
            Material.WHITE_STAINED_GLASS_PANE,
            Material.PINK_STAINED_GLASS_PANE,
            Material.LIGHT_BLUE_STAINED_GLASS_PANE
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

                handler {
                    val player = it.sender as Player
                    val simpleInterface = simpleInterface()

                    player.open(simpleInterface)
                }
            }
        }

        InterfacesListeners.install(this)

        Bukkit.getScheduler().runTaskTimerAsynchronously(
            this,
            Runnable {
                counter++
            },
            0, 1
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

    private fun ItemStack.name(name: String): ItemStack {
        itemMeta = itemMeta.also { meta ->
            meta.displayName(Component.text(name))
        }
        return this
    }
}
