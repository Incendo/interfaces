package org.incendo.interfaces.core.click.clicks;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.click.Click;

/**
 * A left-click implementation of {@link Click}.
 *
 * @param <T> cause type
 */
public final class LeftClick<T> implements Click<T> {

    private final T cause;
    private final boolean shift;
    private final boolean interact;

    LeftClick(
            final @NonNull T cause,
            final boolean shift,
            final boolean interact
    ) {
        this.cause = cause;
        this.shift = shift;
        this.interact = interact;
    }

    @Override
    public boolean rightClick() {
        return false;
    }

    @Override
    public boolean leftClick() {
        return true;
    }

    @Override
    public boolean middleClick() {
        return false;
    }

    @Override
    public boolean shiftClick() {
        return this.shift;
    }

    @Override
    public boolean interact() {
        return this.interact;
    }

    @Override
    public @NonNull T cause() {
        return this.cause;
    }

}
