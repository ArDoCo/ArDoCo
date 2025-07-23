/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.data;

import java.io.Serial;

import org.eclipse.collections.api.map.sorted.ImmutableSortedMap;

import edu.kit.kastel.mcse.ardoco.core.configuration.AbstractConfigurable;

/**
 * Abstract base class for states that can be saved to the {@link DataRepository} as {@link PipelineStepData}.
 */
public abstract class AbstractState extends AbstractConfigurable implements PipelineStepData {

    @Serial
    private static final long serialVersionUID = -3318799425973820663L;

    @Override
    protected void delegateApplyConfigurationToInternalObjects(ImmutableSortedMap<String, String> map) {
        // empty
    }
}
