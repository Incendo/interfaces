package org.incendo.interfaces.click;

public interface ClickHandler {
    ClickHandler EMPTY = ctx -> {};
    ClickHandler ALLOW = ctx -> { ctx.cancelled(false); };

    void handle(ClickContext context);
}
