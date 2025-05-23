/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.java.mappers;

import java.util.List;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.ProgrammingLanguage;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ElementIdentifier;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.java.JavaElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.AbstractCodeItemMapper;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.java.JavaCodeItemMapperCollection;

/**
 * Defines how a CodeItemMapper for Java elements should be implemented.
 */
public abstract class AbstractJavaCodeItemMapper extends AbstractCodeItemMapper {
    protected final JavaElementStorageRegistry elementRegistry;
    private static final ProgrammingLanguage programmingLanguage = ProgrammingLanguage.JAVA;

    protected AbstractJavaCodeItemMapper(CodeItemRepository codeItemRepository, JavaCodeItemMapperCollection javaCodeItemMappers,
            JavaElementStorageRegistry elementRegistry) {
        super(codeItemRepository, javaCodeItemMappers, programmingLanguage);
        this.elementRegistry = elementRegistry;
    }

    @Override
    protected List<Element> getContentOfIdentifier(ElementIdentifier identifier) {
        return elementRegistry.getContentOfIdentifier(identifier);
    }

}
