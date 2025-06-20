/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.text;

import java.io.Serializable;

import org.eclipse.collections.api.list.ImmutableList;

/**
 * Represents a sentence in the document.
 */
public interface Sentence extends Serializable {

    /**
     * Returns the sentence number (starting at 0).
     *
     * @return the sentence number
     */
    int getSentenceNumber();

    /**
     * Returns the sentence number for output (starting at 1).
     *
     * @return the sentence number for output
     */
    default int getSentenceNumberForOutput() {
        return getSentenceNumber() + 1;
    }

    /**
     * Returns the words contained in this sentence.
     *
     * @return the words
     */
    ImmutableList<Word> getWords();

    /**
     * Returns the text of this sentence.
     *
     * @return the text
     */
    String getText();

    /**
     * Checks if this sentence is equal to another sentence.
     *
     * @param other the other sentence
     * @return true if equal
     */
    default boolean isEqualTo(Sentence other) {
        return other != null && this.getSentenceNumber() == other.getSentenceNumber() && other.getText().equals(this.getText());
    }

    /**
     * Returns the phrases in this sentence.
     *
     * @return the phrases
     */
    ImmutableList<Phrase> getPhrases();

    /**
     * Adds a new phrase to the sentence.
     *
     * @param phrase the phrase
     */
    void addPhrase(Phrase phrase);
}
