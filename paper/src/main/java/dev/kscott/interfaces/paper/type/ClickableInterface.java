package dev.kscott.interfaces.paper.type;

import dev.kscott.interfaces.paper.element.ClickHandler;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a clickable interface.
 * <p>
 * {@code clickHandler} will be called whenever the viewer clicks on the interface.
 */
public interface ClickableInterface {

    /**
     * Returns the top click handler.
     *
     * @return the top click handler
     */
    @NonNull ClickHandler clickHandler();

}
