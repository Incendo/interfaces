package org.incendo.interfaces.examples.interfaces;

import org.bukkit.Material;
import org.incendo.interfaces.element.StaticElement;
import org.incendo.interfaces.examples.RegistrableInterface;
import org.incendo.interfaces.interfaces.ChestInterfaceBuilder;
import org.incendo.interfaces.interfaces.Interface;
import org.incendo.interfaces.properties.InterfaceProperty;

import static love.broccolai.corn.minecraft.item.ItemBuilder.itemBuilder;
import static net.kyori.adventure.text.Component.text;
import static org.incendo.interfaces.drawable.Drawable.drawable;

public final class ReactiveInterface implements RegistrableInterface {

    private static final StaticElement BACKING_ELEMENT = new StaticElement(drawable(Material.GRAY_CONCRETE));

    @Override
    public String subcommand() {
        return "reactive";
    }

    @Override
    public Interface<?> create() {
        ChestInterfaceBuilder builder = new ChestInterfaceBuilder()
            .initialTitle(text(this.subcommand()))
            .rows(2);

        InterfaceProperty<Integer> counter = InterfaceProperty.interfaceProperty(0);

        builder.withTransform((pane, view) -> {
            pane.put(0, 4, new StaticElement(
                drawable(itemBuilder(Material.DIAMOND)
                    .name(text("Counter: " + counter.value()))
                    .build()
                ),
                (player, clickType) -> counter.value(counter.value() + 1)
            ));
        }, counter);

        builder.withTransform((pane, view) -> {
            for (int index = 0; index <= 8; index++) {
                pane.put(1, index, BACKING_ELEMENT);
            }
        });

        return builder.build();
    }

}
