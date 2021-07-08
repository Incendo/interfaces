package org.incendo.interfaces.core.view;

/**
 * Represents an Interface View that does not need to be reopened to be refreshed
 */
public interface SelfUpdatingInterfaceView {

    /**
     * Returns a boolean; true if updating, false if not
     *
     * @return true if updating interface, false if not
     */
    boolean updates();

}
