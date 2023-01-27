package org.incendo.interfaces.next.utilities

import net.kyori.adventure.text.Component

internal class TitleState(initialState: Component?) {

    internal var current = initialState
        set(value) {
            hasChanged = true
            field = value
        }
        get() {
            hasChanged = false
            return field
        }

    internal var hasChanged = false
}
