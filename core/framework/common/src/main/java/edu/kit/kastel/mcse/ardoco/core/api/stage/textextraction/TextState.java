/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction;

import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.list.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.kit.kastel.mcse.ardoco.core.configuration.IConfigurable;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.data.PipelineStepData;

/**
 * Represents the state of text extraction, including noun and phrase mappings.
 */
public interface TextState extends IConfigurable, PipelineStepData {
    Logger logger = LoggerFactory.getLogger(TextState.class);

    String ID = "TextState";

    /**
     * Adds a noun mapping to the state.
     *
     * @param nounMapping the noun mapping to add
     */
    void addNounMapping(NounMapping nounMapping);

    /**
     * Removes a noun mapping from the state, optionally replacing it and cascading the removal.
     *
     * @param dataRepository the data repository
     * @param nounMapping    the noun mapping to remove
     * @param replacement    the replacement noun mapping (optional)
     * @param cascade        whether to cascade the removal
     */
    void removeNounMapping(DataRepository dataRepository, NounMapping nounMapping, NounMapping replacement, boolean cascade);

    /**
     * Adds a phrase mapping to the state.
     *
     * @param phraseMapping the phrase mapping to add
     */
    void addPhraseMapping(PhraseMapping phraseMapping);

    /**
     * Removes the specified phrase mapping from the state and replaces it with an (optional) replacement.
     *
     * @param phraseMapping the phrase mapping to remove
     * @param replacement   the replacement phrase mapping (optional)
     */
    void removePhraseMapping(PhraseMapping phraseMapping, PhraseMapping replacement);

    /**
     * Retrieves the phrase mapping associated with the given noun mapping.
     *
     * @param nounMapping the noun mapping
     * @return the associated phrase mapping
     */
    PhraseMapping getPhraseMappingByNounMapping(NounMapping nounMapping);

    /**
     * Retrieves the noun mappings associated with the given phrase mapping.
     *
     * @param phraseMapping the phrase mapping
     * @return the associated noun mappings
     */
    ImmutableList<NounMapping> getNounMappingsByPhraseMapping(PhraseMapping phraseMapping);

    /**
     * Retrieves all noun mappings in the state.
     *
     * @return the noun mappings
     */
    ImmutableList<NounMapping> getNounMappings();

    /**
     * Retrieves all phrase mappings in the state.
     *
     * @return the phrase mappings
     */
    ImmutableList<PhraseMapping> getPhraseMappings();

    /**
     * Retrieves noun mappings of the specified kind.
     *
     * @param kind the mapping kind
     * @return the noun mappings of the specified kind
     */
    default ImmutableList<NounMapping> getNounMappingsOfKind(MappingKind kind) {
        return this.getNounMappings().select(this.nounMappingIsOfKind(kind)).toImmutable();
    }

    /**
     * Retrieves noun mappings that could be of the specified kind for the given word.
     *
     * @param word the word
     * @param kind the mapping kind
     * @return the noun mappings that could be of the specified kind
     */
    default ImmutableList<NounMapping> getMappingsThatCouldBeOfKind(Word word, MappingKind kind) {
        return this.getNounMappingsByWord(word).select(mapping -> mapping.getProbabilityForKind(kind) > 0);
    }

    /**
     * Retrieves noun mappings that could be of multiple specified kinds for the given word.
     *
     * @param word  the word
     * @param kinds the mapping kinds
     * @return the noun mappings that could be of multiple specified kinds
     */
    ImmutableList<NounMapping> getMappingsThatCouldBeMultipleKinds(Word word, MappingKind... kinds);

    /**
     * Retrieves noun mappings associated with the given word.
     *
     * @param word the word
     * @return the noun mappings associated with the word
     */
    default ImmutableList<NounMapping> getNounMappingsByWord(Word word) {
        return this.getNounMappings().select(nm -> nm.getWords().contains(word));
    }

    /**
     * Retrieves noun mappings associated with the given word and of the specified kind.
     *
     * @param word the word
     * @param kind the mapping kind
     * @return the noun mappings associated with the word and of the specified kind
     */
    default ImmutableList<NounMapping> getNounMappingsByWordAndKind(Word word, MappingKind kind) {
        return this.getNounMappings().select(n -> n.getWords().contains(word)).select(this.nounMappingIsOfKind(kind)).toImmutable();
    }

    /**
     * Checks if the given word is contained by a mapping of the specified kind.
     *
     * @param word the word
     * @param kind the mapping kind
     * @return true if the word is contained by a mapping of the specified kind, false otherwise
     */
    default boolean isWordContainedByMappingKind(Word word, MappingKind kind) {
        return this.getNounMappings().select(n -> n.getWords().contains(word)).anySatisfy(this.nounMappingIsOfKind(kind));
    }

    /**
     * Retrieves noun mappings with a similar reference to the given reference.
     *
     * @param reference the reference
     * @return the noun mappings with a similar reference
     */
    ImmutableList<NounMapping> getNounMappingsWithSimilarReference(String reference);

    private Predicate<? super NounMapping> nounMappingIsOfKind(MappingKind mappingKind) {
        return n -> n.getKind() == mappingKind;
    }

}
