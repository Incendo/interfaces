package dev.kscott.interfaces.core;

/**
 * Represents an interface that can update.
 */
public interface UpdatingInterface {

    /**
     * Controls if this interface updates.
     *
     * @param updates is updating or not
     */
    void updates(boolean updates);

    /**
     * Controls if this interface updates.
     *
     * @param updates is updating or not
     * @param delay   the time to wait between updates
     */
    void updates(boolean updates, int delay);

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
