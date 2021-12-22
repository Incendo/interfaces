package org.incendo.interfaces.next.update

import org.incendo.interfaces.next.interfaces.Interface
import org.incendo.interfaces.next.pane.Pane

public sealed interface Update {

    public fun <P : Pane> apply(target: Interface<P>)
}
