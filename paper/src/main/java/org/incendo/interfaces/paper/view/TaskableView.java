package org.incendo.interfaces.paper.view;

import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

/**
 * Represents an interface view that is able to schedule and execute tasks.
 * These tasks are scheduled against itself meaning if the view is closed the task will not be executed.
 */
public interface TaskableView {

    /**
     * Add a task to the view
     *
     * @param plugin the plugin instance to register against
     * @param runnable the runnable to execute
     * @param delay the ticks to wait before executing
     */
    void addTask(@NonNull Plugin plugin, @NonNull Runnable runnable, int delay);

    /**
     * Retrieve all scheduled tasks
     *
     * @return collection of bukkit tasks that have been scheduled
     */
    Collection<Integer> taskIds();

}
