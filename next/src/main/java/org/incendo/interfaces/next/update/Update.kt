package org.incendo.interfaces.next.update

import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.transform.Transform

public sealed interface Update {

    public fun apply(pane: Pane, transforms: Collection<Transform>)

}
