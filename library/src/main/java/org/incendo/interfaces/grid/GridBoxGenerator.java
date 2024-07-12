package org.incendo.interfaces.grid;

import java.util.ArrayList;
import java.util.List;

/**
 * A grid position generator that generates every point inside a box with corners of
 * [min] and [max] (inclusive).
 */
public final class GridBoxGenerator implements GridPositionGenerator {
    private final GridPoint min;
    private final GridPoint max;

    public GridBoxGenerator(final GridPoint min, final GridPoint max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public List<GridPoint> generate() {
        List<GridPoint> result = new ArrayList<>();

        for (int x = this.min.x(); x <= this.max.x(); x++) {
            for (int y = this.min.y(); y <= this.max.y(); y++) {
                result.add(GridPoint.at(x, y));
            }
        }

        return result;
    }
}
