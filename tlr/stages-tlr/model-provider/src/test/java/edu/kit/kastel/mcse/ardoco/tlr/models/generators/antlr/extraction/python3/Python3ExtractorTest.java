/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.generators.antlr.extraction.python3;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.python3.Python3Extractor;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.python3.Python3ElementStorageRegistry;

class Python3ExtractorTest {

    @Test
    void executePython3ExtractorForMinimalDirectoryTest() throws IOException {
        String sourcePath = "src/test/resources/python/interface/edu/";
        Python3Extractor python3Extractor = buildPython3Extractor(sourcePath);
        python3Extractor.extractModel();
        Python3ElementStorageRegistry manager = (Python3ElementStorageRegistry) python3Extractor.getElementExtractor().getElements();

        // Assertions
        Assertions.assertEquals(13, manager.getVariables().size());
        Assertions.assertEquals(17, manager.getFunctions().size());
        Assertions.assertEquals(10, manager.getClasses().size());
        Assertions.assertEquals(8, manager.getModules().size());
    }

    private Python3Extractor buildPython3Extractor(String sourcePath) {
        CodeItemRepository repository = new CodeItemRepository();
        return new Python3Extractor(repository, sourcePath);
    }
}
