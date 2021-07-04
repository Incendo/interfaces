package org.incendo.interfaces.example.kotlin

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.interfaces.kotlin.argument
import org.incendo.interfaces.kotlin.interfaceArgumentOf
import org.incendo.interfaces.kotlin.paper.asElement
import org.incendo.interfaces.kotlin.paper.buildChestInterface
import org.incendo.interfaces.kotlin.paper.open
import org.incendo.interfaces.paper.PaperInterfaceListeners
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

        // Register event listeners.
        PaperInterfaceListeners.install(this)

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

                withTransform { view ->
                    // Extract the name argument.
                    val name: String = view.argument["name"] ?: return@withTransform

                    // Create an item stack element with the player's name.
                    val element =
                        createItemStack(Material.PAPER, text(name)).asElement { event, _ ->
                            event.whoClicked.sendMessage(
                                text("You clicked it :D", NamedTextColor.AQUA))
                        }

                    view[0, 0] = element
                }
            }
    }

    private fun createItemStack(material: Material, name: Component): ItemStack =
        ItemStack(material).also {
            it.itemMeta = it.itemMeta.also { meta -> meta.displayName(name) }
        }

    private inner class InterfaceCommandHandler : CommandExecutor {

        override fun onCommand(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<out String>
        ): Boolean {
            if (sender !is Player) {
                sender.sendMessage(
                    text("Only players may execute this command", NamedTextColor.RED))
                return false
            }

            if (args.isEmpty()) {
                sender.sendMessage(text("You must specify one of: chest", NamedTextColor.RED))
                return false
            }

            // Pass the player's name as an argument.
            val arguments = interfaceArgumentOf("name" to sender.name)

            when (args[0].toLowerCase()) {
                "chest" -> sender.open(exampleChest, arguments)
                else ->
                    sender.sendMessage(
                        text("Unknown interface type '${args[0]}'", NamedTextColor.RED))
            }

            return true
        }
    }
}
