package org.incendo.interfaces.grid;

import java.util.List;

@FunctionalInterface
public interface GridPositionGenerator {
    List<GridPoint> generate();
}
