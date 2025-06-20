/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.common;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class for generating and resetting unique identifiers used throughout the codebase.
 */
public class IdentifierProvider {
    private static final int INITIAL_VALUE = 1337;
    private static final AtomicInteger COUNTER = new AtomicInteger(INITIAL_VALUE);

    private IdentifierProvider() {
        throw new IllegalAccessError("IdentifierProvider is a utility class and cannot be instantiated.");
    }

    /**
     * Creates a new unique identifier string.
     *
     * @return a unique identifier string
     */
    public static String createId() {
        int currentCounter = COUNTER.getAndIncrement();
        return "acm%09djsd".formatted(currentCounter);
    }

    /**
     * Resets the identifier counter to its initial value.
     */
    public static void reset() {
        COUNTER.set(INITIAL_VALUE);
    }
}
