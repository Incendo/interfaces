# `interfaces`

_Building interfaces since 2021._

`interfaces` is a builder-style user interface library designed to make creation of flexible user interfaces as easy as possible.

> This library is currently in a state of constant breaking changes. The API has yet to be finalized, so please keep this in mind when considering using `interfaces`.

## Packages

[`interfaces-core`](https://github.com/Incendo/interfaces/tree/master/core) provides the core API classes.

[`interfaces-paper`](https://github.com/Incendo/interfaces/tree/master/paper) implements `interfaces` using the [Paper Minecraft server API](https://papermc.io). This package 
provides the
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

Snapshots of this repository are hosted at https://repo.incendo.org/content/repositories/snapshots/.

Gradle example:

```kotlin
repositories() {
    // ...
    maven("https://repo.incendo.org/content/repositories/snapshots/")
    // ...
}

dependencies() {
    implementation("org.incendo.interfaces:interfaces-{package}:1.0.0-SNAPSHOT")
}
```

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
    .clickHandler(ClickHandler.cancel())
    // Fill the background with black stained glass panes
    .addTransform(PaperTransform.chestFill(
    ItemStackElement.of(new ItemStack(Material.BLACK_STAINED_GLASS_PANE))
    ))
    // Add some information to the pane
    .addTransform((pane, view) -> {
        // Get the view arguments
        // (Keep in mind - these arguments may be coming from a Supplier, so their values can change!)
        final String time = view.arguments().get(ArgumentKey.of("time", String.class));
        final Player player = view.arguments().get(ArgumentKey.of("player", Player.class));
        final Integer clicks = view.arguments().get(ArgumentKey.of("clicks", Integer.class));

        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.displayName(Component.text("Item Name", NamedTextColor.GREEN));
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text("Line 1"));
        lore.add(Component.text("Clicks: " + clicks));
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);

        // Return a pane with
        return pane.element(ItemStackElement.of(itemStack,
        // Handle click
        (clickHandler) -> {
            final HashMapInterfaceArguments arguments = HashMapInterfaceArguments
                .with(ArgumentKey.of("clicks", Integer.class), (clickHandler.view().arguments().get(ArgumentKey.of("clicks", Integer.class))) + 1)
                .with(ArgumentKey.of("player", Player.class), clickHandler.view().arguments().get(ArgumentKey.of("player", Player.class))).build();

            clickHandler.view().backing().open(clickHandler.viewer(), arguments);
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
    HashMapInterfaceArguments
        .with(ArgumentKey.of("player", Player.class), player)
        .with(ArgumentKey.of("time", String.class), () -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            return dtf.format(now);
            })
        .with(ArgumentKey.of("clicks", Integer.class), 0).build()
);
```

[Video of the result of this code in Minecraft](https://imgur.com/a/JPdJPvX).
</details>

_Note: these examples may not reflect the latest version of the `interfaces` API._

Further examples can be found here: https://github.com/Incendo/interfaces/tree/master/examples

## Credits

Thanks to [kyori](https://github.com/kyoripowered) and their [`adventure` text library](https://github.com/kyoripowered/adventure).

Thanks to [broccolai](https://github.com/broccolai) for letting me steal his entire Gradle project setup.
