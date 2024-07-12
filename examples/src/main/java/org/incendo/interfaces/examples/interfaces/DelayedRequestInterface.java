package org.incendo.interfaces.examples.interfaces;

import org.bukkit.Material;
import org.incendo.interfaces.element.StaticElement;
import org.incendo.interfaces.examples.RegistrableInterface;
import org.incendo.interfaces.examples.utilities.ConcurrencyUtilities;
import org.incendo.interfaces.interfaces.ChestInterfaceBuilder;
import org.incendo.interfaces.interfaces.Interface;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.kyori.adventure.text.Component.text;
import static org.incendo.interfaces.drawable.Drawable.drawable;

public final class DelayedRequestInterface implements RegistrableInterface {

    private static final StaticElement BACKING_ELEMENT = new StaticElement(drawable(Material.GRAY_CONCRETE));

    @Override
    public String subcommand() {
        return "delayed";
    }

    @Override
    public Interface<?> create() {
        ChestInterfaceBuilder builder = new ChestInterfaceBuilder()
            .initialTitle(text(this.subcommand()))
            .rows(2);

        builder.withTransform((pane, view) -> {
            List<Material> materials = ConcurrencyUtilities.supressedGet(this.data());

            for (int index = 0; index < materials.size(); index++) {
                pane.put(0, index, new StaticElement(drawable(materials.get(index))));
            }
        });

        builder.withTransform((pane, view) -> {
            for (int index = 0; index <= 8; index++) {
                pane.put(1, index, BACKING_ELEMENT);
            }
        });

        return builder.build();
    }

    private CompletableFuture<List<Material>> data() {
        return CompletableFuture.supplyAsync(() -> {
            ConcurrencyUtilities.sleep(5);
            return List.of(Material.GREEN_CONCRETE, Material.YELLOW_CONCRETE, Material.RED_CONCRETE);
        });
    }
}
