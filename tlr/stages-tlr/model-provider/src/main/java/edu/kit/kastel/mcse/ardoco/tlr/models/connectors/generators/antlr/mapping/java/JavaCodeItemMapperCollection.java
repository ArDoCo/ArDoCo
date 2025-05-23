/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.java;

import java.util.List;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.java.JavaElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.CodeItemMapperCollection;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.java.mappers.ClassMapper;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.java.mappers.CompilationUnitMapper;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.java.mappers.FunctionMapper;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.java.mappers.InterfaceMapper;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.java.mappers.PackageMapper;

/**
 * Defines a collection of CodeItem mappers that can be used to build CodeItems from extracted Java Elements.
 */
public class JavaCodeItemMapperCollection extends CodeItemMapperCollection {

    public JavaCodeItemMapperCollection(CodeItemRepository repository, JavaElementStorageRegistry elementRegistry) {
        super();
        this.mappers = List.of(new FunctionMapper(repository, this, elementRegistry), new ClassMapper(repository, this, elementRegistry), new InterfaceMapper(
                repository, this, elementRegistry), new CompilationUnitMapper(repository, this, elementRegistry), new PackageMapper(repository, this,
                        elementRegistry));
    }

}
