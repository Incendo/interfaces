package org.incendo.interfaces.next.transform.builtin

import org.bukkit.event.inventory.ClickType
import org.incendo.interfaces.next.drawable.Drawable
import org.incendo.interfaces.next.element.StaticElement
import org.incendo.interfaces.next.grid.GridPoint
import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.properties.Trigger
import org.incendo.interfaces.next.transform.ReactiveTransform
import org.incendo.interfaces.next.utilities.BoundInteger
import org.incendo.interfaces.next.view.InterfaceView

public abstract class PagedTransformation<P : Pane>(
    private val back: PaginationButton,
    private val forward: PaginationButton
) : ReactiveTransform<P> {

    protected val boundPage: BoundInteger = BoundInteger(0, 1, Integer.MAX_VALUE)
    protected var page: Int by boundPage

    override suspend fun invoke(pane: P, view: InterfaceView) {
        if (boundPage.hasPreceeding()) {
            applyButton(pane, back)
        }

        if (boundPage.hasSucceeding()) {
            applyButton(pane, forward)
        }
    }

    protected open fun applyButton(pane: Pane, button: PaginationButton) {
        val (point, drawable, increments) = button

        pane[point] = StaticElement(drawable) { click ->
            increments[click.type]?.let { increment -> page += increment }
        }
    }

    override val triggers: Array<Trigger> = arrayOf(boundPage)
}

public data class PaginationButton(
    public val position: GridPoint,
    public val drawable: Drawable,
    public val increments: Map<ClickType, Int>
)
