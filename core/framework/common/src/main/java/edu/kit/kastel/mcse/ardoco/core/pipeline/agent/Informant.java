/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.pipeline.agent;

import java.util.SortedMap;

import edu.kit.kastel.mcse.ardoco.core.data.DataRepository;
import edu.kit.kastel.mcse.ardoco.core.pipeline.AbstractPipelineStep;

/**
 * Abstract base class for pipeline informants. Informants are components that provide information to agents during pipeline execution.
 */
public abstract class Informant extends AbstractPipelineStep implements Claimant {

    protected Informant(String id, DataRepository dataRepository) {
        super(id, dataRepository);
    }

    @Override
    protected void before() {
        // Nothing by default
    }

    @Override
    protected void after() {
        // Nothing by default
    }

    @Override
    protected void delegateApplyConfigurationToInternalObjects(SortedMap<String, String> additionalConfiguration) {
        // Nothing by default
    }
}
