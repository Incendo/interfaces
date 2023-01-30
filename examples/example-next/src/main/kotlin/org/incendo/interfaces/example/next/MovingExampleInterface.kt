package org.incendo.interfaces.example.next

import org.bukkit.Material
import org.incendo.interfaces.next.drawable.Drawable.Companion.drawable
import org.incendo.interfaces.next.element.StaticElement
import org.incendo.interfaces.next.interfaces.Interface
import org.incendo.interfaces.next.interfaces.buildCombinedInterface
import org.incendo.interfaces.next.utilities.BoundInteger

public class MovingExampleInterface : RegistrableInterface {
    override val subcommand: String = "moving"

    override fun create(): Interface<*> = buildCombinedInterface {
        val countProperty = BoundInteger(4, 1, 7)
        var count by countProperty

        rows = 1

        withTransform(countProperty) { pane, _ ->
            pane[0, 0] = StaticElement(drawable(Material.RED_CONCRETE)) { count-- }
            pane[0, 8] = StaticElement(drawable(Material.GREEN_CONCRETE)) { count++ }

            pane[0, count] = StaticElement(drawable(Material.STICK))
        }
    }
}
