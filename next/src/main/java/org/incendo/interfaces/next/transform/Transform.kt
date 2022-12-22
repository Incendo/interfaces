package org.incendo.interfaces.next.transform

import org.incendo.interfaces.next.pane.Pane

public fun interface Transform<P : Pane> : suspend (P) -> Unit
