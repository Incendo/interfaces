package org.incendo.interfaces.example.next

import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator
import cloud.commandframework.kotlin.extension.buildAndRegister
import cloud.commandframework.paper.PaperCommandManager
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.interfaces.next.element.asElement
import org.incendo.interfaces.next.interfaces.buildChestInterface

public class NextPlugin : JavaPlugin() {

    override fun onEnable() {
        val commandManager = PaperCommandManager.createNative(
            this,
            AsynchronousCommandExecutionCoordinator.newBuilder<CommandSender>().build()
        )

        commandManager.buildAndRegister("interfaces") {
            registerCopy {
                literal("simple")

                handler {
                    simpleInterface(it.sender as Player)
                }
            }
        }
    }

    private fun simpleInterface(player: Player) = buildChestInterface {
        rows = 6

        withTransform { pane ->
            pane.set { column, row ->
                val item = ItemStack(Material.WHITE_STAINED_GLASS_PANE).also { itemStack ->
                    itemStack.itemMeta = itemStack.itemMeta.also { meta ->
                        meta.displayName(Component.text("col: $column, row: $row"))
                    }
                }

                item.asElement()
            }
        }

        withTransform { pane ->
            pane[3, 3] = ItemStack(Material.BEE_NEST).asElement {
                println("hi")
            }
        }
    }
}
