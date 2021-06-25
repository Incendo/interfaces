package dev.kscott.interfaces.paper;

import dev.kscott.interfaces.paper.type.BookInterface;
import dev.kscott.interfaces.paper.type.ChestInterface;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Utility methods for Paper interfaces.
 */
public interface PaperInterfaces {

    /**
     * Creates a new {@link ChestInterface}.
     *
     * @param rows amount of rows
     * @return the interface
     */
    static @NonNull ChestInterface chest(final int rows) {
        return new ChestInterface(rows);
    }

    /**
     * Creates a new {@link BookInterface}.
     *
     * @return the interface
     */
    static @NonNull BookInterface book() {
        return new BookInterface();
    }

}
