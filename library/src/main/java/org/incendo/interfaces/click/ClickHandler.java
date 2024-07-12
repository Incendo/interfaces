package org.incendo.interfaces.click;

public interface ClickHandler {
    ClickHandler EMPTY = ($, ctx) -> {};
    ClickHandler ALLOW = (completion, ctx) -> completion.cancelled(true);

    // should be a better way to do this
    static CompletableClickHandler process(ClickHandler clickHandler, ClickContext context) {
        CompletableClickHandler completableClickHandler = new CompletableClickHandler();

        clickHandler.handle(
            completableClickHandler,
            context
        );

        return completableClickHandler;
    }


    void handle(CompletableClickHandler completionHandler, ClickContext context);
}
