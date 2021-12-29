package org.incendo.interfaces.next.transform

import org.incendo.interfaces.next.properties.Trigger

public class AppliedTransform(
    internal val priority: Int,
    internal val triggers: Array<out Trigger>,
    transform: Transform
) : Transform by transform
