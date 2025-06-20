/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.connectiongenerator.informants;

import java.util.List;
import java.util.SortedMap;
import java.util.stream.Collectors;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import edu.kit.kastel.mcse.ardoco.core.api.entity.Entity;
import edu.kit.kastel.mcse.ardoco.core.api.entity.ModelEntity;
import edu.kit.kastel.mcse.ardoco.core.api.models.ModelStates;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.Model;
import edu.kit.kastel.mcse.ardoco.core.api.stage.recommendationgenerator.RecommendationState;
import edu.kit.kastel.mcse.ardoco.core.api.stage.recommendationgenerator.RecommendationStates;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.MappingKind;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.NounMapping;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.TextState;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.TextStateStrategy;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.SimilarityUtils;
import edu.kit.kastel.mcse.ardoco.core.common.util.CommonUtilities;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.configuration.Configurable;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Informant;
import edu.kit.kastel.mcse.ardoco.tlr.textextraction.TextStateStrategies;

/**
 * This analyzer searches for name type patterns. If these patterns occur recommendations are created.
 */
public class NameTypeConnectionInformant extends Informant {

    @Configurable
    private double probability = 1.0;
    private static final TextStateStrategies STRATEGY = TextStateStrategies.DEFAULT;

    public NameTypeConnectionInformant(DataRepository dataRepository) {
        super(NameTypeConnectionInformant.class.getSimpleName(), dataRepository);
    }

    @Override
    public void process() {
        DataRepository dataRepository = this.getDataRepository();
        var text = DataRepositoryHelper.getAnnotatedText(dataRepository);
        var textState = DataRepositoryHelper.getTextState(dataRepository);
        var textStateStrategy = STRATEGY.apply(this.getDataRepository());

        var modelStates = DataRepositoryHelper.getModelStatesData(dataRepository);
        var recommendationStates = DataRepositoryHelper.getRecommendationStates(dataRepository);
        for (var word : text.words()) {
            this.exec(textState, textStateStrategy, modelStates, recommendationStates, word);
        }
    }

    private void exec(TextState textState, TextStateStrategy tss, ModelStates modelStates, RecommendationStates recommendationStates, Word word) {

        for (var metamodel : modelStates.getMetamodels()) {
            var model = modelStates.getModel(metamodel);
            if (model == null) {
                continue;
            }
            var recommendationState = recommendationStates.getRecommendationState(model.getMetamodel());
            this.checkForNameAfterType(textState, tss, word, model, recommendationState);
            this.checkForNameBeforeType(textState, tss, word, model, recommendationState);
            this.checkForNortBeforeType(textState, tss, word, model, recommendationState);
            this.checkForNortAfterType(textState, tss, word, model, recommendationState);
        }
    }

    /**
     * Checks if the current node is a type in the text extraction state. If the names of the text extraction state contain the previous node. If that's the
     * case a recommendation for the combination of both is created.
     */
    private void checkForNameBeforeType(TextState textExtractionState, TextStateStrategy tss, Word word, Model model, RecommendationState recommendationState) {
        if (textExtractionState == null || word == null) {
            return;
        }

        var preWord = word.getPreWord();

        var similarTypes = CommonUtilities.getSimilarTypes(word, model);

        if (!similarTypes.isEmpty()) {
            tss.addNounMapping(word, MappingKind.TYPE, this, this.probability);

            var nameMappings = textExtractionState.getMappingsThatCouldBeOfKind(preWord, MappingKind.NAME);
            var typeMappings = textExtractionState.getMappingsThatCouldBeOfKind(word, MappingKind.TYPE);

            var entity = this.tryToIdentify(textExtractionState, similarTypes, preWord, model);
            this.addRecommendedInstanceIfNodeNotNull(word, textExtractionState, entity, nameMappings, typeMappings, recommendationState);
        }
    }

    /**
     * Checks if the current node is a type in the text extraction state. If the names of the text extraction state contain the following node. If that's the
     * case a recommendation for the combination of both is created.
     *
     * @param textExtractionState text extraction state
     * @param word                the current word
     * @param model               the current model state
     * @param recommendationState the current recommendation state
     */
    private void checkForNameAfterType(TextState textExtractionState, TextStateStrategy tss, Word word, Model model, RecommendationState recommendationState) {
        if (textExtractionState == null || word == null) {
            return;
        }

        var after = word.getNextWord();

        var sameLemmaTypes = CommonUtilities.getSimilarTypes(word, model);
        if (!sameLemmaTypes.isEmpty()) {
            tss.addNounMapping(word, MappingKind.TYPE, this, this.probability);

            var typeMappings = textExtractionState.getMappingsThatCouldBeOfKind(word, MappingKind.TYPE);
            var nameMappings = textExtractionState.getMappingsThatCouldBeOfKind(after, MappingKind.NAME);

            var instance = this.tryToIdentify(textExtractionState, sameLemmaTypes, after, model);
            this.addRecommendedInstanceIfNodeNotNull(word, textExtractionState, instance, nameMappings, typeMappings, recommendationState);
        }
    }

