package org.incendo.interfaces.example.next

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.incendo.interfaces.next.drawable.Drawable
import org.incendo.interfaces.next.element.StaticElement
import org.incendo.interfaces.next.interfaces.Interface
import org.incendo.interfaces.next.interfaces.buildCombinedInterface
import org.incendo.interfaces.next.properties.interfaceProperty

public class ChangingTitleExampleInterface : RegistrableInterface {
    override val subcommand: String = "changing-title"

    override fun create(): Interface<*> =
        buildCombinedInterface {
            rows = 1

            val numberProperty = interfaceProperty(0)
            var number by numberProperty

            withTransform(numberProperty) { pane, view ->
                view.title(Component.text(number))

                val item =
                    ItemStack(Material.STICK)
                        .name("number -> $number")

                pane[0, 4] =
                    StaticElement(Drawable.drawable(item)) {
                        number += 1
                    }
            }
        }
}
