package org.incendo.interfaces.example.kotlin

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.empty
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.interfaces.core.arguments.ArgumentKey
import org.incendo.interfaces.core.click.ClickContext
import org.incendo.interfaces.core.click.ClickHandler
import org.incendo.interfaces.core.transform.InterfaceProperty
import org.incendo.interfaces.core.transform.types.PaginatedTransform
import org.incendo.interfaces.core.transform.types.SlidingWindowTransform
import org.incendo.interfaces.core.util.Vector2
import org.incendo.interfaces.kotlin.argumentKeyOf
import org.incendo.interfaces.kotlin.arguments
import org.incendo.interfaces.kotlin.getValue
import org.incendo.interfaces.kotlin.interfaceArgumentOf
import org.incendo.interfaces.kotlin.paper.asElement
import org.incendo.interfaces.kotlin.paper.buildChestInterface
import org.incendo.interfaces.kotlin.paper.buildCombinedInterface
import org.incendo.interfaces.kotlin.paper.buildPlayerInterface
import org.incendo.interfaces.kotlin.paper.open
import org.incendo.interfaces.kotlin.plus
import org.incendo.interfaces.kotlin.setValue
import org.incendo.interfaces.kotlin.to
import org.incendo.interfaces.paper.PaperInterfaceListeners
import org.incendo.interfaces.paper.PlayerViewer
import org.incendo.interfaces.paper.element.ItemStackElement
import org.incendo.interfaces.paper.pane.ChestPane
import org.incendo.interfaces.paper.pane.CombinedPane
import org.incendo.interfaces.paper.type.ChestInterface
import org.incendo.interfaces.paper.type.CombinedInterface
import org.incendo.interfaces.paper.type.PlayerInterface
import org.incendo.interfaces.paper.view.PlayerInventoryView

@Suppress("unused")
public class KotlinPlugin : JavaPlugin() {

    private companion object {
        private const val CHEST_ROWS: Int = 5
        private const val CHEST_COLUMNS: Int = 9

        private val CHEST_TITLE = text("Example Chest", NamedTextColor.GOLD)
        private val LEAVES =
            setOf(Material.EMERALD_BLOCK, Material.DIAMOND_BLOCK, Material.IRON_BLOCK)

        private val ARGUMENT_CONCRETE: ArgumentKey<List<Material>> = argumentKeyOf("concrete")
    }

    private lateinit var exampleChest: ChestInterface
    private lateinit var exampleBasicPlayer: PlayerInterface
    private lateinit var examplePlayer: PlayerInterface
    private lateinit var examplePaginated: ChestInterface
    private lateinit var exampleSliding: ChestInterface
    private lateinit var exampleCombined: CombinedInterface
    private lateinit var exampleCombined2: CombinedInterface

    private var _selectedOption: InterfaceProperty<SelectionOptions> =
        InterfaceProperty.of(SelectionOptions.ONE)

    private var _selectedConcrete: InterfaceProperty<Int> = InterfaceProperty.of(0)

