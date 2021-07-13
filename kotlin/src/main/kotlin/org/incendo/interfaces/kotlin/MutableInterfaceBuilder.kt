package org.incendo.interfaces.kotlin

import org.incendo.interfaces.core.click.ClickContext
import org.incendo.interfaces.core.click.ClickHandler
import org.incendo.interfaces.core.pane.Pane

public interface MutableInterfaceBuilder<T : Pane, U, V : ClickContext<T, U>> {

    /**
     * Returns a {@code ClickHandler} that cancels the event and then calls the given click handler.
     *
     * @param clickHandler the handler
     * @param <T> pane type
     * @param <U> click cause
     * @return the handler
     */
    public fun canceling(
        clickHandler: ClickHandler<T, U, V> = ClickHandler {}
    ): ClickHandler<T, U, V> = ClickHandler.canceling(clickHandler)
}
