package org.incendo.interfaces.transform

import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.properties.Trigger

public class AppliedTransform<P : Pane>(
    internal val priority: Int,
    internal val triggers: Set<Trigger>,
    transform: Transform<P>
) : Transform<P> by transform
