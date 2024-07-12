package org.incendo.interfaces.examples.cloud;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public final class PlayerSender implements CommandSourceStack {

    private final CommandSourceStack backing;
    private final Player player;

    public PlayerSender(final CommandSourceStack backing, final Player player) {
        this.backing = backing;
        this.player = player;
    }

    @Override
    public Location getLocation() {
        return this.backing.getLocation();
    }

    @Override
    public Player getSender() {
        return this.player;
    }

    @Override
    public Entity getExecutor() {
        return this.backing.getExecutor();
    }

}
