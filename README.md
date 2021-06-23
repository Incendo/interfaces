# `interfaces`

_Building interfaces since 2021._

`interfaces` is a builder-style user interface library designed to make creation of user interfaces as easy as possible.

`interfaces-paper` implements `interfaces` using the [Paper Minecraft server API](https://papermc.io).

# Example

# Usage

_Gradle instructions coming soon._

### Creating an interface with an updating clock.

```java
ChestInterface menuInterface = Interface.chest(4)
        .updating(true) // Sets this interface to updating
        .updateTicks(2) // This interface will now update every 2 ticks
        // Fill the background with bgItem
        .transform(Transform.gridFill(Element.item(
                bgItem,
                (event, view) -> event.setCancelled(true)))
        )
        .transform(Transform.gridItem(Element.item(diamondItem), 1, 1)) // Add an item and x=1 y=1
        // Adds a clock timer (which will update every 2 ticks)
        .transform(Transform.grid((grid, view) -> {
            // Get arguments
            final @NonNull ChestView chestView = (ChestView) view;
            final @NonNull Long time = chestView.arguments().get("time");
            // Add clock element
            grid.element(Element.item(
                    PaperItemBuilder.paper(Material.CLOCK)
                            .name(Component.text("Time: "+time))
                            .build()
            ), 1, 2);
        }))
        .title(Component.text("/menu"));

// Opens the menu with the time argument given.
// Since InterfaceArguments accept a supplier, passing in System::currentTimeMillis will
// provide the latest time every interface update.
menuInterface.open(player, InterfaceArguments.with("time", System::currentTimeMillis));
```

_Note: this may not reflect the latest iteration of the `interfaces` API._

# Credits

Thanks to [broccolai](https://github.com/broccolai) for letting me steal basically the entire project setup.
