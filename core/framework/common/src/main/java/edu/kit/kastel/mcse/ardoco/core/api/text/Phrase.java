/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.text;

import java.io.Serializable;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.sorted.ImmutableSortedMap;

/**
 * Represents a phrase in a text.
 */
public interface Phrase extends Serializable, Comparable<Phrase> {
    /**
     * Returns the sentence number (starting at 0).
     *
     * @return the sentence number
     */
    int getSentenceNumber();

    /**
     * Returns the text of the phrase.
     *
     * @return the text
     */
    String getText();

    /**
     * Returns the type of the phrase.
     *
     * @return the phrase type
     */
    PhraseType getPhraseType();

    /**
     * Returns the words contained in this phrase.
     *
     * @return the contained words
     */
    ImmutableList<Word> getContainedWords();

    /**
     * Returns the subphrases of this phrase.
     *
     * @return the subphrases
     */
    ImmutableList<Phrase> getSubphrases();

    /**
     * Checks if this phrase is a superphrase of the given phrase.
     *
     * @param other the other phrase
     * @return true if this is a superphrase of other
     */
    boolean isSuperphraseOf(Phrase other);

    /**
     * Checks if this phrase is a subphrase of the given phrase.
     *
     * @param other the other phrase
     * @return true if this is a subphrase of other
     */
    boolean isSubphraseOf(Phrase other);

    /**
     * Returns the phrase vector.
     *
     * @return the phrase vector
     */
    ImmutableSortedMap<Word, Integer> getPhraseVector();
}
