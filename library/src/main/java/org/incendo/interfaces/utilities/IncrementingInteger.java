package org.incendo.interfaces.utilities;

public final class IncrementingInteger {
    private int value = 0;

    public int next() {
        return this.value++;
    }
}
