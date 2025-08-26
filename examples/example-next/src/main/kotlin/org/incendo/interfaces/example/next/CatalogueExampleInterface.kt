package org.incendo.interfaces.example.next

import kotlinx.coroutines.runBlocking
import org.bukkit.Material
import org.incendo.interfaces.next.drawable.Drawable
import org.incendo.interfaces.next.element.StaticElement
import org.incendo.interfaces.next.interfaces.Interface
import org.incendo.interfaces.next.interfaces.buildCombinedInterface

public class CatalogueExampleInterface : RegistrableInterface {
    override val subcommand: String = "catalogue"

    override fun create(): Interface<*> =
        buildCombinedInterface {
            rows = 1

            withTransform { pane, _ ->
                pane[3, 3] =
                    StaticElement(
                        Drawable.drawable(Material.STICK),
                    ) { (player) ->
                        runBlocking {
                            ChangingTitleExampleInterface().create().open(player)
                        }
                    }
            }
        }
}
