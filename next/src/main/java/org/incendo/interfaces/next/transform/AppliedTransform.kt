package org.incendo.interfaces.next.transform

internal class AppliedTransform(
    internal val id: Int,
    internal val priority: Int,
    transform: Transform
) : Transform by transform
