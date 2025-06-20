/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction;

import java.io.Serializable;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.sorted.ImmutableSortedMap;
import org.eclipse.collections.api.set.sorted.ImmutableSortedSet;

import edu.kit.kastel.mcse.ardoco.core.api.text.Phrase;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.kit.kastel.mcse.ardoco.core.data.Confidence;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Claimant;

/**
 * Represents a mapping of noun phrases or words to a concept (name, type, or both).
 */
public interface NounMapping extends Serializable {
    /**
     * Returns the surface forms (occurrences) of this mapping.
     *
     * @return all appearances of the mapping
     */
    ImmutableList<String> getSurfaceForms();

    /**
     * Returns all words that are contained by the mapping.
     *
     * @return all words referenced by this mapping
     */
    ImmutableSortedSet<Word> getWords();

    /**
     * Returns the probability of being a mapping of its kind.
     *
     * @return probability of being a mapping of its kind
     */
    double getProbability();

    /**
     * Returns the kind: name, type, or name_or_type.
     *
     * @return the kind
     */
    MappingKind getKind();

    /**
     * Returns the reference (comparable and naming attribute) of this mapping.
     *
     * @return the reference
     */
    String getReference();

    /**
     * Returns the reference words.
     *
     * @return the reference words
     */
    ImmutableList<Word> getReferenceWords();

    /**
     * Returns the sentence numbers of occurrences, sorted.
     *
     * @return sentence numbers of the occurrences
     */
    ImmutableList<Integer> getMappingSentenceNo();

    /**
     * Returns the phrases associated with this mapping.
     *
     * @return the phrases
     */
    ImmutableSortedSet<Phrase> getPhrases();

    /**
     * Gets the probability for a specific mapping kind.
     *
     * @param mappingKind the kind of mapping
     * @return the probability for the kind
     */
    double getProbabilityForKind(MappingKind mappingKind);

    /**
     * Gets the distribution of all mapping kinds.
     *
     * @return the distribution
     */
    ImmutableSortedMap<MappingKind, Confidence> getDistribution();

    /**
     * Returns the claimants for this mapping.
     *
     * @return the claimants
     */
    ImmutableList<Claimant> getClaimants();

    /**
     * Adds the kind with probability.
     *
     * @param kind        the kind
     * @param claimant    the agent that claims the kind
     * @param probability the probability
     */
    void addKindWithProbability(MappingKind kind, Claimant claimant, double probability);

    /**
     * Checks if this mapping is a compound.
     *
     * @return true if compound, false otherwise
     */
    boolean isCompound();
}
