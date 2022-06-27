package org.incendo.interfaces.next.transform

import org.incendo.interfaces.next.properties.Trigger

public interface ReactiveTransform : Transform {

    public val triggers: Array<Trigger>

}
