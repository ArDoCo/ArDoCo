/* Licensed under MIT 2024-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.textextraction.informants;

import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.TextStateStrategy;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Informant;
import edu.kit.kastel.mcse.ardoco.tlr.textextraction.TextStateStrategies;

abstract class TextExtractionInformant extends Informant {

    private final TextStateStrategies textStateStrategyProvider;

    protected TextExtractionInformant(String id, DataRepository dataRepository) {
        super(id, dataRepository);
        this.textStateStrategyProvider = TextStateStrategies.DEFAULT;
    }

    protected TextStateStrategy getTextStateStrategy() {
        return this.textStateStrategyProvider.apply(this.getDataRepository());
    }

}
