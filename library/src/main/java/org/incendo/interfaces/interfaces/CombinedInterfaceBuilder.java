package org.incendo.interfaces.interfaces;

import net.kyori.adventure.text.Component;
import org.incendo.interfaces.pane.CombinedPane;

public final class CombinedInterfaceBuilder extends AbstractInterfaceBuilder<CombinedPane, CombinedInterface> {

    private int rows = 0;
    private Component initialTitle = null;

    public int rows() {
        return this.rows;
    }

    public void rows(final int rows) {
        this.rows = rows;
    }

    public Component initialTitle() {
        return this.initialTitle;
    }

    public void initialTitle(final Component initialTitle) {
        this.initialTitle = initialTitle;
    }

    @Override
    public CombinedInterface build() {
        return new CombinedInterface(
            this.rows,
            this.initialTitle,
            this.closeHandlers(),
            this.transforms(),
            this.clickPreprocessors()
        );
    }
}
