/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction;

import static edu.kit.kastel.mcse.ardoco.core.common.AggregationFunctions.AVERAGE;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.sorted.ImmutableSortedMap;
import org.eclipse.collections.api.set.sorted.ImmutableSortedSet;

import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.kit.kastel.mcse.ardoco.core.common.AggregationFunctions;
import edu.kit.kastel.mcse.ardoco.core.common.tuple.Pair;
import edu.kit.kastel.mcse.ardoco.core.data.Confidence;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Claimant;

/**
 * Interface for strategies for the text state. Responsible for creating {@link NounMapping NounMappings} from their constituent parts in a variety of
 * situations.
 */
public interface TextStateStrategy {
    /**
     * Aggregation function used to aggregate multiple confidences into a single value.
     */
    AggregationFunctions DEFAULT_AGGREGATOR = AVERAGE;

    /**
     * Adds a name mapping to the state.
     *
     * @param word        word of the mapping
     * @param kind        the kind of the mapping
     * @param claimant    the claimant of the mapping
     * @param probability probability to be a name mapping
     * @return the resulting noun mapping
     */
    default NounMapping addNounMapping(Word word, MappingKind kind, Claimant claimant, double probability) {
        return this.addNounMapping(word, kind, claimant, probability, Lists.immutable.with(word.getText()));
    }

    /**
     * Tries to add a mapping to the state using the specified parameters. If a matching mapping already exists, the mapping is extended instead.
     *
     * @param word         the word
     * @param kind         the kind
     * @param claimant     the claimant of the mapping
     * @param probability  the probability
     * @param surfaceForms the surface forms
     * @return the resulting noun mapping, either new or merged
     */
    NounMapping addNounMapping(Word word, MappingKind kind, Claimant claimant, double probability, ImmutableList<String> surfaceForms);

    /**
     * Adds a mapping to the state using the specified parameters. Does not consider whether a matching mapping already exists.
     *
     * @param words          words of the mapping
     * @param distribution   distribution of the mapping for the mapping kinds
     * @param referenceWords reference words of the mapping
     * @param surfaceForms   surface forms of the mapping
     * @param reference      a joined reference string
     * @return the new or merged mapping
     */
    NounMapping addNounMapping(ImmutableSortedSet<Word> words, ImmutableSortedMap<MappingKind, Confidence> distribution, ImmutableList<Word> referenceWords,
            ImmutableList<String> surfaceForms, String reference);

    /**
     * Adds a noun mapping of the specified kind to the state that contains the specified words, surface forms, etc.
     *
     * @param words          words of the mapping
     * @param kind           kind of the mapping
     * @param claimant       claimant of the mapping
     * @param probability    probability to be a noun mapping of this kind
     * @param referenceWords references of this noun mapping
     * @param surfaceForms   surface forms of this noun mapping
     * @param reference      a joined reference string
     * @return the new or merged mapping
     */
    NounMapping addNounMapping(ImmutableSortedSet<Word> words, MappingKind kind, Claimant claimant, double probability, ImmutableList<Word> referenceWords,
            ImmutableList<String> surfaceForms, String reference);

    /**
     * Merges two noun mappings into a new mapping of the given kind, probability and claimant without adding it to the state.
     *
     * @param firstNounMapping  the first mapping
     * @param secondNounMapping the second mapping
     * @param referenceWords    reference words to use, nullable
     * @param reference         reference to use, nullable
     * @param mappingKind       the mapping kind
     * @param claimant          the claimant
     * @param probability       the probability
     * @return the merged noun mapping
     */
    NounMapping mergeNounMappingsStateless(NounMapping firstNounMapping, NounMapping secondNounMapping, ImmutableList<Word> referenceWords, String reference,
            MappingKind mappingKind, Claimant claimant, double probability);

    /**
     * Merges two noun mappings into a new mapping of the given kind, probability and claimant and adds it to the state.
     *
     * @param firstNounMapping  the first mapping
     * @param secondNounMapping the second mapping
     * @param referenceWords    reference words to use, nullable
     * @param reference         reference to use, nullable
     * @param mappingKind       the mapping kind
     * @param claimant          the claimant
     * @param probability       the probability
     * @return the merged noun mapping
     */
    NounMapping mergeNounMappings(NounMapping firstNounMapping, NounMapping secondNounMapping, ImmutableList<Word> referenceWords, String reference,
            MappingKind mappingKind, Claimant claimant, double probability);

    /**
     * Calculates a joined reference for a set of reference words.
     *
     * @param referenceWords the reference words
     * @return a joined reference
     */
    default String calculateNounMappingReference(ImmutableList<Word> referenceWords) {
        StringBuilder refBuilder = new StringBuilder();
        referenceWords.toSortedListBy(Word::getPosition);
        referenceWords.toSortedListBy(Word::getSentenceNumber);

        for (int i = 0; i < referenceWords.size() - 1; i++) {
            refBuilder.append(referenceWords.get(i).getText()).append(" ");
        }
        refBuilder.append(referenceWords.get(referenceWords.size() - 1).getText());
        return refBuilder.toString();
    }

    /**
     * Merges phrase mappings and noun mappings into the state.
     *
     * @param phraseMapping        the phrase mapping
     * @param similarPhraseMapping the similar phrase mapping
     * @param similarNounMappings  the similar noun mappings
     * @param claimant             the claimant
     */
    void mergePhraseMappingsAndNounMappings(PhraseMapping phraseMapping, PhraseMapping similarPhraseMapping,
            MutableList<Pair<NounMapping, NounMapping>> similarNounMappings, Claimant claimant);

    /**
     * Merges two noun mappings into the state.
     *
     * @param nounMapping               the noun mapping
     * @param textuallyEqualNounMapping the textually equal noun mapping
     * @param claimant                  the claimant
     */
    void mergeNounMappings(NounMapping nounMapping, NounMapping textuallyEqualNounMapping, Claimant claimant);

}
