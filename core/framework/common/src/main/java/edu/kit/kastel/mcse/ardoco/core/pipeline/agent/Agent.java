/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.pipeline.agent;

/**
 * An Agent is a {@link Claimant} with an ID.
 */
public interface Agent extends Claimant {

    /**
     * Returns the ID of the agent.
     *
     * @return the ID of the agent
     */
    String getId();
}
