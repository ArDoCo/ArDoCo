/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl;

import java.util.List;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture.ArchitectureItem;

/**
 * Represents an architecture model that is an AMTL instance.
 */
public abstract sealed class ArchitectureModel extends Model permits ArchitectureComponentModel, ArchitectureModelWithComponentsAndInterfaces {

    /**
     * Returns the content of the architecture model.
     *
     * @return list of architecture items
     */
    @Override
    public abstract List<? extends ArchitectureItem> getContent();

    /**
     * Returns the endpoints of this model.
     *
     * @return list of architecture items
     */
    @Override
    public abstract List<? extends ArchitectureItem> getEndpoints();
}
