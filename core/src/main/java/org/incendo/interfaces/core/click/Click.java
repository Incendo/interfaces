package org.incendo.interfaces.core.click;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Represents a class that holds data about a click.
 *
 * @param <T> the click cause
 */
@SuppressWarnings("unused")
public interface Click<T> {

    /**
     * Returns the click cause
     *
     * @return the click cause
     */
    @NonNull T cause();

    /**
     * Returns true if this click was a right click.
     *
     * @return true if this click was a right click
     */
    boolean rightClick();

    /**
     * Returns true if this click was a left click.
     *
     * @return true if this click was a left click
     */
    boolean leftClick();

    /**
     * Returns true if this click was a middle click.
     *
     * @return true if this click was a middle click
     */
    boolean middleClick();

    /**
     * Returns true if this click was a shift click.
     *
     * @return true if this click was a shift click
     */
    boolean shiftClick();

}
