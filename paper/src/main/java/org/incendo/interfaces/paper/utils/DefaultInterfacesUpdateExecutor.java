package org.incendo.interfaces.paper.utils;

import org.bukkit.plugin.Plugin;

/**
 * Runs the update on whatever thread the update was called from.
 */
public final class DefaultInterfacesUpdateExecutor implements InterfacesUpdateExecutor {

    @Override
    public void execute(final Plugin plugin, final Runnable runnable) {
        runnable.run();
    }

}
