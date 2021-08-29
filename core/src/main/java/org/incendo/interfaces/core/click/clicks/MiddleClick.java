package org.incendo.interfaces.core.click.clicks;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.click.Click;

/**
 * A middle-click implementation of {@link Click}.
 *
 * @param <T> cause type
 */
public final class MiddleClick<T> implements Click<T> {

    private final T cause;
    private final boolean shift;

    MiddleClick(
            final @NonNull T cause,
            final boolean shift
    ) {
        this.cause = cause;
        this.shift = shift;
    }

    @Override
    public boolean rightClick() {
        return false;
    }

    @Override
    public boolean leftClick() {
        return false;
    }

    @Override
    public boolean middleClick() {
        return true;
    }

    @Override
    public boolean shiftClick() {
        return this.shift;
    }

    @Override
    public boolean interact() {
        return false;
    }

    @Override
    public @NonNull T cause() {
        return this.cause;
    }

}
