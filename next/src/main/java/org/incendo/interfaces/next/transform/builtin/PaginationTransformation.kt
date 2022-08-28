package org.incendo.interfaces.next.transform.builtin

import org.bukkit.event.inventory.ClickType
import org.incendo.interfaces.next.drawable.Drawable
import org.incendo.interfaces.next.element.Element
import org.incendo.interfaces.next.element.StaticElement
import org.incendo.interfaces.next.grid.GridPoint
import org.incendo.interfaces.next.grid.GridPositionGenerator
import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.properties.InterfaceProperty
import org.incendo.interfaces.next.properties.Trigger
import org.incendo.interfaces.next.transform.ReactiveTransform
import org.incendo.interfaces.next.utilities.BoundInteger

public class PaginationTransformation<P : Pane>(
    default: Collection<Element>,
    private val positionGenerator: GridPositionGenerator,
    private val back: ButtonInformation?,
    private val forward: ButtonInformation?
) : ReactiveTransform<P> {

    private val boundPage = BoundInteger(0, 1, default.size)
    private var page by boundPage

    private val pageProperty = InterfaceProperty(page)

    private val valuesProperty = InterfaceProperty(default.toList()) { entries -> boundPage.max = entries.size }
    private val values by valuesProperty

    override val triggers: Array<Trigger> = arrayOf(pageProperty, valuesProperty)

    override fun invoke(pane: P) {
        val positions = positionGenerator.generate()
        val slots = positions.size

        val offset = page * slots

        positions.forEachIndexed { index, point ->
            pane[point] = values[index + offset]
        }

        back?.let { button -> applyButton(pane, button) }
        forward?.let { button -> applyButton(pane, button) }
    }

    private fun applyButton(pane: Pane, button: ButtonInformation) {
        val (point, drawable, increments) = button

        pane[point] = StaticElement.syncHandler(drawable) { click ->
            increments[click.type]?.let { increment -> page += increment }
        }
    }

    // todo(josh): move this out
    public data class ButtonInformation(
        public val position: GridPoint,
        public val drawable: Drawable,
        public val increments: Map<ClickType, Int>
    )
}
