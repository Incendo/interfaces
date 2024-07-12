package org.incendo.interfaces.examples.utilities;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public final class ConcurrencyUtilities {

    private ConcurrencyUtilities() {
    }

    public static void sleep(final int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static  <T> T supressedGet(final CompletableFuture<T> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
