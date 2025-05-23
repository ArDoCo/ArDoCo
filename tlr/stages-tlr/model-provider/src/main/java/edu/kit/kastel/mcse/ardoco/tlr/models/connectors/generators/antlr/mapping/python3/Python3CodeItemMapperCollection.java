/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.python3;

import java.util.List;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.python3.Python3ElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.CodeItemMapperCollection;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.python3.mappers.*;

/**
 * Responsible for defining the CodeItemMapperCollection to be used for mapping Python3 elements.
 */
public class Python3CodeItemMapperCollection extends CodeItemMapperCollection {

    public Python3CodeItemMapperCollection(CodeItemRepository repository, Python3ElementStorageRegistry elementRegistry) {
        super();
        this.mappers = List.of(new FunctionMapper(repository, this, elementRegistry), new ClassMapper(repository, this, elementRegistry), new ModuleMapper(
                repository, this, elementRegistry), new PackageMapper(repository, this, elementRegistry));
    }

}
