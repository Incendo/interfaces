package org.incendo.interfaces.kotlin

import org.incendo.interfaces.core.pane.Pane
import org.incendo.interfaces.paper.element.ClickHandler

public interface MutableInterfaceBuilder<T : Pane> {

    /**
     * Returns a {@code ClickHandler} that cancels the event and then calls the given click handler.
     *
     * @param clickHandler the handler
     * @param <T> pane type
     * @return the handler
     */
    public fun canceling(
            clickHandler: ClickHandler<T> = ClickHandler { _, _ -> }
    ): ClickHandler<T> = ClickHandler.canceling(clickHandler)
}
