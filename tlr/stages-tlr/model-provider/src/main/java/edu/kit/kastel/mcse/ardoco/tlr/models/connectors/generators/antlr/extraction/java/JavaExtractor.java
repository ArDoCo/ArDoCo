/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.java;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.ProgrammingLanguage;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.ANTLRExtractor;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.java.JavaElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.java.JavaModelMapper;

/**
 * Sets up the ElementExtractor and ModelMapper for the Java language.
 */
public class JavaExtractor extends ANTLRExtractor {

    public JavaExtractor(CodeItemRepository repository, String path) {
        super(repository, path, ProgrammingLanguage.JAVA);
        JavaElementStorageRegistry elementManager = new JavaElementStorageRegistry();
        this.mapper = new JavaModelMapper(repository, elementManager);
        this.elementExtractor = new JavaElementExtractor(elementManager);
    }
}
