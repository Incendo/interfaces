package org.incendo.interfaces.element;

import org.incendo.interfaces.click.ClickHandler;
import org.incendo.interfaces.drawable.Drawable;

public record StaticElement(Drawable drawable, ClickHandler clickHandler) implements Element {

    public StaticElement(final Drawable drawable) {
        this(drawable, ClickHandler.EMPTY);
    }

}
