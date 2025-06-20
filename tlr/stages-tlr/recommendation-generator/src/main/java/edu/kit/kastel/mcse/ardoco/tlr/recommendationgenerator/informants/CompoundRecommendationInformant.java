/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.recommendationgenerator.informants;

import java.util.SortedMap;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.SortedSets;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.sorted.MutableSortedSet;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.Model;
import edu.kit.kastel.mcse.ardoco.core.api.stage.recommendationgenerator.RecommendationState;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.MappingKind;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.NounMapping;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.TextState;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.SimilarityUtils;
import edu.kit.kastel.mcse.ardoco.core.common.util.CommonUtilities;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.configuration.Configurable;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Informant;

public class CompoundRecommendationInformant extends Informant {

    @Configurable
    private double confidence = 0.8;

    public CompoundRecommendationInformant(DataRepository dataRepository) {
        super(CompoundRecommendationInformant.class.getSimpleName(), dataRepository);
    }

    private static ImmutableList<Word> getCompoundWordsFromNounMapping(NounMapping nounMapping) {
        ImmutableList<Word> compoundWords = Lists.immutable.empty();
        for (var word : nounMapping.getWords()) {
            var currentCompoundWords = CommonUtilities.getCompoundWords(word);
            if (currentCompoundWords.size() > compoundWords.size()) {
                compoundWords = currentCompoundWords;
            }
        }
        return compoundWords;
    }

    @Override
    public void process() {
        DataRepository dataRepository = this.getDataRepository();
        var modelStates = DataRepositoryHelper.getModelStatesData(dataRepository);
        var textState = DataRepositoryHelper.getTextState(dataRepository);
        var recommendationStates = DataRepositoryHelper.getRecommendationStates(dataRepository);

        for (var metamodel : modelStates.getMetamodels()) {
            var model = modelStates.getModel(metamodel);
            if (model == null) {
                continue;
            }
            var recommendationState = recommendationStates.getRecommendationState(metamodel);

            this.createRecommendationInstancesFromCompoundNounMappings(textState, recommendationState, model);
            this.findMoreCompoundsForRecommendationInstances(textState, recommendationState, model);
            this.findSpecialNamedEntitities(textState, recommendationState);
        }
    }

    /**
     * Look at NounMappings and add RecommendedInstances, if a NounMapping was created because of a compound (in text-extraction)
     */
    private void createRecommendationInstancesFromCompoundNounMappings(TextState textState, RecommendationState recommendationState, Model model) {
        for (var nounMapping : textState.getNounMappings()) {
            if (nounMapping.isCompound()) {
                var typeMappings = this.getRelatedTypeMappings(nounMapping, textState);
                this.addRecommendedInstance(nounMapping, typeMappings, recommendationState, model);
            }
        }
    }

    /**
     * Find additional compounds and create RecommendedInstances for them. Additional compounds are when a word in a NounMapping has another word in front or
     * afterwards and that compounds is a TypeMapping
     */
    private void findMoreCompoundsForRecommendationInstances(TextState textState, RecommendationState recommendationState, Model model) {
        for (var nounMapping : textState.getNounMappings()) {
            for (var word : nounMapping.getWords()) {
                var prevWord = word.getPreWord();
                this.addRecommendedInstanceIfCompoundWithOtherWord(nounMapping, prevWord, textState, recommendationState, model);

                var nextWord = word.getNextWord();
                this.addRecommendedInstanceIfCompoundWithOtherWord(nounMapping, nextWord, textState, recommendationState, model);
            }
        }
    }

    /**
     * Find words that use CamelCase or snake_case.
     */
    private void findSpecialNamedEntitities(TextState textState, RecommendationState recommendationState) {
        this.findSpecialNamedEntitiesInNounMappings(textState.getNounMappingsOfKind(MappingKind.NAME), recommendationState);
    }

    private void findSpecialNamedEntitiesInNounMappings(ImmutableList<NounMapping> nounMappings, RecommendationState recommendationState) {
        for (var nounMapping : nounMappings) {
            for (var word : nounMapping.getWords()) {
                var wordText = word.getText();
                if (CommonUtilities.isCamelCasedWord(wordText) || CommonUtilities.nameIsSnakeCased(wordText)) {
                    var localNounMappings = Lists.immutable.of(nounMapping);
                    recommendationState.addRecommendedInstance(nounMapping.getReference(), "", this, this.confidence, localNounMappings, Lists.immutable
                            .empty());
                }
            }
        }
    }

    private void addRecommendedInstance(NounMapping nounMapping, ImmutableList<NounMapping> typeMappings, RecommendationState recommendationState,
            Model model) {
        var nounMappings = Lists.immutable.of(nounMapping);
        var types = this.getSimilarModelTypes(typeMappings, model);
        if (types.isEmpty()) {
            recommendationState.addRecommendedInstance(nounMapping.getReference(), "", this, this.confidence, nounMappings, typeMappings);
        } else {
            for (var type : types) {
                recommendationState.addRecommendedInstance(nounMapping.getReference(), type, this, this.confidence, nounMappings, typeMappings);
            }
        }
    }

    private ImmutableList<String> getSimilarModelTypes(ImmutableList<NounMapping> typeMappings, Model model) {
        MutableSortedSet<String> similarModelTypes = SortedSets.mutable.empty();
        var typeIdentifiers = CommonUtilities.getSplittedTypeIdentifiers(model);
        for (var typeMapping : typeMappings) {
            var currSimilarTypes = Lists.immutable.fromStream(typeIdentifiers.stream()
                    .filter(typeId -> SimilarityUtils.getInstance().areWordsSimilar(typeId, typeMapping.getReference())));
            similarModelTypes.addAll(currSimilarTypes.toList());
            for (var word : typeMapping.getWords()) {
                currSimilarTypes = Lists.immutable.fromStream(typeIdentifiers.stream()
                        .filter(typeId -> SimilarityUtils.getInstance().areWordsSimilar(typeId, word.getLemma())));
                similarModelTypes.addAll(currSimilarTypes.toList());
            }
        }
        return similarModelTypes.toList().toImmutable();
    }

    private ImmutableList<NounMapping> getRelatedTypeMappings(NounMapping nounMapping, TextState textState) {
        MutableList<NounMapping> typeMappings = Lists.mutable.empty();
        // find TypeMappings that come from the Compound Words within the Compound Word
        var compoundWords = getCompoundWordsFromNounMapping(nounMapping);
        for (var word : compoundWords) {
            typeMappings.addAll(textState.getNounMappingsByWordAndKind(word, MappingKind.TYPE).toList());
        }
        return typeMappings.toImmutable();
    }

    private void addRecommendedInstanceIfCompoundWithOtherWord(NounMapping nounMapping, Word word, TextState textState, RecommendationState recommendationState,
            Model model) {
        if (word == null) {
            return;
        }

        if (word.getPosTag().isNoun()) {
            var typeMappings = textState.getMappingsThatCouldBeOfKind(word, MappingKind.TYPE);
            if (!typeMappings.isEmpty()) {
                this.addRecommendedInstance(nounMapping, typeMappings, recommendationState, model);
            }
        }
    }

    @Override
    protected void delegateApplyConfigurationToInternalObjects(SortedMap<String, String> map) {
        // empty
    }
}
