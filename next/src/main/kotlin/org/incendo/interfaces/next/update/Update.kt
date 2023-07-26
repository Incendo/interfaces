package org.incendo.interfaces.next.update

import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.view.AbstractInterfaceView

public sealed interface Update {

    public suspend fun <P : Pane> apply(target: AbstractInterfaceView<*, P>)
}
