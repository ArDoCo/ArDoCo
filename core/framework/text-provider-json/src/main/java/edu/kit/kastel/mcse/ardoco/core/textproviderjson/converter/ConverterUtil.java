/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.textproviderjson.converter;

import java.util.List;

import edu.kit.kastel.mcse.ardoco.core.api.text.Phrase;
import edu.kit.kastel.mcse.ardoco.core.api.text.Sentence;

/**
 * This utility class provides methods for the converter
 */
public final class ConverterUtil {

    private ConverterUtil() {
    }

    /**
     * gets the direct children of the given phrase
     * 
     * @param parentPhrase the phrase
     * @return the direct children of this phrase
     */
    public static List<Phrase> getChildPhrases(Phrase parentPhrase) {
        List<Phrase> subphrases = parentPhrase.getSubphrases().toList();
        return subphrases.stream().filter(x -> isPhraseOnHighestLevel(subphrases, x)).toList();
    }

    /**
     * gets the phrases on the highest level of the given sentence
     * 
     * @param sentence the sentence
     * @return the child phrases of the sentence
     */
    public static List<Phrase> getChildPhrases(Sentence sentence) {
        List<Phrase> phrases = sentence.getPhrases().toList();
        return phrases.stream().filter(x -> isPhraseOnHighestLevel(phrases, x)).toList();
    }

    private static boolean isPhraseOnHighestLevel(List<Phrase> subphrases, Phrase childPhrase) {
        for (Phrase subphrase : subphrases) {
            if (childPhrase.isSubphraseOf(subphrase)) {
                return false;
            }
        }
        return true;
    }
}
