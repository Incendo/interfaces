package org.incendo.interfaces.click;

import java.util.concurrent.CompletableFuture;

public final class CompletableClickHandler {

    private CompletableFuture<Void> deferred = new CompletableFuture<>();
    private boolean cancelled = true;
    private boolean completingLater = false;

    public boolean complete() {
        if (this.deferred.isDone()) {
            return false;
        }
        this.deferred.complete(null);
        return true;
    }

    public void cancel() {
        if (!this.deferred.isDone()) {
            this.deferred.cancel(true);
        }
    }

    public CompletableClickHandler onComplete(final Runnable handler) {
        this.deferred.whenComplete((result, throwable) -> {
            //todo: error handling
            handler.run();
        });
        return this;
    }

    public boolean cancelled() {
        return this.cancelled;
    }

    public void cancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean completingLater() {
        return this.completingLater;
    }

    public void completingLater(final boolean completingLater) {
        this.completingLater = completingLater;
    }

}
