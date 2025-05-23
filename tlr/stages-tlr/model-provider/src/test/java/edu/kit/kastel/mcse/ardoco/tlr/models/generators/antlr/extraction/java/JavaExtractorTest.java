/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.generators.antlr.extraction.java;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.java.JavaExtractor;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.java.JavaElementStorageRegistry;

class JavaExtractorTest {

    @Test
    void executeJavaExtractorForMinimalDirectoryTest() {
        String sourcePath = "src/test/resources/interface/edu/";
        JavaExtractor javaExtractor = buildJavaExtractor(sourcePath);
        javaExtractor.extractModel();
        JavaElementStorageRegistry manager = (JavaElementStorageRegistry) javaExtractor.getElementExtractor().getElements();

        // Assertions
        Assertions.assertEquals(3, manager.getVariables().size());
        Assertions.assertEquals(1, manager.getFunctions().size());
        Assertions.assertEquals(6, manager.getClasses().size());
        Assertions.assertEquals(4, manager.getInterfaces().size());
        Assertions.assertEquals(7, manager.getCompilationUnits().size());
        Assertions.assertEquals(3, manager.getPackages().size());
    }

    private JavaExtractor buildJavaExtractor(String sourcePath) {
        CodeItemRepository repository = new CodeItemRepository();
        return new JavaExtractor(repository, sourcePath);
    }

}
