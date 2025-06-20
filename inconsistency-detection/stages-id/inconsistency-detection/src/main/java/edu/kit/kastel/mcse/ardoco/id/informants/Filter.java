/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.id.informants;

import edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency.InconsistencyState;
import edu.kit.kastel.mcse.ardoco.core.api.stage.recommendationgenerator.RecommendedInstance;
import edu.kit.kastel.mcse.ardoco.core.common.util.DataRepositoryHelper;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Informant;

/**
 * A Filter is a special kind of Informant that takes the existing {@link RecommendedInstance}s and removes some of them based on a given heuristic.
 * <p>
 * Heuristics are implemented by creating an implementation of {@code filterRecommendedInstances}.
 */
public abstract class Filter extends Informant {

    protected Filter(String id, DataRepository dataRepository) {
        super(id, dataRepository);
    }

    @Override
    public void process() {
        var dataRepository = getDataRepository();
        var modelStates = DataRepositoryHelper.getModelStatesData(dataRepository);
        var inconsistencyStates = DataRepositoryHelper.getInconsistencyStates(dataRepository);

        for (var model : modelStates.getMetamodels()) {
            var modelState = modelStates.getModel(model);
            var inconsistencyState = inconsistencyStates.getInconsistencyState(modelState.getMetamodel());
            filterRecommendedInstances(inconsistencyState);
        }
    }

    protected abstract void filterRecommendedInstances(InconsistencyState inconsistencyState);
}
