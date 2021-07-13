package org.incendo.interfaces.example.kotlin

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.interfaces.core.arguments.ArgumentKey
import org.incendo.interfaces.core.transform.InterfaceProperty
import org.incendo.interfaces.kotlin.*
import org.incendo.interfaces.kotlin.getValue
import org.incendo.interfaces.kotlin.interfaceArgumentOf
import org.incendo.interfaces.kotlin.paper.asElement
import org.incendo.interfaces.kotlin.paper.buildChestInterface
import org.incendo.interfaces.kotlin.paper.open
import org.incendo.interfaces.kotlin.setValue
import org.incendo.interfaces.paper.PaperInterfaceListeners
import org.incendo.interfaces.paper.pane.ChestPane
import org.incendo.interfaces.paper.type.ChestInterface

@Suppress("unused")
public class KotlinPlugin : JavaPlugin() {

    private companion object {
        private const val CHEST_ROWS: Int = 5
        private const val CHEST_COLUMNS: Int = 9

        private val CHEST_TITLE = text("Example Chest", NamedTextColor.GOLD)
        private val LEAVES =
            setOf(Material.EMERALD_BLOCK, Material.DIAMOND_BLOCK, Material.IRON_BLOCK)

        private val ARGUMENT_CONCRETE: ArgumentKey<Material> = argumentKeyOf("concrete")
    }

    private lateinit var exampleChest: ChestInterface

    private var _selectedOption: InterfaceProperty<SelectionOptions> =
        InterfaceProperty.of(SelectionOptions.ONE)

    override fun onEnable() {
        // Register the command.
        getCommand("interfaces")?.setExecutor(InterfaceCommandHandler())

        // Register event listeners.
        PaperInterfaceListeners.install(this)

        // Update the dependent value every time the server ticks.
        var selectedOption: SelectionOptions by _selectedOption

        // Build a chest interface.
        exampleChest =
            buildChestInterface {
                title = CHEST_TITLE
                rows = CHEST_ROWS

                clickHandler(
                    canceling { event: InventoryClickEvent, _ ->
                        event.whoClicked.sendMessage(
                            text("You clicked ", NamedTextColor.GRAY)
                                .append(text(event.slot.toString(), NamedTextColor.GOLD)))
                    })

                withTransform(priority = 5) { view ->
                    println("rendering black concrete backing")

                    val displayElement =
                        createItemStack(Material.BLACK_CONCRETE, text("")).asElement<ChestPane>()

                    for (x in 3 until CHEST_COLUMNS - 1) {
                        for (y in 0 until CHEST_ROWS) {
                            view[x, y] = displayElement
                        }
                    }
                }

                withTransform { view ->
                    println("rendering options")
                    SelectionOptions.values().forEach { option ->
                        view[1, option.index] =
                            createItemStack(option.material, text(option.name)).asElement { _, _ ->
                                _selectedOption.set(option)
                            }
                    }
                }

                withTransform(_selectedOption) { view ->
                    println("rendering selected option")

                    // Extract an argument from the view
                    val concrete = view.arguments[ARGUMENT_CONCRETE]

                    val displayElement = createItemStack(concrete, text("")).asElement<ChestPane>()
                    selectedOption.art.forEach { (x, y) -> view[x, y] = displayElement }
                }

                withCloseHandler { event, _ -> event.player.sendMessage(text("bye")) }
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

            // Pass an argument to the interface.
            val arguments = interfaceArgumentOf(ARGUMENT_CONCRETE to Material.LIME_CONCRETE)

            when (args[0].toLowerCase()) {
                "chest" ->
                    sender.open(
                        exampleChest,
                        arguments,
                        text("Your Chest: ${sender.name}", NamedTextColor.GREEN))
                else ->
                    sender.sendMessage(
                        text("Unknown interface type '${args[0]}'", NamedTextColor.RED))
            }

            return true
        }
    }
}
