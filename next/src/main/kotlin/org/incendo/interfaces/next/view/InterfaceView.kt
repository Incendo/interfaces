package org.incendo.interfaces.next.view

import net.kyori.adventure.text.Component

public interface InterfaceView {

    public fun open()

    public fun close()

    // todo(josh): temporarily done for interfaces 1 shim.
    //            should we keep it?
    public fun parent(): InterfaceView?

    public fun back()

    public fun title(value: Component)
}
