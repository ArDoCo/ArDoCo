/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.id.agents;

import java.util.List;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.PipelineAgent;
import edu.kit.kastel.mcse.ardoco.id.informants.OccasionFilter;
import edu.kit.kastel.mcse.ardoco.id.informants.RecommendedInstanceProbabilityFilter;
import edu.kit.kastel.mcse.ardoco.id.informants.UnwantedWordsFilter;

public class InitialInconsistencyAgent extends PipelineAgent {

    public InitialInconsistencyAgent(DataRepository dataRepository) {
        super(List.of(new RecommendedInstanceProbabilityFilter(dataRepository), new OccasionFilter(dataRepository), new UnwantedWordsFilter(dataRepository)),
                InitialInconsistencyAgent.class.getSimpleName(), dataRepository);

    }

    @Override
    protected void initializeState() {
        var dataRepository = getDataRepository();
        var modelStates = DataRepositoryHelper.getModelStatesData(dataRepository);
        var recommendationStates = DataRepositoryHelper.getRecommendationStates(dataRepository);
        var inconsistencyStates = DataRepositoryHelper.getInconsistencyStates(dataRepository);
        for (var model : modelStates.getMetamodels()) {
            var modelState = modelStates.getModel(model);
            Metamodel metamodel = modelState.getMetamodel();
            var inconsistencyState = inconsistencyStates.getInconsistencyState(metamodel);
            var recommendationState = recommendationStates.getRecommendationState(metamodel);

            inconsistencyState.addRecommendedInstances(recommendationState.getRecommendedInstances().toList());
        }
    }
}
