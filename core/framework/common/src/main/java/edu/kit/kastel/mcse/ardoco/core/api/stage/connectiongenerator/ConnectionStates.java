/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.stage.connectiongenerator;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.data.PipelineStepData;

/**
 * State interface for managing multiple connection states by metamodel.
 */
public interface ConnectionStates extends PipelineStepData {
    /**
     * The ID for this state.
     */
    String ID = "ConnectionStates";

    /**
     * Returns the connection state for the given metamodel.
     *
     * @param mm the metamodel
     * @return the connection state
     */
    ConnectionState getConnectionState(Metamodel mm);
}
