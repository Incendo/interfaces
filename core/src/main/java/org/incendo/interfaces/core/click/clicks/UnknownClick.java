package org.incendo.interfaces.core.click.clicks;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.click.Click;

/**
 * Unknown click type.
 *
 * @param <T> cause type
 */
public final class UnknownClick<T> implements Click<T> {

    private final T cause;

    UnknownClick(
            final @NonNull T cause
    ) {
        this.cause = cause;
    }

    @Override
    public @NonNull T cause() {
        return this.cause;
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
        return false;
    }

    @Override
    public boolean shiftClick() {
        return false;
    }

}
