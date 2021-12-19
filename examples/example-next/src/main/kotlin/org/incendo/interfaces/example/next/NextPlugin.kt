package org.incendo.interfaces.example.next

import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator
import cloud.commandframework.kotlin.extension.buildAndRegister
import cloud.commandframework.paper.PaperCommandManager
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
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
    }
}
