package org.incendo.interfaces.utilities;

import net.kyori.adventure.text.Component;

public final class TitleState {

    private Component current;
    private boolean hasChanged = false;

    public TitleState(final Component initialState) {
        this.current = initialState;
    }

    public Component current() {
        this.hasChanged = false;
        return this.current;
    }

    public void current(final Component value) {
        if (this.current.equals(value)) {
            return;
        }

        this.hasChanged = true;
        this.current = value;
    }

    public boolean hasChanged() {
        return this.hasChanged;
    }

}