    /**
     * Checks if the current node is a type in the text extraction state. If the name_or_types of the text extraction state contain the previous node. If that's
     * the case a recommendation for the combination of both is created.
     */
    private void checkForNortBeforeType(TextState textExtractionState, TextStateStrategy tss, Word word, Model model, RecommendationState recommendationState) {
        if (textExtractionState == null || word == null) {
            return;
        }

        var preWord = word.getPreWord();

        var sameLemmaTypes = CommonUtilities.getSimilarTypes(word, model);

        if (!sameLemmaTypes.isEmpty()) {
            tss.addNounMapping(word, MappingKind.TYPE, this, this.probability);

            var typeMappings = textExtractionState.getMappingsThatCouldBeOfKind(word, MappingKind.TYPE);
            var nortMappings = textExtractionState.getMappingsThatCouldBeMultipleKinds(preWord, MappingKind.NAME, MappingKind.TYPE);

            var instance = this.tryToIdentify(textExtractionState, sameLemmaTypes, preWord, model);
            this.addRecommendedInstanceIfNodeNotNull(word, textExtractionState, instance, nortMappings, typeMappings, recommendationState);
        }
    }

    /**
     * Checks if the current node is a type in the text extraction state. If the name_or_types of the text extraction state contain the afterwards node. If
     * that's the case a recommendation for the combination of both is created.
     */
    private void checkForNortAfterType(TextState textExtractionState, TextStateStrategy tss, Word word, Model model, RecommendationState recommendationState) {
        if (textExtractionState == null || word == null) {
            return;
        }

        var after = word.getNextWord();

        var sameLemmaTypes = CommonUtilities.getSimilarTypes(word, model);
        if (!sameLemmaTypes.isEmpty()) {
            tss.addNounMapping(word, MappingKind.TYPE, this, this.probability);

            var typeMappings = textExtractionState.getMappingsThatCouldBeOfKind(word, MappingKind.TYPE);
            var nortMappings = textExtractionState.getMappingsThatCouldBeMultipleKinds(after, MappingKind.NAME, MappingKind.TYPE);

            var instance = this.tryToIdentify(textExtractionState, sameLemmaTypes, after, model);
            this.addRecommendedInstanceIfNodeNotNull(word, textExtractionState, instance, nortMappings, typeMappings, recommendationState);
        }
    }

    /**
     * Adds a RecommendedInstance to the recommendation state if the mapping of the current node exists. Otherwise a recommendation is added for each existing
     * mapping.
     *
     * @param currentWord         the current node
     * @param textExtractionState the text extraction state
     * @param entity              the instance
     * @param nameMappings        the name mappings
     * @param typeMappings        the type mappings
     */
    private void addRecommendedInstanceIfNodeNotNull(//
            Word currentWord, TextState textExtractionState, Entity entity, ImmutableList<NounMapping> nameMappings, ImmutableList<NounMapping> typeMappings,
            RecommendationState recommendationState) {
        var nounMappingsByCurrentWord = textExtractionState.getNounMappingsByWord(currentWord);
        if (entity != null && nounMappingsByCurrentWord != null) {
            for (NounMapping nmapping : nounMappingsByCurrentWord) {
                var name = entity.getName();
                var type = nmapping.getReference();
                recommendationState.addRecommendedInstance(name, type, this, this.probability, nameMappings, typeMappings);
            }
        }
    }

    /**
     * Tries to identify instances by the given similar types and the name of a given node. If an unambiguous instance can be found it is returned and the name
     * is added to the text extraction state.
     *
     * @param textExtractionState the next extraction state to work with
     * @param similarTypes        the given similar types
     * @param word                the node for name identification
     * @return the unique matching instance
     */
    private Entity tryToIdentify(TextState textExtractionState, ImmutableList<String> similarTypes, Word word, Model model) {
        if (textExtractionState == null || similarTypes == null || word == null) {
            return null;
        }
        MutableList<ModelEntity> matchingEntities = Lists.mutable.empty();

        for (String type : similarTypes) {
            var entitiesOfType = getEntitiesOfType(model, type);
            matchingEntities.addAll(entitiesOfType);
        }

        var text = word.getText();
        matchingEntities = matchingEntities.select(e -> SimilarityUtils.getInstance().areWordsOfListsSimilar(e.getNameParts(), Lists.immutable.with(text)));

        if (!matchingEntities.isEmpty()) {
            return matchingEntities.getFirst();
        }
        return null;
    }

    private List<ModelEntity> getEntitiesOfType(Model model, String type) {
        var entitiesWithTypeParts = model.getEndpoints().stream().filter(e -> e.getTypeParts().isPresent());
        return entitiesWithTypeParts.filter(e -> e.getTypeParts().orElseThrow().contains(type)).collect(Collectors.toList());
    }

    @Override
    protected void delegateApplyConfigurationToInternalObjects(SortedMap<String, String> additionalConfiguration) {
        // handle additional configuration
    }

}
