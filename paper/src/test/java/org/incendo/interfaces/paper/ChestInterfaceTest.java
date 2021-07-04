package org.incendo.interfaces.paper;

import be.seeseemelk.mockbukkit.MockBukkit;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.incendo.interfaces.paper.type.ChestInterface;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ChestInterfaceTest {

    private static final Component TITLE = Component.text("Test Inventory");

    @Before
    public void setUp() {
        MockBukkit.mock();
    }

    @After
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void chestInterfaceTitle() {
        final ChestInterface chestInterface = ChestInterface.builder().title(TITLE).build();
        final Player player = MockBukkit.getMock().addPlayer();

        // Show the interface to the player.
        chestInterface.open(PlayerViewer.of(player));

        // Make sure the title was set correctly.
        Assertions.assertEquals(TITLE, player.getOpenInventory().title());
    }

}
