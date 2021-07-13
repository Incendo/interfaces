package org.incendo.interfaces.kotlin

import org.incendo.interfaces.core.Interface
import org.incendo.interfaces.core.arguments.InterfaceArguments
import org.incendo.interfaces.core.pane.Pane
import org.incendo.interfaces.core.view.InterfaceView
import org.incendo.interfaces.core.view.InterfaceViewer

/** The parent interface. */
public val <T : Pane, U : InterfaceViewer> InterfaceView<T, U>.parent: Interface<T, U>
    get() = this.backing()

/** The viewer of this view. */
public val InterfaceView<*, *>.viewer: InterfaceViewer
    get() = this.viewer()

/** Whether the viewer is currently viewing this view. */
public val InterfaceView<*, *>.viewing: Boolean
    get() = this.viewing()

/** The argument provided to this view. */
public val InterfaceView<*, *>.arguments: InterfaceArguments
    get() = this.arguments()

/** The pane. */
public val <T : Pane> InterfaceView<T, *>.pane: T
    get() = this.pane()
