/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.textextraction;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.SortedMaps;
import org.eclipse.collections.api.factory.SortedSets;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.sorted.ImmutableSortedMap;
import org.eclipse.collections.api.map.sorted.MutableSortedMap;
import org.eclipse.collections.api.set.sorted.ImmutableSortedSet;
import org.eclipse.collections.api.set.sorted.MutableSortedSet;

import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.MappingKind;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.NounMapping;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.PhraseMapping;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.TextState;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.TextStateStrategy;
import edu.kit.kastel.mcse.ardoco.core.api.text.Phrase;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.SimilarityUtils;
import edu.kit.kastel.mcse.ardoco.core.common.tuple.Pair;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.data.Confidence;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Claimant;

@Deterministic
public final class DefaultTextStateStrategy implements TextStateStrategy {
    private final TextState textState;
    private final DataRepository dataRepository;

    public DefaultTextStateStrategy(DataRepository dataRepository) {
        this.dataRepository = Objects.requireNonNull(dataRepository);
        this.textState = Objects.requireNonNull(DataRepositoryHelper.getTextState(dataRepository));
    }

    /**
     * Creates a new noun mapping using the parameters without adding it to the state.
     *
     * @param words          the words
     * @param distribution   the distribution of the mappings kinds
     * @param referenceWords the reference words
     * @param surfaceForms   the surface forms
     * @param reference      the joined reference, nullable
     * @return the created noun mapping
     */
    public NounMapping createNounMappingStateless(ImmutableSortedSet<Word> words, ImmutableSortedMap<MappingKind, Confidence> distribution,
            ImmutableList<Word> referenceWords, ImmutableList<String> surfaceForms, String reference) {
        if (reference == null) {
            reference = this.calculateNounMappingReference(referenceWords);
        }

        return new NounMappingImpl(words, distribution.toImmutable(), referenceWords, surfaceForms, reference);
    }

    @Override
    public NounMapping addNounMapping(ImmutableSortedSet<Word> words, ImmutableSortedMap<MappingKind, Confidence> distribution,
            ImmutableList<Word> referenceWords, ImmutableList<String> surfaceForms, String reference) {
        //Do not add noun mappings to the state, which do not have any claimants
        if (distribution.valuesView().noneSatisfy(d -> !d.getClaimants().isEmpty())) {
            throw new IllegalArgumentException("Atleast 1 claimant is required");
        }

        NounMapping nounMapping = this.createNounMappingStateless(words, distribution, referenceWords, surfaceForms, reference);
        this.textState.addNounMapping(nounMapping);
        return nounMapping;
    }

    @Override
    public NounMapping addNounMapping(ImmutableSortedSet<Word> words, MappingKind kind, Claimant claimant, double probability,
            ImmutableList<Word> referenceWords, ImmutableList<String> surfaceForms, String reference) {
        MutableSortedMap<MappingKind, Confidence> distribution = SortedMaps.mutable.empty();
        distribution.put(MappingKind.NAME, new Confidence(DEFAULT_AGGREGATOR));
        distribution.put(MappingKind.TYPE, new Confidence(DEFAULT_AGGREGATOR));
        var nounMapping = this.createNounMappingStateless(words, distribution.toImmutable(), referenceWords, surfaceForms, reference);
        nounMapping.addKindWithProbability(kind, claimant, probability);
        this.textState.addNounMapping(nounMapping);
        return nounMapping;
    }

    @Override
    public void mergeNounMappings(NounMapping nounMapping, NounMapping textuallyEqualNounMapping, Claimant claimant) {
        this.mergeNounMappings(nounMapping, textuallyEqualNounMapping, null, null, nounMapping.getKind(), claimant, nounMapping.getProbabilityForKind(
                nounMapping.getKind()));

    }

    @Override
    public void mergePhraseMappingsAndNounMappings(PhraseMapping phraseMapping, PhraseMapping similarPhraseMapping,
            MutableList<Pair<NounMapping, NounMapping>> similarNounMappings, Claimant claimant) {
        this.mergePhraseMappings(phraseMapping, similarPhraseMapping);
        for (Pair<NounMapping, NounMapping> nounMappingPair : similarNounMappings) {
            this.mergeNounMappings(nounMappingPair.first(), nounMappingPair.second(), claimant);
        }
    }

