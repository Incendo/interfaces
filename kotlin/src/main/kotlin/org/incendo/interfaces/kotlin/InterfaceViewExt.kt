package org.incendo.interfaces.kotlin

import org.incendo.interfaces.core.Interface
import org.incendo.interfaces.core.arguments.InterfaceArgument
import org.incendo.interfaces.core.pane.Pane
import org.incendo.interfaces.core.view.InterfaceView
import org.incendo.interfaces.core.view.InterfaceViewer

/** The parent interface. */
public val <T : Pane, U : InterfaceViewer> InterfaceView<T, U, *>.parent: Interface<T, U>
    get() = this.parent()

/** The viewer of this view. */
public val InterfaceView<*, *, *>.viewer: InterfaceViewer
    get() = this.viewer()

/** Whether the viewer is currently viewing this view. */
public val InterfaceView<*, *, *>.viewing: Boolean
    get() = this.viewing()

/** The argument provided to this view. */
public val InterfaceView<*, *, *>.argument: InterfaceArgument
    get() = this.argument()

/** The pane. */
public val <T : Pane> InterfaceView<T, *, *>.pane: T
    get() = this.pane()
