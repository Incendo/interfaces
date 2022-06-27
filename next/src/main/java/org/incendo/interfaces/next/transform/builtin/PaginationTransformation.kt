package org.incendo.interfaces.next.transform.builtin

import org.incendo.interfaces.next.element.Element
import org.incendo.interfaces.next.grid.GridPositionGenerator
import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.properties.InterfaceProperty
import org.incendo.interfaces.next.properties.Trigger
import org.incendo.interfaces.next.transform.ReactiveTransform

public class PaginationTransformation(
    default: Collection<Element>,
    private val positionGenerator: GridPositionGenerator
) : ReactiveTransform {

    private val pageProperty = InterfaceProperty(0)
    private val page by pageProperty

    private val valuesProperty = InterfaceProperty(default.toList())
    private val values by valuesProperty

    override val triggers: Array<Trigger> = arrayOf(pageProperty, valuesProperty)

    override fun invoke(pane: Pane) {
        val positions = positionGenerator.generate()
        val slots = positions.size

        val offset = page * slots

        positions.forEachIndexed { index, point ->
            pane[point] = values[index + offset]
        }
    }

}
