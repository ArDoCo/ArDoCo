/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.java;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.java.JavaElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.ModelMapper;

/**
 * Responsible for defining the CodeItemMapperCollection and elementRegistry to be used for mapping Java elements.
 */
public class JavaModelMapper extends ModelMapper {

    public JavaModelMapper(CodeItemRepository codeItemRepository, JavaElementStorageRegistry elementRegistry) {
        super(codeItemRepository, new JavaCodeItemMapperCollection(codeItemRepository, elementRegistry), elementRegistry);
    }
}
