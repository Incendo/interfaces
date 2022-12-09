package org.incendo.interfaces.next.update

import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.view.InterfaceView

public sealed interface Update {

    public suspend fun <P : Pane> apply(target: InterfaceView<*, P>)
}
