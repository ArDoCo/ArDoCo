/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.architecture;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.ArchitectureModel;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.Extractor;

public abstract class ArchitectureExtractor extends Extractor {

    protected ArchitectureExtractor(String path, Metamodel metamodelToExtract) {
        super(path, metamodelToExtract);
    }

    @Override
    public abstract ArchitectureModel extractModel();

}
