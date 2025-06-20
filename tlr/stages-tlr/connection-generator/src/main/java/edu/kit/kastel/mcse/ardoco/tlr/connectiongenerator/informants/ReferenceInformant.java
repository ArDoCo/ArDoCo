/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.connectiongenerator.informants;

import java.util.SortedMap;

import org.eclipse.collections.api.list.ImmutableList;

import edu.kit.kastel.mcse.ardoco.core.api.entity.ModelEntity;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.Model;
import edu.kit.kastel.mcse.ardoco.core.api.stage.recommendationgenerator.RecommendationState;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.MappingKind;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.NounMapping;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.TextState;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.SimilarityUtils;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.configuration.Configurable;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Informant;

public class ReferenceInformant extends Informant {

    @Configurable
    private double probability = 0.75;

    public ReferenceInformant(DataRepository dataRepository) {
        super(ReferenceInformant.class.getSimpleName(), dataRepository);
    }

    @Override
    public void process() {
        DataRepository dataRepository = this.getDataRepository();
        var textState = DataRepositoryHelper.getTextState(dataRepository);
        var modelStates = DataRepositoryHelper.getModelStatesData(dataRepository);
        var recommendationStates = DataRepositoryHelper.getRecommendationStates(dataRepository);
        for (var metamodel : modelStates.getMetamodels()) {
            var model = modelStates.getModel(metamodel);
            if (model == null) {
                continue;
            }
            var recommendationState = recommendationStates.getRecommendationState(metamodel);
            this.findRecommendedInstancesFromNounMappingsThatAreSimilarToInstances(model, recommendationState, textState);
        }
    }

    /**
     * Searches for instances mentioned in the text extraction state as names. If it founds some similar names it creates recommendations.
     */
    private void findRecommendedInstancesFromNounMappingsThatAreSimilarToInstances(Model model, RecommendationState recommendationState, TextState textState) {
        for (ModelEntity modelEntity : model.getEndpoints()) {
            var similarToInstanceMappings = this.getSimilarNounMappings(modelEntity, textState);

            for (NounMapping similarNameMapping : similarToInstanceMappings) {
                recommendationState.addRecommendedInstance(similarNameMapping.getReference(), this, this.probability, similarToInstanceMappings);
            }
        }

    }

    private ImmutableList<NounMapping> getSimilarNounMappings(ModelEntity modelEntity, TextState textState) {
        var nameMappings = textState.getNounMappingsOfKind(MappingKind.NAME);
        return nameMappings.select(nounMapping -> SimilarityUtils.getInstance().isNounMappingSimilarToModelInstance(nounMapping, modelEntity));
    }

    @Override
    protected void delegateApplyConfigurationToInternalObjects(SortedMap<String, String> map) {
        // empty
    }
}
