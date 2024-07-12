package org.incendo.interfaces.utilities;

import org.incendo.interfaces.properties.DelegateTrigger;

public final class BoundInteger extends DelegateTrigger {

    private int value;
    private int min;
    private int max;

    public BoundInteger(final int initial, final int min, final int max) {
        this.value = initial;
        this.min = min;
        this.max = max;
    }

    public int value() {
        return this.value;
    }

    public BoundInteger value(final int newValue) {
        if (newValue >= this.min && newValue <= this.max) {
            this.value = newValue;
        } else {
            this.value = Math.min(Math.max(newValue, this.min), this.max);
        }

        this.trigger();
        return this;
    }

    public int min() {
        return this.min;
    }

    public void min(final int min) {
        this.min = min;
    }

    public int max() {
        return this.max;
    }

    public void max(final int max) {
        this.max = max;
    }

    public boolean hasSucceeding() {
        return this.value < this.max;
    }

    public boolean hasPreceding() {
        return this.value > this.min;
    }
}
