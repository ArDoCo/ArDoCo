/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.generators.pcm;

import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.ArchitectureModel;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.architecture.pcm.PcmExtractor;
import edu.kit.kastel.mcse.ardoco.tlr.models.generators.ArchitectureExtractorTest;

class PcmExtractorTest extends ArchitectureExtractorTest {

    @Test
    void extractorTest() {
        var pcmExtractor = new PcmExtractor("src/test/resources/mediastore/architecture/pcm/ms.repository", Metamodel.ARCHITECTURE_ONLY_COMPONENTS);
        ArchitectureModel model = pcmExtractor.extractModel();
        checkModel(model);
    }

}
