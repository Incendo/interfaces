package org.incendo.interfaces.example.next

import kotlinx.coroutines.delay
import net.kyori.adventure.text.Component.text
import org.bukkit.Material
import org.incendo.interfaces.next.drawable.Drawable
import org.incendo.interfaces.next.element.StaticElement
import org.incendo.interfaces.next.interfaces.Interface
import org.incendo.interfaces.next.interfaces.buildCombinedInterface
import kotlin.time.Duration.Companion.seconds

public class DelayedRequestExampleInterface : RegistrableInterface

    private companion object {
        private val BACKING_ELEMENT = StaticElement(Drawable.drawable(Material.GRAY_CONCRETE))
    }

    override val subcommand: String = "delayed"

    override fun create(): Interface<*> = buildCombinedInterface {
        initialTitle = text(subcommand)
        rows = 2

        withTransform { pane ->
            suspendingData().forEachIndexed { index, material ->
                pane[0, index] = StaticElement(Drawable.drawable(material))
            }
        }

        withTransform { pane ->
            for (index in 0..8) {
                pane[1, index] = BACKING_ELEMENT
            }

            pane[0, 8] = StaticElement(Drawable.Companion.drawable(Material.ENDER_PEARL)) {
                it.view.back()
            }
        }
    }

    private suspend fun suspendingData(): List<Material> {
        delay(3.seconds)
        return listOf(Material.GREEN_CONCRETE, Material.YELLOW_CONCRETE, Material.RED_CONCRETE)
    }
}
