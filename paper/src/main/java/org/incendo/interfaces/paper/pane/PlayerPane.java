package org.incendo.interfaces.paper.pane;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.IntRange;
import org.incendo.interfaces.core.element.Element;
import org.incendo.interfaces.core.pane.GridPane;
import org.incendo.interfaces.core.util.Vector2;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.utils.PaperUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A pane based off of a player inventory.
 */
public final class PlayerPane implements GridPane<PlayerPane, ItemStackElement<PlayerPane>> {

    public static final int HOTBAR_MIN = 0;
    public static final int HOTBAR_MAX = 8;
    public static final int MAIN_MIN = 9;
    public static final int MAIN_MAX = 35;
    public static final int ARMOR_MIN = 36;
    public static final int ARMOR_MAX = 39;
    public static final int OFF_HAND = 40;

    private final ItemStackElement[] elements;

    /**
     * Constructs {@code PlayerPane}.
     */
    public PlayerPane() {
        this.elements = new ItemStackElement[41];
        for (int i = 0; i < this.elements.length; i++) {
            this.elements[i] = ItemStackElement.empty();
        }
    }

    private PlayerPane(
            final @NonNull ItemStackElement @NonNull[] elements
    ) {
        this.elements = elements;
    }

    @Override
    public @NonNull Collection<Element> elements() {
        final List<Element> elements = new ArrayList<>(this.elements.length);
        elements.addAll(Arrays.asList(this.elements));
        return elements;
    }

    /**
     * Returns a list containing all elements in this interface
     *
     * @return the elements
     */
    public @NonNull List<@NonNull ItemStackElement<PlayerPane>> inventoryElements() {
        final List<ItemStackElement<PlayerPane>> elements = new ArrayList<>(this.elements.length);
        for (var element : this.elements) {
            elements.add((ItemStackElement<PlayerPane>) element);
        }
        return elements;
    }

    /**
     * Sets an element at the given position.
     * <p>
     * This methods returns an updated instance of PlayerPane with the new element.
     *
     * @param index   the index
     * @param element the element
     * @return a new {@code PlayerPane}
     */
    public @NonNull PlayerPane element(
            final @IntRange(from = 0, to = 40) int index,
            final @NonNull ItemStackElement<PlayerPane> element
    ) {
        final ItemStackElement[] elements = new ItemStackElement[this.elements.length];
        System.arraycopy(this.elements, 0, elements, 0, this.elements.length);
        elements[index] = element;
        return new PlayerPane(elements);
    }

    /**
     * Returns the element at the given index.
     *
     * @param index the index
     * @return the element
     */
    public @NonNull ItemStackElement<PlayerPane> element(final @IntRange(from = 0, to = 40) int index) {
        return this.elements[index];
    }

    /**
     * Sets an element at the given position.
     * <p>
     * This methods returns an updated instance of PlayerPane with the new element.
     *
     * @param index   the index
     * @param element the element
     * @return a new {@code PlayerPane}
     */
    public @NonNull PlayerPane armor(
            final @IntRange(from = 0, to = 3) int index,
            final @NonNull ItemStackElement<PlayerPane> element
    ) {
        return this.setAdjusted(index, SlotType.ARMOR, element);
    }

    /**
     * Returns the element at the given index.
     *
     * @param index the index
     * @return the element
     */
    public @NonNull ItemStackElement<PlayerPane> armor(final @IntRange(from = 0, to = 3) int index) {
        return this.getAdjusted(index, SlotType.ARMOR);
    }

    /**
     * Sets an element at the given position.
     * <p>
     * This methods returns an updated instance of PlayerPane with the new element.
     *
     * @param index   the index
     * @param element the element
     * @return a new {@code PlayerPane}
     */
    public @NonNull PlayerPane hotbar(
            final @IntRange(from = 0, to = 8) int index,
            final @NonNull ItemStackElement<PlayerPane> element
    ) {
        return this.setAdjusted(index, SlotType.HOTBAR, element);
    }

    /**
     * Returns the element at the given index.
     *
     * @param index the index
     * @return the element
     */
    public @NonNull ItemStackElement<PlayerPane> hotbar(final @IntRange(from = 0, to = 8) int index) {
        return this.getAdjusted(index, SlotType.HOTBAR);
    }

    /**
     * Sets an element at the given position.
     * <p>
     * This methods returns an updated instance of PlayerPane with the new element.
     *
     * @param index   the index
     * @param element the element
     * @return a new {@code PlayerPane}
     */
    public @NonNull PlayerPane main(
            final @IntRange(from = 0, to = 26) int index,
            final @NonNull ItemStackElement<PlayerPane> element
    ) {
        return this.setAdjusted(index, SlotType.MAIN, element);
    }

