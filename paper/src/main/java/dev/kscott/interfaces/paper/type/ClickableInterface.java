package dev.kscott.interfaces.paper.type;

import dev.kscott.interfaces.paper.element.ClickHandler;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a clickable interface.
 */
public interface ClickableInterface {

    /**
     * Returns the top click handler.
     *
     * @return the top click handler
     */
    @NonNull ClickHandler topClickHandler();

    /**
     * Sets the top click handler.
     *
     * @param handler the handler
     */
    void topClickHandler(final @NonNull ClickHandler handler);

}
