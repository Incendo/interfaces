package org.incendo.interfaces.next.transform

internal class AppliedTransform(
    internal val priority: Int,
    transform: Transform
) : Transform by transform