    /**
     * Returns the element at the given index.
     *
     * @param index the index
     * @return the element
     */
    public @NonNull ItemStackElement<PlayerPane> main(final @IntRange(from = 0, to = 26) int index) {
        return this.getAdjusted(index, SlotType.MAIN);
    }

    /**
     * Sets an element at the given position.
     * <p>
     * This methods returns an updated instance of PlayerPane with the new element.
     *
     * @param x       the x coordinate
     * @param y       the y coordinate
     * @param element the element
     * @return a new {@code PlayerPane}
     */
    public @NonNull PlayerPane main(
            final @IntRange(from = 0, to = 8) int x,
            final @IntRange(from = 0, to = 2) int y,
            final @NonNull ItemStackElement<PlayerPane> element
    ) {
        return this.setAdjusted(
                PaperUtils.gridToSlot(Vector2.at(x, y)),
                SlotType.MAIN,
                element
        );
    }

    /**
     * Sets the off hand element.
     * <p>
     * This methods returns an updated instance of PlayerPane with the new element.
     *
     * @param element the element
     * @return a new {@code PlayerPane}
     */
    public @NonNull PlayerPane offHand(
            final @NonNull ItemStackElement<PlayerPane> element
    ) {
        return this.setAdjusted(
                0,
                SlotType.OFF_HAND,
                element
        );
    }

    /**
     * Returns the off hand element.
     *
     * @return the element
     */
    public @NonNull ItemStackElement<PlayerPane> offHand() {
        return this.getAdjusted(
                0,
                SlotType.OFF_HAND
        );
    }

    /**
     * Sets the element in the given [index], where the index is
     * relative to the [slotType].
     *
     * @param index    the index of the item
     * @param slotType the slot type
     * @param element  the element
     * @return the updated pane
     */
    public @NonNull PlayerPane setAdjusted(
            final int index,
            final @NonNull SlotType slotType,
            final @NonNull ItemStackElement<PlayerPane> element
    ) {
        final int adjusted = index + slotType.minIndex();
        if (adjusted > slotType.maxIndex()) {
            throw new ArrayIndexOutOfBoundsException(
                    String.format(
                            "%d is not in the range [0, %d]",
                            slotType.maxIndex() - slotType.minIndex(),
                            index
                    )
            );
        }
        return this.element(adjusted, element);
    }

    /**
     * Returns the element in the given [index], where the index is
     * relative to the [slotType].
     *
     * @param index    the index of the item
     * @param slotType the slot type
     * @return the element
     */
    public @NonNull ItemStackElement<PlayerPane> getAdjusted(
            final int index,
            final @NonNull SlotType slotType
    ) {
        final int adjusted = index + slotType.minIndex();
        if (adjusted > slotType.maxIndex()) {
            throw new ArrayIndexOutOfBoundsException(
                    String.format(
                            "%d is not in the range [0, %d]",
                            slotType.maxIndex() - slotType.minIndex(),
                            index
                    )
            );
        }
        return this.element(adjusted);
    }

    @Override
    public @NonNull PlayerPane element(
            @NonNull final ItemStackElement<PlayerPane> element,
            final int x,
            final int y
    ) {
        return this.main(PaperUtils.gridToSlot(Vector2.at(x, y)), element);
    }

    @Override
    public @NonNull ItemStackElement<PlayerPane> element(final int x, final int y) {
        return this.main(PaperUtils.gridToSlot(Vector2.at(x, y)));
    }

    public enum SlotType {
        /** The player hotbar. */
        HOTBAR(HOTBAR_MIN, HOTBAR_MAX),
        /** The main player inventory. */
        MAIN(MAIN_MIN,  MAIN_MAX),
        /** The armor slots. */
        ARMOR(ARMOR_MIN, ARMOR_MAX),
        /** The off hand slot. */
        OFF_HAND(PlayerPane.OFF_HAND,  PlayerPane.OFF_HAND);

        private final int minIndex;
        private final int maxIndex;

        SlotType(
                final @IntRange(from = 0, to = 40) int minIndex,
                final @IntRange(from = 0, to = 40) int maxIndex
        ) {
            this.minIndex = minIndex;
            this.maxIndex = maxIndex;
        }

        /**
         * Returns the minimum index for this slot type
         *
         * @return the minimum index
         */
        public @IntRange(from = 0, to = 40) int minIndex() {
            return this.minIndex;
        }

        /**
         * Returns the maximum index for this slot type
         *
         * @return the maximum index
         */
        public @IntRange(from = 0, to = 40) int maxIndex() {
            return this.maxIndex;
        }
    }

}
