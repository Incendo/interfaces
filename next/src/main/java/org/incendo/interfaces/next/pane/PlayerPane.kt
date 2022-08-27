package org.incendo.interfaces.next.pane

import org.incendo.interfaces.next.element.Element

public class PlayerPane : Pane() {

    public fun hotbar(column: Int, element: Element) {
        set(column, 0, element)
    }
}
