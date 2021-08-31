package org.incendo.interfaces.core.transform;

/**
 * Exception that can be thrown if a {@link Transform} should cancel the updating of
 * an interface.
 */
public class InterruptUpdateException extends RuntimeException {

    /**
     * Constructs a new exception instance.
     *
     * @param reason the reason for interrupting the update
     */
    public InterruptUpdateException(
            final String reason
    ) {
        super(reason);
    }

    @Override
    public final synchronized Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public final synchronized Throwable initCause(final Throwable cause) {
        return this;
    }

}
