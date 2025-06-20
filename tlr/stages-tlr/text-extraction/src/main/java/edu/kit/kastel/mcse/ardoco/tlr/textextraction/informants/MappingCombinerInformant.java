/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.textextraction.informants;

import java.util.SortedMap;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.NounMapping;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.PhraseMapping;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.TextState;
import edu.kit.kastel.mcse.ardoco.core.api.text.Phrase;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.PhraseMappingAggregatorStrategy;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.SimilarityUtils;
import edu.kit.kastel.mcse.ardoco.core.common.tuple.Pair;
import edu.kit.kastel.mcse.ardoco.core.common.util.Comparators;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.configuration.Configurable;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;

public class MappingCombinerInformant extends TextExtractionInformant {

    @Configurable
    private double minCosineSimilarity = 0.4;

    public MappingCombinerInformant(DataRepository dataRepository) {
        super(MappingCombinerInformant.class.getSimpleName(), dataRepository);
    }

    @Override
    public void process() {
        TextState textState = DataRepositoryHelper.getTextState(this.getDataRepository());
        this.combineSimilarPhraseMappings(textState);
    }

    private void combineSimilarPhraseMappings(TextState textState) {

        ImmutableList<PhraseMapping> phraseMappings = textState.getPhraseMappings();

        for (PhraseMapping phraseMapping : phraseMappings) {
            ImmutableList<PhraseMapping> similarPhraseMappings = phraseMappings.select(p -> SimilarityUtils.getInstance()
                    .getPhraseMappingSimilarity(textState, phraseMapping, p, PhraseMappingAggregatorStrategy.MAX_SIMILARITY) > this.minCosineSimilarity);

            // Remove the phrase mapping from the list of similar phrase mappings
            // Comment: This would break the logic but seems to be logical ..
            // similarPhraseMappings = similarPhraseMappings.newWithout(phraseMapping);

            this.processPhraseMappingForSimilarPhraseMappings(textState, similarPhraseMappings, phraseMapping);
        }

    }

    private void processPhraseMappingForSimilarPhraseMappings(TextState textState, ImmutableList<PhraseMapping> similarPhraseMappings,
            PhraseMapping phraseMapping) {
        ImmutableList<NounMapping> nounMappingsOfPhraseMapping = textState.getNounMappingsByPhraseMapping(phraseMapping);

        for (PhraseMapping similarPhraseMapping : similarPhraseMappings) {

            ImmutableList<NounMapping> nounMappingsOfSimilarPhraseMapping = textState.getNounMappingsByPhraseMapping(similarPhraseMapping);

            if (Comparators.collectionsEqualsAnyOrder(similarPhraseMapping.getPhrases().collect(Phrase::getText), phraseMapping.getPhrases()
                    .collect(Phrase::getText))) {
                this.processSimilarPhraseMappingWhenEqualPhraseText(textState, phraseMapping, nounMappingsOfSimilarPhraseMapping);
                continue;
            }

            if (nounMappingsOfPhraseMapping.size() == nounMappingsOfSimilarPhraseMapping.size()) {
                this.processSimilarPhraseMappingWhenEquallySized(similarPhraseMappings, phraseMapping, nounMappingsOfPhraseMapping, similarPhraseMapping,
                        nounMappingsOfSimilarPhraseMapping);
            }
        }
    }

    private void processSimilarPhraseMappingWhenEquallySized(ImmutableList<PhraseMapping> similarPhraseMappings, PhraseMapping phraseMapping,
            ImmutableList<NounMapping> nounMappingsOfPhraseMapping, PhraseMapping similarPhraseMapping,
            ImmutableList<NounMapping> nounMappingsOfSimilarPhraseMapping) {
        MutableList<Pair<NounMapping, NounMapping>> similarNounMappings = Lists.mutable.empty();

        for (NounMapping nounMapping : nounMappingsOfPhraseMapping) {
            NounMapping similarNounMapping = this.getMostSimilarNounMappingOverThreshold(nounMapping, nounMappingsOfSimilarPhraseMapping);
            if (similarNounMapping != null) {
                similarNounMappings.add(new Pair<>(nounMapping, similarNounMapping));
            }
        }

        boolean merge = true;
        merge &= similarNounMappings.size() == nounMappingsOfPhraseMapping.size();
        merge &= similarPhraseMappings.size() == similarNounMappings.collect(Pair::second).distinct().size() * 2;

        if (merge) {
            this.getTextStateStrategy().mergePhraseMappingsAndNounMappings(phraseMapping, similarPhraseMapping, similarNounMappings, this);
        }
    }

    private void processSimilarPhraseMappingWhenEqualPhraseText(TextState textState, PhraseMapping phraseMapping,
            ImmutableList<NounMapping> nounMappingsOfSimilarPhraseMapping) {
        for (NounMapping nounMappingOfSimilarPhraseMapping : nounMappingsOfSimilarPhraseMapping) {
            for (NounMapping nounMapping : textState.getNounMappingsByPhraseMapping(phraseMapping)) {
                if (nounMapping == nounMappingOfSimilarPhraseMapping || !textState.getNounMappings().contains(nounMappingOfSimilarPhraseMapping) || !textState
                        .getNounMappings()
                        .contains(nounMapping)) {
                    continue;
                }
                if (SimilarityUtils.getInstance().areNounMappingsSimilar(nounMapping, nounMappingOfSimilarPhraseMapping)) {
                    this.getTextStateStrategy().mergeNounMappings(nounMapping, nounMappingOfSimilarPhraseMapping, this);
                }
            }
        }
    }

    private NounMapping getMostSimilarNounMappingOverThreshold(NounMapping nounMapping, ImmutableList<NounMapping> nounMappingsOfSimilarPhraseMapping) {

        MutableList<NounMapping> similarNounMappings = Lists.mutable.empty();

        for (NounMapping nounMappingOfSimilarPhraseMapping : nounMappingsOfSimilarPhraseMapping) {
            if (SimilarityUtils.getInstance().areNounMappingsSimilar(nounMapping, nounMappingOfSimilarPhraseMapping)) {
                similarNounMappings.add(nounMappingOfSimilarPhraseMapping);
            }
        }

        if (similarNounMappings.size() != 1) {
            return null;
        }

        return similarNounMappings.getFirst();
    }

    @Override
    protected void delegateApplyConfigurationToInternalObjects(SortedMap<String, String> map) {
        //none
    }
}
