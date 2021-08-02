package org.incendo.interfaces.paper.view;

import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Set;

final class BukkitNestedRunnable extends BukkitRunnable {

    private final Set<Integer> tasks;
    private final Runnable runnable;

    BukkitNestedRunnable(
            final @NonNull Set<Integer> tasks,
            final @NonNull Runnable runnable
    ) {
        this.tasks = tasks;
        this.runnable = runnable;
    }

    @Override
    public void run() {
        this.runnable.run();
        this.tasks.remove(this.getTaskId());
    }

}
