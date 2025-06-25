/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.python3;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.python3.Python3ElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.ModelMapper;

/**
 * Responsible for mapping extracted elements from Python3 to the Arcotl model.
 * Defines the CodeItemMapperCollection and elementRegistry to be used.
 */
public class Python3ModelMapper extends ModelMapper {

    public Python3ModelMapper(CodeItemRepository codeItemRepository, Python3ElementStorageRegistry elementRegistry) {
        super(codeItemRepository, new Python3CodeItemMapperCollection(codeItemRepository, elementRegistry), elementRegistry);
    }
}
