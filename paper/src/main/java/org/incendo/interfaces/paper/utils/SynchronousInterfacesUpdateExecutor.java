package org.incendo.interfaces.paper.utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public final class SynchronousInterfacesUpdateExecutor implements InterfacesUpdateExecutor  {

    @Override
    public void execute(final Plugin plugin, final Runnable runnable) {
        if (Bukkit.isPrimaryThread()) {
            runnable.run();
            return;
        }

        Future<Void> completionFuture = Bukkit.getScheduler().callSyncMethod(plugin, () -> {
           runnable.run();
           return null;
        });

        try {
            completionFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
