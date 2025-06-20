/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency;

import java.io.Serializable;

import org.eclipse.collections.api.collection.ImmutableCollection;

/**
 * Represents an identified inconsistency of a certain type with a certain reason.
 */
public interface Inconsistency extends Serializable {
    /**
     * Returns the reason why there is an inconsistency.
     *
     * @return the reason of inconsistency
     */
    String getReason();

    /**
     * Returns the name of the type of inconsistency.
     *
     * @return the name of the type of inconsistency
     */
    String getType();

    /**
     * Return a list with String arrays as entries. The entries should have the format to first state the type of inconsistency, then the sentence number and
     * third the id of the model element or the name of the text element (or both). Fourth entry can be an optional confidence value.
     *
     * @return list with String arrays as entry with the format {SentenceNumber, ModelElementId/TextElement, (optional) confidence}.
     */
    ImmutableCollection<String[]> toFileOutput();
}
