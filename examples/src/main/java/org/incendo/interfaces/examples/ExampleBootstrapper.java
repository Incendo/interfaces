package org.incendo.interfaces.examples;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.Command;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.interfaces.examples.cloud.ExampleSenderMapper;
import org.incendo.interfaces.examples.cloud.PlayerSender;
import org.incendo.interfaces.examples.interfaces.DelayedRequestInterface;

import java.util.List;

@SuppressWarnings("ALL")
public final class ExampleBootstrapper implements PluginBootstrap {

    private static final List<RegistrableInterface> INTERFACES = List.of(
        new DelayedRequestInterface()
    );

    private PaperCommandManager.Bootstrapped<CommandSourceStack> commandManager;

    @Override
    public void bootstrap(final BootstrapContext bootstrapContext) {
        this.commandManager = PaperCommandManager.builder(new ExampleSenderMapper())
            .executionCoordinator(ExecutionCoordinator.asyncCoordinator())
            .buildBootstrapped(bootstrapContext);

        this.loadCommands();
    }

    private void loadCommands() {
        Command.Builder<PlayerSender> root = this.commandManager
            .commandBuilder("interfaces")
            .senderType(PlayerSender.class);

        for (RegistrableInterface inventory : INTERFACES) {
            this.commandManager.command(
                root.literal(inventory.subcommand())
                    .handler(context -> {
                        try {
                            inventory.create().open(context.sender().getSender(), null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    })
            );
        }
    }

    @Override
    public JavaPlugin createPlugin(final PluginProviderContext context) {
        return new ExamplePlugin(this.commandManager);
    }

}
