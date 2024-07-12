package org.incendo.interfaces.examples.cloud;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.incendo.cloud.SenderMapper;

@SuppressWarnings("UnstableApiUsage")
public final class ExampleSenderMapper implements SenderMapper<CommandSourceStack, CommandSourceStack> {

    @Override
    public CommandSourceStack map(final CommandSourceStack base) {
        if (base.getSender() instanceof Player player) {
            return new PlayerSender(base, player);
        }

        return base;
    }

    @Override
    public CommandSourceStack reverse(final CommandSourceStack mapped) {
        return mapped;
    }

}
