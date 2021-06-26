# `interfaces`

_Building interfaces since 2021._

`interfaces` is a builder-style user interface library designed to make creation of flexible user interfaces as easy as possible.

## Packages

`interfaces-core` provides the core API classes.

`interfaces-paper` implements `interfaces` using the [Paper Minecraft server API](https://papermc.io). This package provides the
capability to construct a variety of Minecraft-based interfaces, including text, inventory (i.e. chests), and books.

## Terminology

#### Interface

An interface is the main class that you'll be interacting with. Interfaces hold a series of transformations and other values that
can be used to construct an InterfaceView.

#### InterfaceView ("view")

An InterfaceView represents a 'rendered' interface. An InterfaceView holds one pane and one InterfaceViewer.

#### InterfaceViewer ("viewer")

An InterfaceViewer represents an object that can view an interface. InterfaceViewers are provided panes to view.

#### Pane

A pane holds a collection of elements that make up the visual aspect of an interface.

#### Transform

A transformation ("transform") operates on a type of pane to add, remove, or change elements. Transformations are used to interact
with panes.

## Usage

Gradle instructions coming soon. For now, clone the repo, build, and publish using the `publishMavenPublicationToMavenLocal` 
task. The dependency information can be found in `build.gradle.kts`.

## Examples

<details open>
<summary>Creating an interface</summary>

This code creates a chest interface with one row, a background fill, an ItemStackElement containing information about the 
player, and utilizes an argument provider. This element also tracks how many times it has been clicked. 

```java
ChestInterface infoInterface = ChestInterface.builder()
    // This interface will have one row.
    .rows(1)
    // This interface will update every five ticks.
    .updates(true, 5)
    // Cancel all inventory click events
    .topClickHandler(ClickHandler.cancel())
    // Fill the background with black stained glass panes
    .addTransform(PaperTransform.chestFill(
        ItemStackElement.of(new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
    ))
    // Add some information to the pane
    .addTransform((pane, view) -> {
        // Get the view arguments
        // (Keep in mind - these arguments may be coming from a Supplier, so their values can change!)
        final @NonNull String time = view.argument().get("time");
        final @NonNull Player player = view.argument().get("player");

        // Return a pane with 
        return pane.element(ItemStackElement.of(PaperItemBuilder.paper(Material.PAPER)
            // Add icon name
            .name(Component.text()
                .append(player.displayName())
                .append(Component.text("'s info"))
                .decoration(TextDecoration.ITALIC, false)
                .asComponent())
            // Add icon lore
            .loreComponents(
                Component.text()
                    .append(Component.text("Current time: "))
                    .append(Component.text(time))
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false)
                    .asComponent(),
                Component.text()
                    .append(Component.text("Health: "))
                    .append(Component.text(Double.toString(player.getHealth())))
                    .color(NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false)
                    .asComponent())
                    .build(),
                // Handle click
                (clickEvent, clickView) -> {
                    final @NonNull InterfaceArgument argument = clickView.argument();
                    argument.set("clicks", ((Integer) argument.get("clicks")) + 1);
                    clickView.parent().open(clickView.viewer(), argument);
                }
            ), 4, 0);
        })
        // Set the title
        .title(Component.text("interfaces demo"))
        // Build the interface
        .build();
```
</details>

<details open>
<summary>Showing the interface to a player</summary>

This code shows the interface created in the previous example being shown to a viewer.
As the interface uses arguments, we can pass in supplier functions to the interface argument.

```java
// Open the interface to the player.
infoInterface.open(PlayerViewer.of(player),
    // Create a HashMapInterfaceArgument with a time argument set to a
    // supplier that returns the current time printed all nice and pretty.
    HashMapInterfaceArgument.with("time", () -> {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    })
        .with("clicks", 0)
        .build()
);
```
</details>

_Note: these examples may not reflect the latest version of the `interfaces` API._

## Credits

Thanks to [kyori](https://github.com/kyoripowered) and their [`adventure` text library](https://github.com/kyoripowered/adventure).

Thanks to [broccolai](https://github.com/broccolai) for letting me steal his entire Gradle project setup.
