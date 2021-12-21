package org.incendo.interfaces.next.transform

internal class AppliedTransform(private val id: Int, transform: Transform) : Transform by transform
