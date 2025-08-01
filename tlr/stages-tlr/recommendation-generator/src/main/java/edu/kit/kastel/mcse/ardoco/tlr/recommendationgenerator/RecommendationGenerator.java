/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.recommendationgenerator;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.map.sorted.ImmutableSortedMap;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.models.ModelStates;
import edu.kit.kastel.mcse.ardoco.core.api.stage.recommendationgenerator.RecommendationStates;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.pipeline.AbstractExecutionStage;
import edu.kit.kastel.mcse.ardoco.tlr.recommendationgenerator.agents.InitialRecommendationAgent;
import edu.kit.kastel.mcse.ardoco.tlr.recommendationgenerator.agents.PhraseRecommendationAgent;

/**
 * The Class RecommendationGenerator defines the recommendation stage.
 */
public class RecommendationGenerator extends AbstractExecutionStage {

    /**
     * Creates a new model connection agent with the given extraction state and ntr state.
     */
    public RecommendationGenerator(DataRepository dataRepository) {
        super(Lists.mutable.of(//
                //new TermBuilder(dataRepository),//
                new InitialRecommendationAgent(dataRepository),//
                new PhraseRecommendationAgent(dataRepository)),//
                RecommendationGenerator.class.getSimpleName(), dataRepository);
    }

    /**
     * Creates a {@link RecommendationGenerator} and applies the additional configuration to it.
     *
     * @param additionalConfigs the additional configuration
     * @param dataRepository    the data repository
     * @return an instance of {@link RecommendationGenerator}
     */
    public static RecommendationGenerator get(ImmutableSortedMap<String, String> additionalConfigs, DataRepository dataRepository) {
        var recommendationGenerator = new RecommendationGenerator(dataRepository);
        recommendationGenerator.applyConfiguration(additionalConfigs);
        return recommendationGenerator;
    }

    @Override
    protected void initializeState() {
        var activeMetamodels = this.getDataRepository().getData(ModelStates.ID, ModelStates.class).orElseThrow().getMetamodels();
        var recommendationStates = RecommendationStatesImpl.build(activeMetamodels.toArray(Metamodel[]::new));
        this.getDataRepository().addData(RecommendationStates.ID, recommendationStates);
    }
}
