package org.incendo.interfaces.example.kotlin

import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.interfaces.kotlin.paper.buildChestInterface
import org.incendo.interfaces.kotlin.paper.open
import org.incendo.interfaces.paper.type.ChestInterface

@Suppress("unused")
public class KotlinPlugin : JavaPlugin() {

  private companion object {
    private const val CHEST_ROWS: Int = 3

    private val CHEST_TITLE = text("Example Chest", NamedTextColor.GOLD)
  }

  private lateinit var exampleChest: ChestInterface

  override fun onEnable() {
    // Register the command.
    getCommand("interfaces")?.setExecutor(InterfaceCommandHandler())

    // Build a chest interface.
    exampleChest =
        buildChestInterface {
          title = CHEST_TITLE
          rows = CHEST_ROWS

          clickHandler { event, _ ->
            event.whoClicked.sendMessage(
                text("You clicked ", NamedTextColor.GRAY)
                    .append(text(event.slot.toString(), NamedTextColor.GOLD)))
          }
        }
  }

  private inner class InterfaceCommandHandler : CommandExecutor {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
      if (sender !is Player) {
        sender.sendMessage(text("Only players may execute this command", NamedTextColor.RED))
        return false
      }

      if (args.isEmpty()) {
        sender.sendMessage(text("You must specify one of: chest", NamedTextColor.RED))
        return false
      }

      when (args[0].toLowerCase()) {
        "chest" -> sender.open(exampleChest)
        else -> sender.sendMessage(text("Unknown interface type '${args[0]}'", NamedTextColor.RED))
      }

      return true
    }
  }
}
