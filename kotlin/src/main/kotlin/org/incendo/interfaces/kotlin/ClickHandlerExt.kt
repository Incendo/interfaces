package org.incendo.interfaces.kotlin

import org.incendo.interfaces.core.click.ClickContext
import org.incendo.interfaces.core.click.ClickHandler
import org.incendo.interfaces.core.pane.Pane
import org.incendo.interfaces.core.view.InterfaceViewer

/**
 * Returns a new {@link ClickHandler} that first executes the current handler, and then executes the
 * other handler.
 *
 * @param other the other handler
 * @return a combination of this and the given click handler
 */
public operator fun <T : Pane, U, V : InterfaceViewer, W : ClickContext<T, U, V>> ClickHandler<
    T, U, V, W>.plus(other: ClickHandler<T, U, V, W>): ClickHandler<T, U, V, W> =
    this.andThen(other)
