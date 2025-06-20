/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.recommendationgenerator.agents;

import java.util.List;

import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.PipelineAgent;
import edu.kit.kastel.mcse.ardoco.tlr.recommendationgenerator.informants.NameTypeInformant;
import edu.kit.kastel.mcse.ardoco.tlr.textextraction.TextStateStrategies;

/**
 * The Class InitialRecommendationAgent runs all extractors of this stage.
 */
public class InitialRecommendationAgent extends PipelineAgent {
    public InitialRecommendationAgent(DataRepository dataRepository) {
        super(List.of(new NameTypeInformant(dataRepository, TextStateStrategies.DEFAULT)), InitialRecommendationAgent.class.getSimpleName(), dataRepository);
    }
}
