/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.cpp;

import java.util.List;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.cpp.CppElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.CodeItemMapperCollection;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.cpp.mappers.ClassMapper;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.cpp.mappers.FileMapper;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.cpp.mappers.FunctionMapper;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.cpp.mappers.NamespaceMapper;

/**
 * Defines a collection of CodeItem mappers that can be used to build CodeItems from extracted C++ Elements.
 */
public class CppCodeItemMapperCollection extends CodeItemMapperCollection {

    public CppCodeItemMapperCollection(CodeItemRepository repository, CppElementStorageRegistry elementRegistry) {
        super();
        this.mappers = List.of(new FunctionMapper(repository, this, elementRegistry), new ClassMapper(repository, this, elementRegistry), new NamespaceMapper(
                repository, this, elementRegistry), new FileMapper(repository, this, elementRegistry));
    }
}
