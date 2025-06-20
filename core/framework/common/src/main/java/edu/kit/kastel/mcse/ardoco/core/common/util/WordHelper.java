/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.util;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;

import edu.kit.kastel.mcse.ardoco.core.api.text.DependencyTag;
import edu.kit.kastel.mcse.ardoco.core.api.text.POSTag;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;

/**
 * Helper class with static methods for working with words and their linguistic properties.
 */
public final class WordHelper {

    private WordHelper() {
        throw new IllegalAccessError();
    }

    /**
     * Checks for determiner as previous word.
     *
     * @param word the word
     * @return true, if found
     */
    public static boolean hasDeterminerAsPreWord(Word word) {

        Word preWord = word.getPreWord();
        if (preWord == null) {
            return false;
        }

        var prePosTag = preWord.getPosTag();
        return POSTag.DETERMINER == prePosTag;

    }

    /**
     * Checks for indirect determiner as previous word.
     *
     * @param word the word
     * @return true, if found
     */
    public static boolean hasIndirectDeterminerAsPreWord(Word word) {
        return hasDeterminerAsPreWord(word) && ("a".equalsIgnoreCase(word.getText()) || "an".equalsIgnoreCase(word.getText()));
    }

    /**
     * Gets the incoming dependency tags.
     *
     * @param word the word
     * @return the incoming dependency tags
     */
    public static ImmutableList<DependencyTag> getIncomingDependencyTags(Word word) {
        return Lists.immutable.with(DependencyTag.values()).select(d -> !word.getIncomingDependencyWordsWithType(d).isEmpty());
    }

    /**
     * Gets the outgoing dependency tags.
     *
     * @param word the word
     * @return the outgoing dependency tags
     */
    public static ImmutableList<DependencyTag> getOutgoingDependencyTags(Word word) {
        return Lists.immutable.with(DependencyTag.values()).select(d -> !word.getOutgoingDependencyWordsWithType(d).isEmpty());
    }

    /**
     * Checks if the word is a verb based on its POS tag.
     *
     * @param word the word
     * @return true if the word is a verb, false otherwise
     */
    public static boolean isVerb(Word word) {
        String tag = word.getPosTag().getTag();
        return tag.startsWith("VB") || tag.startsWith("MD");
    }

    /**
     * Checks if the word is an adjective based on its POS tag.
     *
     * @param word the word
     * @return true if the word is an adjective, false otherwise
     */
    public static boolean isAdjective(Word word) {
        String tag = word.getPosTag().getTag();
        return tag.startsWith("JJ");
    }

    /**
     * Checks if the word is an adverb based on its POS tag.
     *
     * @param word the word
     * @return true if the word is an adverb, false otherwise
     */
    public static boolean isAdverb(Word word) {
        String tag = word.getPosTag().getTag();
        return tag.startsWith("RB");
    }

    /**
     * Checks if the word is a noun based on its POS tag.
     *
     * @param word the word
     * @return true if the word is a noun, false otherwise
     */
    public static boolean isNoun(Word word) {
        String tag = word.getPosTag().getTag();
        return tag.startsWith("NN");
    }

    /**
     * Checks if the word is a pronoun based on its POS tag.
     *
     * @param word the word
     * @return true if the word is a pronoun, false otherwise
     */
    public static boolean isPronoun(Word word) {
        String tag = word.getPosTag().getTag();
        return tag.startsWith("PR") || tag.startsWith("WP");
    }
}
