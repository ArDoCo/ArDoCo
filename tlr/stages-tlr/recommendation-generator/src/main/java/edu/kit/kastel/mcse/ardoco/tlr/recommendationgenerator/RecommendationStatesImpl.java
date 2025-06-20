/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.recommendationgenerator;

import java.io.Serial;
import java.util.EnumMap;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.stage.recommendationgenerator.RecommendationStates;

public class RecommendationStatesImpl implements RecommendationStates {
    @Serial
    private static final long serialVersionUID = -6792479283538202394L;
    private final EnumMap<Metamodel, RecommendationStateImpl> recommendationStates;

    private RecommendationStatesImpl() {
        this.recommendationStates = new EnumMap<>(Metamodel.class);
    }

    public static RecommendationStates build() {
        var recStates = new RecommendationStatesImpl();
        for (Metamodel mm : Metamodel.values()) {
            recStates.recommendationStates.put(mm, new RecommendationStateImpl());
        }
        return recStates;
    }

    @Override
    public RecommendationStateImpl getRecommendationState(Metamodel metamodel) {
        return this.recommendationStates.get(metamodel);
    }
}
