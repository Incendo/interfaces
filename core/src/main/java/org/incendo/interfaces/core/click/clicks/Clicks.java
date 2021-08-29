package org.incendo.interfaces.core.click.clicks;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.click.Click;

@SuppressWarnings("unused")
public final class Clicks {

    private Clicks() {
    }

    /**
     * Returns a new left-click instance
     *
     * @param cause    the click cause
     * @param shift    whether or not the click was a shift-click
     * @param interact whether or nor the click was triggered by an interact event
     * @param <T> the cause type
     * @return the click
     */
    public static <T> @NonNull Click<T> leftClick(
            final @NonNull T cause,
            final boolean shift,
            final boolean interact
    ) {
        return new LeftClick<>(
                cause,
                shift,
                interact
        );
    }

    /**
     * Returns a new middle-click instance
     *
     * @param cause    the click cause
     * @param shift    whether or not the click was a shift-click
     * @param <T> the cause type
     * @return the click
     */
    public static <T> @NonNull Click<T> middleClick(
            final @NonNull T cause,
            final boolean shift
    ) {
        return new MiddleClick<>(
                cause,
                shift
        );
    }

    /**
     * Returns a new right-click instance
     *
     * @param cause    the click cause
     * @param shift    whether or not the click was a shift-click
     * @param interact whether or nor the click was triggered by an interact event
     * @param <T> the cause type
     * @return the click
     */
    public static <T> @NonNull Click<T> rightClick(
            final @NonNull T cause,
            final boolean shift,
            final boolean interact
    ) {
        return new RightClick<>(
                cause,
                shift,
                interact
        );
    }

    /**
     * Returns a new click for an unknown click type
     *
     * @param cause the click cause
     * @param <T> the cause type
     * @return the click
     */
    public static <T> @NonNull Click<T> unknownClick(
            final @NonNull T cause
    ) {
        return new UnknownClick<>(
                cause
        );
    }
}
