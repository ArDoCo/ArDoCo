/* Licensed under MIT 2024-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.textextraction;

import java.util.function.Function;

import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.TextStateStrategy;
import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;

public enum TextStateStrategies implements Function<DataRepository, TextStateStrategy> {
    DEFAULT(DefaultTextStateStrategy::new);

    private final Function<DataRepository, TextStateStrategy> creator;

    TextStateStrategies(Function<DataRepository, TextStateStrategy> creator) {
        this.creator = creator;
    }

    @Override
    public TextStateStrategy apply(DataRepository t) {
        return this.creator.apply(t);
    }
}
