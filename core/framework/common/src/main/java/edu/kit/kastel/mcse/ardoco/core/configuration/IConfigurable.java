/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.configuration;

import java.util.SortedMap;

/**
 * Interface for components that can be configured with additional configuration parameters.
 */
public interface IConfigurable {
    /**
     * Applies the given configuration to this component.
     *
     * @param additionalConfiguration the configuration to apply
     */
    void applyConfiguration(SortedMap<String, String> additionalConfiguration);

    /**
     * Returns the last applied configuration.
     *
     * @return the last applied configuration as an unmodifiable map
     */
    SortedMap<String, String> getLastAppliedConfiguration();
}
