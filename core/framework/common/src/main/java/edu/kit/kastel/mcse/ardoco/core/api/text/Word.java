/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.text;

import java.io.Serializable;

import org.eclipse.collections.api.list.ImmutableList;

/**
 * Represents a word in a text.
 */
public interface Word extends Comparable<Word>, Serializable {

    /**
     * Returns the sentence number (starting at 0).
     *
     * @return the sentence number
     */
    int getSentenceNumber();

    /**
     * Returns the sentence containing this word.
     *
     * @return the sentence
     */
    Sentence getSentence();

    /**
     * Returns the text of the word.
     *
     * @return the text
     */
    String getText();

    /**
     * Returns the POS tag of the word.
     *
     * @return the POS tag
     */
    POSTag getPosTag();

    /**
     * Returns the previous word in the sentence.
     *
     * @return the previous word
     */
    Word getPreWord();

    /**
     * Returns the next word in the sentence.
     *
     * @return the next word
     */
    Word getNextWord();

    /**
     * Returns the position of the word in the entire text.
     *
     * @return the position
     */
    int getPosition();

    /**
     * Returns the lemmatized form of the word.
     *
     * @return the lemma
     */
    String getLemma();

    /**
     * Returns outgoing dependency words of this word for the given dependency tag.
     *
     * @param dependencyTag the dependency tag
     * @return outgoing dependency words
     */
    ImmutableList<Word> getOutgoingDependencyWordsWithType(DependencyTag dependencyTag);

    /**
     * Returns incoming dependency words of this word for the given dependency tag.
     *
     * @param dependencyTag the dependency tag
     * @return incoming dependency words
     */
    ImmutableList<Word> getIncomingDependencyWordsWithType(DependencyTag dependencyTag);

    /**
     * Returns the phrase containing this word.
     *
     * @return the phrase
     */
    Phrase getPhrase();

    @Override
    default int compareTo(Word o) {
        if (this.equals(o))
            return 0;

        int compareSentences = Integer.compare(this.getSentenceNumber(), o.getSentenceNumber());
        if (compareSentences != 0) {
            return compareSentences;
        }
        return Integer.compare(this.getPosition(), o.getPosition());
    }
}
