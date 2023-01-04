package org.incendo.interfaces.next.view

import net.kyori.adventure.text.Component

public interface InterfaceView {

    public fun open()

    public fun close()

    public fun back()

    public fun title(value: Component)
}
