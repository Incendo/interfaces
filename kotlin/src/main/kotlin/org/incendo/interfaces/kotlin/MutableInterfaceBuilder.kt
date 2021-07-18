package org.incendo.interfaces.kotlin

import org.incendo.interfaces.core.click.ClickContext
import org.incendo.interfaces.core.click.ClickHandler
import org.incendo.interfaces.core.pane.Pane
import org.incendo.interfaces.core.view.InterfaceViewer

public interface MutableInterfaceBuilder<
    T : Pane, U, V : InterfaceViewer, W : ClickContext<T, U, V>> {

    /**
     * Returns a {@code ClickHandler} that cancels the event and then calls the given click handler.
     *
     * @param clickHandler the handler
     * @param <T> pane type
     * @param <U> click cause
     * @param <V> viewer type
     * @param <W> context type
     * @return the handler
     */
    public fun canceling(
        clickHandler: ClickHandler<T, U, V, W> = ClickHandler {}
    ): ClickHandler<T, U, V, W> = ClickHandler.canceling(clickHandler)
}
