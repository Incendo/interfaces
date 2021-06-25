package dev.kscott.interfaces.core;

/**
 * Represents an interface that can update.
 */
public interface UpdatingInterface {

    /**
     * Returns a boolean; true if updating, false if not
     *
     * @return true if updating interface, false if not
     */
    boolean updates();

    /**
     * Returns the update delay.
     *
     * @return the update delay
     */
    int updateDelay();

}
