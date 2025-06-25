/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.generators.antlr.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.CodeModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.python3.Python3Extractor;

class Python3ModelMapperTest {

    @Test
    void testPython3ModelMapper() {
        CodeItemRepository repository = new CodeItemRepository();
        Python3Extractor extractor = new Python3Extractor(repository, "src/test/resources/python/interface/edu/");
        CodeModel codeModel = extractor.extractModel();

        // More Detailed Assertions
        Assertions.assertEquals(codeModel.getAllPackages().size(), 3);

    }

}
