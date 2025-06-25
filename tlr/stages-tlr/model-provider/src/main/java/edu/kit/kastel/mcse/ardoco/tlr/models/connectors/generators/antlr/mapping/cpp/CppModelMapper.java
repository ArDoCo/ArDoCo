/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.cpp;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.cpp.CppElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.ModelMapper;

/**
 * Responsible for mapping extracted elements from C++ to the Arcotl model.
 * Defines the CodeItemMapperCollection and elementRegistry to be used.
 */
public class CppModelMapper extends ModelMapper {

    public CppModelMapper(CodeItemRepository codeItemRepository, CppElementStorageRegistry elementRegistry) {
        super(codeItemRepository, new CppCodeItemMapperCollection(codeItemRepository, elementRegistry), elementRegistry);
    }
}
