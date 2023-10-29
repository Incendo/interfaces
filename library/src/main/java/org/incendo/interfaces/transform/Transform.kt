package org.incendo.interfaces.transform

import org.incendo.interfaces.next.pane.Pane
import org.incendo.interfaces.next.view.InterfaceView

public fun interface Transform<P : Pane> : suspend (P, InterfaceView) -> Unit
