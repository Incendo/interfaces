package org.incendo.interfaces.pane;

import org.incendo.interfaces.element.Element;
import org.incendo.interfaces.grid.GridPoint;

import java.util.List;
import java.util.Objects;

public final class PlayerPane extends OrderedPane {
    public static final List<Integer> PANE_ORDERING = List.of(1, 2, 3, 0, 4);
    private static final GridPoint OFF_HAND_SLOT = GridPoint.at(4, 4);

    private final Armor armor = new Armor();

    public PlayerPane() {
        super(PANE_ORDERING);
    }

    public Element offHand() {
        return Objects.requireNonNullElse(
            get(OFF_HAND_SLOT),
            Element.EMPTY
        );
    }

    public void offHand(final Element value) {
        put(OFF_HAND_SLOT, value);
    }

    public void hotbar(final int slot, final Element value) {
        put(3, slot, value);
    }

    public Armor armor() {
        return this.armor;
    }

    // todo(josh): introduce an actual concept of subpanes?
    public final class Armor {
        public Element helmet() {
            return Objects.requireNonNullElse(
                get(4, 3),
                Element.EMPTY
            );
        }

        public void helmet(final Element helmet) {
            put(4, 3, helmet);
        }

        public Element chest() {
            return Objects.requireNonNullElse(
                get(4, 2),
                Element.EMPTY
            );
        }

        public void chest(final Element chest) {
            put(4, 2, chest);
        }

        public Element leggings() {
            return Objects.requireNonNullElse(
                get(4, 1),
                Element.EMPTY
            );
        }

        public void leggings(final Element leggings) {
            put(4, 1, leggings);
        }

        public Element boots() {
            return Objects.requireNonNullElse(
                get(4, 0),
                Element.EMPTY
            );
        }

        public void boots(final Element boots) {
            put(4, 0, boots);
        }
    }
}
