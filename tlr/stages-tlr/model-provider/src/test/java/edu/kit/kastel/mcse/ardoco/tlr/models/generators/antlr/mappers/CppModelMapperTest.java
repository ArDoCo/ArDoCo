/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.generators.antlr.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.CodeModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.cpp.CppExtractor;

class CppModelMapperTest {

    @Test
    void testCppModelMapper() {
        CodeItemRepository repository = new CodeItemRepository();
        CppExtractor extractor = new CppExtractor(repository, "src/test/resources/cpp/interface/edu/");
        CodeModel model = extractor.extractModel();

        // Assertions
        Assertions.assertNotNull(model);
        Assertions.assertEquals(model.getAllPackages().size(), 0);
    }

}
