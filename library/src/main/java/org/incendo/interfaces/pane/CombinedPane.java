package org.incendo.interfaces.pane;

import java.util.ArrayList;
import java.util.List;

public class CombinedPane extends OrderedPane {

    private static List<Integer> createMappings(final int rows) {
        List<Integer> mappings = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            mappings.add(i);
        }

        // the players hotbar is row 0 in the players inventory,
        // for combined interfaces it makes more sense for hotbar
        // to be the last row, so reshuffle here.
        mappings.add(rows + 1);
        mappings.add(rows + 2);
        mappings.add(rows + 3);
        mappings.add(rows);

        return mappings;
    }

    public CombinedPane(final int chestRows) {
        super(createMappings(chestRows));
    }
}


