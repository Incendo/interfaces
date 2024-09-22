package org.incendo.interfaces.utilities;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.incendo.interfaces.InterfacesListeners;

public final class ThreadUtils {

    private ThreadUtils() {
    }

    public static void runSync(final Runnable function) {
        if (Bukkit.isPrimaryThread()) {
            function.run();
            return;
        }

        Plugin plugin = InterfacesListeners.instance().plugin();

        if (plugin == null) {
            throw new IllegalStateException("Could not find plugin for class " + function.getClass().getName());
        }

        try {
            Bukkit.getScheduler().callSyncMethod(plugin, () -> {
                function.run();
                return null;
            }).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
