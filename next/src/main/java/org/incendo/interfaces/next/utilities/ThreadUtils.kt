package org.incendo.interfaces.next.utilities

import org.bukkit.Bukkit
import org.bukkit.plugin.java.PluginClassLoader

internal fun runSync(function: () -> Unit) {
    if (Bukkit.isPrimaryThread()) {
        function()
        return
    }

    val plugin = (function::class.java.classLoader as PluginClassLoader).plugin
    Bukkit.getScheduler().callSyncMethod(plugin, function).get()
}
