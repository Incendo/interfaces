package org.incendo.interfaces.interfaces;

import net.kyori.adventure.text.Component;
import org.incendo.interfaces.pane.ChestPane;

public final class ChestInterfaceBuilder extends AbstractInterfaceBuilder<ChestPane, ChestInterface> {

    private int rows = 0;
    private Component initialTitle = null;

    public int rows() {
        return this.rows;
    }

    public ChestInterfaceBuilder rows(final int rows) {
        this.rows = rows;
        return this;
    }

    public Component initialTitle() {
        return this.initialTitle;
    }

    public ChestInterfaceBuilder initialTitle(final Component initialTitle) {
        this.initialTitle = initialTitle;
        return this;
    }

    @Override
    public ChestInterface build() {
        return new ChestInterface(
            this.rows,
            this.initialTitle,
            this.closeHandlers(),
            this.transforms(),
            this.clickPreprocessors()
        );
    }
}
