package org.incendo.interfaces.paper;

import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.interfaces.core.view.InterfaceViewer;
import org.incendo.interfaces.paper.pane.ChestPane;

/**
 * An interface viewer holding a {@link Player}.
 * <p>
 * Supports {@link ChestPane}.
 */
public interface PlayerViewer extends InterfaceViewer {

    /**
     * Returns a new {@code PlayerViewer} containing {@code player}.
     *
     * @param player the player
     * @return the viewer
     */
    static @NonNull PlayerViewer of(final @NonNull Player player) {
        return new PlayerViewerImpl(player);
    }

    /**
     * Returns the player.
     *
     * @return the player
     */
    @NonNull Player player();

}
