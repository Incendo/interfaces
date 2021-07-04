package org.incendo.interfaces.kotlin

import org.incendo.interfaces.core.Interface
import org.incendo.interfaces.core.arguments.InterfaceArgument
import org.incendo.interfaces.core.pane.Pane
import org.incendo.interfaces.core.view.InterfaceView
import org.incendo.interfaces.core.view.InterfaceViewer

/** The parent interface. */
public val <T : Pane, U : InterfaceViewer, V : Interface<T, U>> InterfaceView<T, U, V>.parent:
    Interface<T, U>
  get() = this.parent()

/** The viewer of this view. */
public val <T : Pane, U : InterfaceViewer, V : Interface<T, U>> InterfaceView<T, U, V>.viewer:
    InterfaceViewer
  get() = this.viewer()

/** Whether the viewer is currently viewing this view. */
public val <T : Pane, U : InterfaceViewer, V : Interface<T, U>> InterfaceView<T, U, V>.viewing:
    Boolean
  get() = this.viewing()

/** The argument provided to this view. */
public val <T : Pane, U : InterfaceViewer, V : Interface<T, U>> InterfaceView<T, U, V>.argument:
    InterfaceArgument
  get() = this.argument()

/** The pane. */
public val <T : Pane, U : InterfaceViewer, V : Interface<T, U>> InterfaceView<T, U, V>.pane: T
  get() = this.pane()