    override fun onEnable() {
        // Register the command.
        getCommand("interfaces")?.setExecutor(InterfaceCommandHandler())

        // Register event listeners.
        PaperInterfaceListeners.install(this)

        val selectedOption: SelectionOptions by _selectedOption
        var selectedConcrete: Int by _selectedConcrete

        // Build a chest interface.
        exampleChest = buildChestInterface {
            title = CHEST_TITLE
            rows = CHEST_ROWS

            clickHandler(
                canceling {
                    it.viewer()
                        .player()
                        .sendMessage(
                            text("You clicked ", NamedTextColor.GRAY)
                                .append(text(it.slot().toString(), NamedTextColor.GOLD))
                        )
                }
            )

            withTransform { view ->
                println("rendering black concrete backing")

                val displayElement: ItemStackElement<ChestPane> = createItemStack(Material.BLACK_CONCRETE, text("")).asElement()

                for (x in 3 until CHEST_COLUMNS) {
                    for (y in 0 until CHEST_ROWS) {
                        view[x, y] = displayElement
                    }
                }
            }

            withTransform { view ->
                println("rendering options")
                SelectionOptions.values().forEach { option ->
                    view[1, option.index] = createItemStack(option.material, text(option.name)).asElement {
                        _selectedOption.set(option)
                    }
                }
            }

            withTransform(2, _selectedOption, _selectedConcrete) { view ->
                println("rendering selected option")

                // Extract an argument from the view
                val concreteList = view.arguments[ARGUMENT_CONCRETE]
                val concrete = concreteList[selectedConcrete]

                val displayElement = createItemStack(concrete, text("")).asElement<ChestPane>()
                selectedOption.art.forEach { (x, y) -> view[x, y] = displayElement }
            }

            withTransform(2, _selectedConcrete) { view ->
                println("rendering concrete selection button")

                // Extract an argument from the view
                val concreteList = view.arguments[ARGUMENT_CONCRETE]
                val concrete = concreteList[selectedConcrete]

                view[0, 4] = createItemStack(concrete, text("CHANGE CONCRETE")).asElement {
                    var newSelection = selectedConcrete + 1
                    if (newSelection == concreteList.size) {
                        newSelection = 0
                    }
                    selectedConcrete = newSelection
                }
            }

            withCloseHandler { event, _ -> event.player.sendMessage(text("bye")) }
        }

        exampleBasicPlayer = buildPlayerInterface {
            clickHandler = canceling()

            withTransform {
                it.hotbar[2] = createItemStack(Material.COMPASS, text("TIME")).asElement { click ->
                    click.cancel(true)
                    val player = click.viewer().player()

                    player.sendMessage(
                        text("The time is: ")
                            .append(text(player.world.time, NamedTextColor.RED))
                    )
                }
            }
        }

        examplePlayer = buildPlayerInterface {
            withTransform {
                val num = (Bukkit.getCurrentTick() / 20) % 16

                for (i in 0..3) {
                    val wool = if (num and (1 shl i) != 0) {
                        Material.WHITE_WOOL
                    } else {
                        Material.BLACK_WOOL
                    }

                    it.armor[i] = ItemStackElement.of(
                        createItemStack(wool, empty()), ClickHandler.cancel()
                    )
                }
            }

            withTransform {
                val tick = Bukkit.getCurrentTick()
                val wools = Material.values().filter { material -> "WOOL" in material.name }

                for (i in 0..8) {
                    val woolIndex = (i + tick) % wools.size

                    it.hotbar[i] = ItemStackElement.of(
                        createItemStack(wools[woolIndex], empty()), ClickHandler.cancel()
                    )
                }
            }

            withTransform {
                for (x in 0..8) {
                    for (y in 0..2) {
                        val wool = if (((Bukkit.getCurrentTick() / 2) % 9) + y == x) {
                            Material.YELLOW_WOOL
                        } else {
                            Material.RED_WOOL
                        }

                        it.main[x, y] = ItemStackElement.of(
                            createItemStack(wool, empty()), ClickHandler.cancel()
                        )
                    }
                }
            }

            updates(true, 1)
        }

        examplePaginated = buildChestInterface {
            rows = 4

            val reactiveTransform: PaginatedTransform<ItemStackElement<ChestPane>, ChestPane, PlayerViewer> = PaginatedTransform(
                Vector2.at(2, 1),
                Vector2.at(6, 2),
                (1..30).map {
                    createItemStack(
                        Material.PAPER, text(it.toString(), NamedTextColor.BLUE)
                    ).asElement(ClickHandler.cancel())
                }
            )
            reactiveTransform.backwardElement(Vector2.at(0, 0)) { transform ->
                createSkull(text("Previous Page"), "MHF_ArrowLeft").asElement {
                    transform.previousPage()
                }
            }
            reactiveTransform.forwardElement(Vector2.at(8, 3)) { transform ->
                createSkull(text("Next Page"), "MHF_ArrowRight").asElement {
                    transform.nextPage()
                }
            }

            addTransform(reactiveTransform)
        }

        exampleSliding = buildChestInterface {
            rows = 4

            val elements = mutableListOf<ItemStackElement<ChestPane>>()
            var index = 0
            for (material in Material.values().filter { "WOOL" in it.name }) {
                repeat(2) {
                    elements.add(
                        createItemStack(material, text(index++.toString()))
                            .asElement(ClickHandler.cancel())
                    )
                }
            }

            val reactiveTransform:
                SlidingWindowTransform<ItemStackElement<ChestPane>, ChestPane, PlayerViewer> =
                SlidingWindowTransform(Vector2.at(2, 1), Vector2.at(6, 2), elements)

            reactiveTransform.backwardElement(Vector2.at(0, 0)) { transform ->
                createSkull(text("Slide Back"), "MHF_ArrowLeft").asElement {
                    transform.slideBack()
                }
            }
            reactiveTransform.forwardElement(Vector2.at(8, 3)) { transform ->
                createSkull(text("Slide Forward"), "MHF_ArrowRight").asElement {
                    transform.slideForward()
                }
            }

            addTransform(reactiveTransform)
        }

        exampleCombined = buildCombinedInterface {
            chestRows = 2

            clickHandler(canceling())

            withCloseHandler { event, view ->
                Bukkit.broadcast(text("hallo"))
            }

            val soundHandler:
                ClickHandler<
                    CombinedPane,
                    InventoryClickEvent,
                    PlayerViewer,
                    ClickContext<CombinedPane, InventoryClickEvent, PlayerViewer>> =
                ClickHandler {
                    it.viewer()
                        .player()
                        .playSound(
                            net.kyori.adventure.sound.Sound.sound(
                                Sound.UI_BUTTON_CLICK.key(),
                                net.kyori.adventure.sound.Sound.Source.MASTER,
                                1f,
                                1f
                            )
                        )
                }

            withTransform { view ->
                view.hotbar(
                    4,
                    createItemStack(Material.LIME_CONCRETE, text("wooo"))
                        .asElement(
                            soundHandler + { it.viewer().player().sendMessage(text("hi")) }
                        )
                )
            }

            withTransform { view ->
                for (x in 0 until 9) {
                    for (y in 0 until 4) {
                        view[x, y] = createItemStack(Material.COMPASS, text("hi $y")).asElement()
                    }
                }
            }

//            updates(true, 5)
        }

        exampleCombined2 = buildCombinedInterface {
            chestRows = 2

            withCloseHandler { event, view ->
                Bukkit.broadcast(text("hey"))
            }

            withTransform { view ->
                view[0, 0] = ItemStackElement(ItemStack(Material.GRANITE)) {
                    exampleCombined.open(view.viewer())
                }
            }
        }
    }

