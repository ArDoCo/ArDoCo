/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency;

/**
 * Extends {@link Inconsistency} for inconsistencies stemming from a concrete textual component (e.g., a sentence).
 * Provides information on the text-side for more details.
 */
public interface TextInconsistency extends Inconsistency {
    /**
     * Returns the sentence number associated with this inconsistency.
     *
     * @return the sentence number
     */
    int getSentenceNumber();
}
