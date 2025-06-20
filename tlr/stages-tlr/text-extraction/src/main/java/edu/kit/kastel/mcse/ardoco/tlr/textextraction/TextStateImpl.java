/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.textextraction;

import java.io.Serial;
import java.util.Comparator;
import java.util.SortedMap;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.ordered.SortedIterable;

import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.MappingKind;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.NounMapping;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.PhraseMapping;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.TextState;
import edu.kit.kastel.mcse.ardoco.core.api.text.Phrase;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.SimilarityUtils;
import edu.kit.kastel.mcse.ardoco.core.common.util.Comparators;
import edu.kit.kastel.mcse.ardoco.core.data.AbstractState;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepositorySyncer;

/**
 * The Class TextState defines the basic implementation of a {@link TextState}.
 */
public class TextStateImpl extends AbstractState implements TextState {

    @Serial
    private static final long serialVersionUID = -8204535036570535865L;

    /**
     * Minimum difference that need to shall not be reached to identify a NounMapping as NameOrType.
     */
    private static final double MAPPING_KIND_MAX_DIFF = 0.1;
    private final MutableList<NounMapping> nounMappings;
    private final MutableList<PhraseMapping> phraseMappings;

    public TextStateImpl() {
        this.nounMappings = Lists.mutable.empty();
        this.phraseMappings = Lists.mutable.empty();
    }

    @Override
    public ImmutableList<NounMapping> getNounMappings() {
        return this.nounMappings.toImmutableList();
    }

    @Override
    public ImmutableList<PhraseMapping> getPhraseMappings() {
        return this.phraseMappings.toImmutableList();
    }

    @Override
    public PhraseMapping getPhraseMappingByNounMapping(NounMapping nounMapping) {
        ImmutableList<PhraseMapping> phraseMappingsByNounMapping = this.getPhraseMappingsByNounMapping(nounMapping);
        assert (!phraseMappingsByNounMapping.isEmpty()) : "Every noun mapping should be connected to a phrase mapping";
        return phraseMappingsByNounMapping.get(0);
    }

    private ImmutableList<PhraseMapping> getPhraseMappingsByNounMapping(NounMapping nounMapping) {
        MutableList<PhraseMapping> result = Lists.mutable.empty();

        for (Phrase phrase : nounMapping.getPhrases()) {
            result.addAll(this.phraseMappings.select(pm -> pm.getPhrases().contains(phrase)));
        }
        return result.toImmutable();
    }

    @Override
    public ImmutableList<NounMapping> getNounMappingsByPhraseMapping(PhraseMapping phraseMapping) {
        return this.getNounMappings()
                .select(nm -> Comparators.collectionsEqualsAnyOrder(phraseMapping.getPhrases().castToCollection(), nm.getPhrases().castToCollection()));
    }

    private ImmutableList<NounMapping> getNounMappingsThatBelongToTheSamePhraseMapping(NounMapping nounMapping) {
        return this.getNounMappingsByPhraseMapping(this.getPhraseMappingByNounMapping(nounMapping)).select(nm -> !nm.equals(nounMapping));
    }

    @Override
    public ImmutableList<NounMapping> getMappingsThatCouldBeMultipleKinds(Word word, MappingKind... kinds) {
        if (kinds.length == 0) {
            throw new IllegalArgumentException("You need to provide some mapping kinds!");
        }

        if (kinds.length < 2) {
            return this.getNounMappingsOfKind(kinds[0]);
        }

        MutableList<NounMapping> result = Lists.mutable.empty();
        ImmutableList<NounMapping> mappings = this.getNounMappingsByWord(word);

        for (NounMapping mapping : mappings) {
            ImmutableList<Double> probabilities = Lists.immutable.with(kinds).collect(mapping::getProbabilityForKind);
            if (probabilities.anySatisfy(p -> p <= 0)) {
                continue;
            }

            boolean similar = probabilities.allSatisfy(p1 -> probabilities.allSatisfy(p2 -> Math.abs(p1 - p2) < MAPPING_KIND_MAX_DIFF));
            if (similar) {
                result.add(mapping);
            }

        }

        return result.toImmutable();
    }

    @Override
    public ImmutableList<NounMapping> getNounMappingsWithSimilarReference(String reference) {
        return this.getNounMappings().select(nm -> SimilarityUtils.getInstance().areWordsSimilar(reference, nm.getReference())).toImmutable();
    }

    @Override
    public void addNounMapping(NounMapping nounMapping) {
        if (this.nounMappings.contains(nounMapping)) {
            throw new IllegalArgumentException("Nounmapping was already in state");
        }
        this.nounMappings.add(nounMapping);
        this.nounMappings.sortThis(ORDER_NOUNMAPPING);

        for (PhraseMapping phraseMapping : this.phraseMappings) {
            SortedIterable<Phrase> phrases = phraseMapping.getPhrases();
            SortedIterable<Phrase> phrasesOfNounMapping = nounMapping.getPhrases();
            if (Comparators.collectionsIdentityAnyOrder(phrases, phrasesOfNounMapping)) {
                return;
            }
        }
        PhraseMapping phraseMappingImpl = new PhraseMappingImpl(nounMapping.getPhrases());
        this.phraseMappings.add(phraseMappingImpl);
    }

    @Override
    public void removeNounMapping(DataRepository dataRepository, NounMapping nounMapping, NounMapping replacement, boolean cascade) {

        if (cascade) {
            PhraseMapping phraseMapping = this.getPhraseMappingByNounMapping(nounMapping);

            var otherNounMappings = this.getNounMappingsThatBelongToTheSamePhraseMapping(nounMapping);
            if (!otherNounMappings.isEmpty()) {
                var phrases = nounMapping.getPhrases().select(p -> !otherNounMappings.flatCollect(NounMapping::getPhrases).contains(p));
                phrases.forEach(phraseMapping::removePhrase);
            }
        }

        this.nounMappings.remove(nounMapping);
        DataRepositorySyncer.onNounMappingDeletion(dataRepository, nounMapping, replacement);
    }

    @Override
    public void addPhraseMapping(PhraseMapping phraseMapping) {
        this.phraseMappings.add(phraseMapping);
    }

    @Override
    public void removePhraseMapping(PhraseMapping phraseMapping, PhraseMapping replacement) {
        this.phraseMappings.remove(phraseMapping);
    }

    @Override
    public String toString() {
        return "TextExtractionState [NounMappings: \n" + this.getNounMappings() + "\n PhraseMappings: \n" + this.getPhraseMappings() + "]";
    }

    @Override
    protected void delegateApplyConfigurationToInternalObjects(SortedMap<String, String> additionalConfiguration) {
        // handle additional configuration
    }

    private static final Comparator<NounMapping> ORDER_NOUNMAPPING = (n1, n2) -> {
        if (n1.equals(n2)) {
            return 0;
        }
        var nm1 = (NounMappingImpl) n1;
        var nm2 = (NounMappingImpl) n2;
        int compare = Long.compare(nm1.earliestCreationTime(), nm2.earliestCreationTime());
        if (compare != 0) {
            return compare;
        }
        throw new IllegalStateException("NounMappings are not equal but have same creation time");
    };

}
