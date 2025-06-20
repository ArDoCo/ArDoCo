/* Licensed under MIT 2024-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.agents;

import java.io.File;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.models.ModelFormat;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.Extractor;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.architecture.pcm.PcmExtractor;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.architecture.uml.UmlExtractor;

public record ArchitectureConfiguration(File architectureFile, ModelFormat type, Metamodel representation) {
    public ArchitectureConfiguration {
        if (architectureFile == null || type == null) {
            throw new IllegalArgumentException("Architecture file and type must not be null");
        }
        if (!architectureFile.exists() || !architectureFile.isFile()) {
            throw new IllegalArgumentException("Architecture file must exist and be a file");
        }
    }

    public Extractor extractor() {
        return switch (this.type) {
            case PCM -> new PcmExtractor(this.architectureFile.getAbsolutePath(), this.representation);
            case UML -> new UmlExtractor(this.architectureFile.getAbsolutePath(), this.representation);
            case RAW -> throw new IllegalArgumentException("Raw model is not supported for this project.");
            case ACM -> throw new IllegalArgumentException("ACM model is not supported for this project.");
        };
    }
}
