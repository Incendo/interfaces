package org.incendo.interfaces.next.transform

import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.properties.Trigger

public interface ReactiveTransform<P : Pane> : Transform<P> {

    public val triggers: Array<Trigger>
}
