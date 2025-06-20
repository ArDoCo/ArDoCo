/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction;

import java.io.Serializable;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.sorted.ImmutableSortedMap;
import org.eclipse.collections.api.set.sorted.ImmutableSortedSet;

import edu.kit.kastel.mcse.ardoco.core.api.text.Phrase;
import edu.kit.kastel.mcse.ardoco.core.api.text.PhraseType;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;

/**
 * Represents a mapping of phrases to noun mappings and phrase vectors.
 */
public interface PhraseMapping extends Serializable {
    /**
     * Returns the noun mappings associated with this phrase mapping.
     *
     * @param textState the text state
     * @return the noun mappings
     */
    ImmutableList<NounMapping> getNounMappings(TextState textState);

    /**
     * Returns the phrases in this mapping.
     *
     * @return the phrases
     */
    ImmutableSortedSet<Phrase> getPhrases();

    /**
     * Returns the type of the phrase.
     *
     * @return the phrase type
     */
    PhraseType getPhraseType();

    /**
     * Returns the phrase vector.
     *
     * @return the phrase vector
     */
    ImmutableSortedMap<Word, Integer> getPhraseVector();

    /**
     * Removes a phrase from this mapping.
     *
     * @param phrase the phrase to remove
     */
    void removePhrase(Phrase phrase);
}
