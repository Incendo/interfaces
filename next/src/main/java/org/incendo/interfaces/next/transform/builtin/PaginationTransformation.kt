package org.incendo.interfaces.next.transform.builtin

import org.incendo.interfaces.next.element.Element
import org.incendo.interfaces.next.grid.GridPositionGenerator
import org.incendo.interfaces.next.pane.Pane
import kotlin.properties.Delegates

public class PaginationTransformation<P : Pane>(
    private val positionGenerator: GridPositionGenerator,
    default: Collection<Element>,
    back: PaginationButton,
    forward: PaginationButton
) : PagedTransformation<P>(back, forward) {

    private val values by Delegates.observable(default.toList()) { _, _, _ ->
        boundPage.max = maxPages()
    }

    init {
        boundPage.max = maxPages()
    }

    override suspend fun invoke(pane: P) {
        val positions = positionGenerator.generate()
        val slots = positions.size

        val offset = page * slots

        positions.forEachIndexed { index, point ->
            pane[point] = values[index + offset]
        }

        super.invoke(pane)
    }

    private fun maxPages(): Int {
        return values.size.floorDiv(positionGenerator.generate().size)
    }
}
