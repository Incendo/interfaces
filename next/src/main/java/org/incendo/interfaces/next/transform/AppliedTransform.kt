package org.incendo.interfaces.next.transform

public class AppliedTransform(
    internal val priority: Int,
    transform: Transform
) : Transform by transform
