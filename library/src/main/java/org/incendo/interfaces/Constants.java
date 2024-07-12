package org.incendo.interfaces;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Constants {

    public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(
        16,
        Thread.ofPlatform().name("interfaces-", 1).factory()
    );

    private Constants() {
    }
}
