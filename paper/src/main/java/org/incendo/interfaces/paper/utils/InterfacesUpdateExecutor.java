package org.incendo.interfaces.paper.utils;

import org.bukkit.plugin.Plugin;

public interface InterfacesUpdateExecutor {

    /**
     * Run the given task via the executor
     *
     * @param plugin the plugin instance of the interface
     * @param runnable the runnable to execute
     */
    void execute(Plugin plugin, Runnable runnable);

}
