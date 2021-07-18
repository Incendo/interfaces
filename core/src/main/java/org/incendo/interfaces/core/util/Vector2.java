package org.incendo.interfaces.core.util;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

/**
 * A 2-dimensional vector storing integer components.
 */
public final class Vector2 {

    private final int x;
    private final int y;

    Vector2(
            final int x,
            final int y
    ) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a new vector containing the given components
     *
     * @param x x component
     * @param y y component
     * @return the vector
     */
    public static @NonNull Vector2 at(
            final int x,
            final int y
    ) {
        return new Vector2(x, y);
    }

    /**
     * Returns the x component
     *
     * @return x component
     */
    public int x() {
        return this.x;
    }

    /**
     * Returns the y component
     *
     * @return y component
     */
    public int y() {
        return this.y;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Vector2 vector2 = (Vector2) o;
        return this.x() == vector2.x() && this.y() == vector2.y();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x(), this.y());
    }

    //<editor-fold desc="Kotlin Support">
    /**
     * Returns the x component
     *
     * @return x component
     */
    public int getX() {
        return this.x();
    }

    /**
     * Returns the y component
     *
     * @return y component
     */
    public int getY() {
        return this.y();
    }

    /**
     * Returns the x component
     *
     * @return x component
     */
    public int component1() {
        return this.x();
    }

    /**
     * Returns the y component
     *
     * @return y component
     */
    public int component2() {
        return this.y();
    }
    //</editor-fold desc="Kotlin Support">

}
