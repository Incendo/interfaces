package dev.kscott.interfaces.paper;

import dev.kscott.interfaces.paper.type.ChestInterface;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Utility methods for Paper interfaces.
 */
public interface PaperInterface {

    /**
     * Creates a new {@code ChestInterface}.
     *
     * @param rows amount of rows
     * @return the interface
     */
    static @NonNull ChestInterface chest(final int rows) {
        return new ChestInterface(rows);
    }

}