    private void mergePhraseMappings(PhraseMapping phraseMapping, PhraseMapping similarPhraseMapping) {
        MutableSortedSet<Phrase> mergedPhrases = phraseMapping.getPhrases().toSortedSet();
        mergedPhrases.addAll(similarPhraseMapping.getPhrases().toList());

        PhraseMapping mergedPhraseMapping = new PhraseMappingImpl(mergedPhrases.toImmutable());
        this.textState.addPhraseMapping(mergedPhraseMapping);
        this.textState.removePhraseMapping(phraseMapping, mergedPhraseMapping);
        this.textState.removePhraseMapping(similarPhraseMapping, mergedPhraseMapping);
    }

    private Confidence putAllConfidencesTogether(Confidence confidence, Confidence confidence1) {
        Confidence result = confidence.createCopy();
        result.addAllConfidences(confidence1);
        return result;
    }

    @Override
    public NounMapping addNounMapping(Word word, MappingKind kind, Claimant claimant, double probability, ImmutableList<String> surfaceForms) {

        NounMapping disposableNounMapping = new NounMappingImpl(SortedSets.immutable.with(word), kind, claimant, probability, Lists.immutable.with(word),
                surfaceForms);

        for (var existingNounMapping : this.textState.getNounMappings()) {
            if (SimilarityUtils.getInstance().areNounMappingsSimilar(disposableNounMapping, existingNounMapping)) {

                return this.mergeNounMappings(existingNounMapping, disposableNounMapping, disposableNounMapping.getReferenceWords(), disposableNounMapping
                        .getReference(), disposableNounMapping.getKind(), claimant, disposableNounMapping.getProbability());
            }
        }

        this.textState.addNounMapping(disposableNounMapping);
        return disposableNounMapping;
    }

    @Override
    public NounMappingImpl mergeNounMappingsStateless(NounMapping firstNounMapping, NounMapping secondNounMapping, ImmutableList<Word> referenceWords,
            String reference, MappingKind mappingKind, Claimant claimant, double probability) {

        MutableSortedSet<Word> mergedWords = firstNounMapping.getWords().toSortedSet();
        mergedWords.add(secondNounMapping.getReferenceWords().get(0));
        //This makes only sense under specific conditions, since it is sequentially dependent. It might be fixed in future versions

        var existingNounMappingDistribution = firstNounMapping.getDistribution();
        var disposableNounMappingDistribution = secondNounMapping.getDistribution();
        var mergedRawMap = Arrays.stream(MappingKind.values())
                .collect(Collectors.toMap( //
                        kind -> kind, //
                        kind -> this.putAllConfidencesTogether(existingNounMappingDistribution.get(kind), disposableNounMappingDistribution.get(kind)) //
                ));
        MutableSortedMap<MappingKind, Confidence> mergedDistribution = SortedMaps.mutable.withSortedMap(mergedRawMap);

        MutableList<String> mergedSurfaceForms = firstNounMapping.getSurfaceForms().toList();
        for (var surface : secondNounMapping.getSurfaceForms()) {
            if (mergedSurfaceForms.contains(surface)) {
                continue;
            }
            mergedSurfaceForms.add(surface);
        }

        ImmutableList<Word> mergedReferenceWords = firstNounMapping.getReferenceWords();

        String mergedReference = mergedReferenceWords.collect(Word::getText).makeString(" ");

        return new NounMappingImpl(NounMappingImpl.earliestCreationTime(firstNounMapping, secondNounMapping), mergedWords.toSortedSet().toImmutable(),
                mergedDistribution.toImmutable(), mergedReferenceWords.toImmutable(), mergedSurfaceForms.toImmutable(), mergedReference);
    }

    @Override
    public NounMappingImpl mergeNounMappings(NounMapping firstNounMapping, NounMapping secondNounMapping, ImmutableList<Word> referenceWords, String reference,
            MappingKind mappingKind, Claimant claimant, double probability) {
        var mergedNounMapping = this.mergeNounMappingsStateless(firstNounMapping, secondNounMapping, referenceWords, reference, mappingKind, claimant,
                probability);

        // We just need to remove them plain from the state -> no cascade.
        this.textState.removeNounMapping(this.dataRepository, firstNounMapping, mergedNounMapping, false);
        this.textState.removeNounMapping(this.dataRepository, secondNounMapping, mergedNounMapping, false);

        this.textState.addNounMapping(mergedNounMapping);
        return mergedNounMapping;
    }

}
