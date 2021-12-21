package org.incendo.interfaces.next.pane

import org.incendo.interfaces.next.element.Element
import org.incendo.interfaces.next.transform.Transform
import org.incendo.interfaces.next.utilities.LayeredGridMap

public abstract class Pane {
    private val backing = LayeredGridMap<Element>()

    public fun apply(priority: Int, transform: Transform) {
        val layer = backing[priority]
        transform(layer)
    }
    
}
