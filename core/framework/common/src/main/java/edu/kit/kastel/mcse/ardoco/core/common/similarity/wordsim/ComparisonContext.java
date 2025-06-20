/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim;

import java.util.Objects;

import edu.kit.kastel.mcse.ardoco.core.api.text.Word;

/**
 * Contains all information for comparing similarity between objects in ArDoCo.
 * The fields {@code firstString} and {@code secondString} are always non-null.
 * The field {@code lemmatize} decides whether the lemmatized version of both words should be used for comparison.
 */
public record ComparisonContext(String firstString, String secondString, Word firstWord, Word secondWord, boolean lemmatize) {

    /**
     * Constructs a string-based context with the default match function and no lemmatization.
     *
     * @param firstString  the first string
     * @param secondString the second string
     */
    public ComparisonContext(String firstString, String secondString) {
        this(firstString, secondString, null, null, false);
    }

    /**
     * Constructs a string-based context with the default match function.
     *
     * @param firstString  the first string
     * @param secondString the second string
     * @param lemmatize    whether the string should be lemmatized
     */
    public ComparisonContext(String firstString, String secondString, boolean lemmatize) {
        this(firstString, secondString, null, null, lemmatize);
    }

    /**
     * Constructs a word-based context with the default match function.
     *
     * @param firstWord  the first word
     * @param secondWord the second word
     * @param lemmatize  whether the words should be lemmatized
     */
    public ComparisonContext(Word firstWord, Word secondWord, boolean lemmatize) {
        this(firstWord.getText(), secondWord.getText(), firstWord, secondWord, lemmatize);
    }

    /**
     * Finds the most appropriate string representation for the first object in this comparison.
     *
     * @return the most appropriate string representation of the first object
     */
    public String firstTerm() {
        return this.findAppropriateTerm(this.firstString, this.firstWord);
    }

    /**
     * Finds the most appropriate string representation for the second object in this comparison.
     *
     * @return the most appropriate string representation of the second object
     */
    public String secondTerm() {
        return this.findAppropriateTerm(this.secondString, this.secondWord);
    }

    private String findAppropriateTerm(String string, Word word) {
        Objects.requireNonNull(string);

        if (word != null) {
            return this.lemmatize ? word.getLemma() : word.getText();
        }
        return string;
    }

}
