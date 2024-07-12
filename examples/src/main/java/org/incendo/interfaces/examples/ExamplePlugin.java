package org.incendo.interfaces.examples;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.interfaces.InterfacesListeners;

public final class ExamplePlugin extends JavaPlugin implements Listener {

    private final PaperCommandManager.Bootstrapped<CommandSourceStack> commandManager;

    public ExamplePlugin(final PaperCommandManager.Bootstrapped<CommandSourceStack> commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void onEnable() {
        this.commandManager.onEnable();
        InterfacesListeners.install(this);
    }

}
