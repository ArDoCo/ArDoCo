/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.data.PipelineStepData;

/**
 * State interface for managing multiple inconsistency states by metamodel.
 */
public interface InconsistencyStates extends PipelineStepData {
    /**
     * The ID for this state.
     */
    String ID = "InconsistencyStates";

    /**
     * Returns the inconsistency state for the given metamodel.
     *
     * @param metamodel the metamodel
     * @return the inconsistency state
     */
    InconsistencyState getInconsistencyState(Metamodel metamodel);
}
