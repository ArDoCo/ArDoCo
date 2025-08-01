/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.generators.antlr.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.core.api.models.CodeModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.java.JavaExtractor;

class JavaModelMapperTest {

    @Test
    void testJavaModelMapper() {
        CodeItemRepository repository = new CodeItemRepository();
        JavaExtractor extractor = new JavaExtractor(repository, "src/test/resources/interface/edu/");
        CodeModel codeModel = extractor.extractModel();

        // Assertions
        Assertions.assertNotNull(codeModel);
        Assertions.assertEquals(7, codeModel.getEndpoints().size());

        // More Detailed Assertions
        Assertions.assertEquals(3, codeModel.getAllPackages().size());

    }

}