    private fun createItemStack(material: Material, name: Component): ItemStack = ItemStack(material).also {
        it.itemMeta = it.itemMeta.also { meta -> meta.displayName(name) }
    }

    private fun createSkull(name: Component, owner: String): ItemStack = ItemStack(Material.PLAYER_HEAD).also {
        it.itemMeta =
            it.itemMeta.also { meta ->
                meta.displayName(name)
                (meta as SkullMeta).owner = owner
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
                sender.sendMessage(
                    text("Only players may execute this command", NamedTextColor.RED)
                )
                return false
            }

            if (args.isEmpty()) {
                sender.sendMessage(
                    text(
                        "You must specify one of: chest, player, close, paginated, sliding",
                        NamedTextColor.RED
                    )
                )
                return false
            }

            // Pass an argument to the interface.
            val arguments =
                interfaceArgumentOf(
                    ARGUMENT_CONCRETE to listOf(
                        Material.LIME_CONCRETE,
                        Material.WHITE_CONCRETE,
                        Material.BLUE_CONCRETE
                    )
                )

            when (args[0].lowercase()) {
                "chest" -> sender.open(
                    exampleChest,
                    arguments,
                    text("Your Chest: ${sender.name}", NamedTextColor.GREEN)
                )
                "basicplayer" -> sender.open(exampleBasicPlayer, arguments)
                "player" -> sender.open(examplePlayer, arguments)
                "paginated" -> sender.open(examplePaginated, arguments)
                "sliding" -> sender.open(exampleSliding, arguments)
                "combined" -> sender.open(exampleCombined, arguments)
                "combined2" -> sender.open(exampleCombined2, arguments)
                "close" -> {
                    sender.closeInventory()
                    // Also close their player interface, if they have one open.
                    PlayerInventoryView.forPlayer(sender)?.close()
                }
                else -> sender.sendMessage(
                    text("Unknown interface type '${args[0]}'", NamedTextColor.RED)
                )
            }

            return true
        }
    }
}
