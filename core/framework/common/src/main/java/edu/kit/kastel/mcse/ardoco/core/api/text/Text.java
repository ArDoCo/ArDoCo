/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.text;

import java.io.Serializable;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;

/**
 * Represents a text document.
 */
public interface Text extends Serializable {

    /**
     * Returns the length of the text (number of words).
     *
     * @return the length
     */
    default int getNumberOfWords() {
        return words().size();
    }

    /**
     * Returns all words in the text (ordered).
     *
     * @return the words
     */
    ImmutableList<Word> words();

    /**
     * Returns all phrases in the text (ordered).
     *
     * @return the phrases
     */
    default ImmutableList<Phrase> phrases() {
        return Lists.immutable.fromStream(getSentences().stream().flatMap(s -> s.getPhrases().stream()));
    }

    /**
     * Returns the word at the given index.
     *
     * @param index the index
     * @return the word at the given index
     */
    Word getWord(int index);

    /**
     * Returns the sentences of the text, ordered by appearance.
     *
     * @return the sentences
     */
    ImmutableList<Sentence> getSentences();
}
