package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.cpp.mappers;

import java.util.List;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ElementIdentifier;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.cpp.CppElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.AbstractCodeItemMapper;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.CodeItemMapperCollection;

/**
 * Defines how a CodeItemMapper for C++ elements should be implemented.
 */
public abstract class AbstractCppCodeItemMapper extends AbstractCodeItemMapper {
    protected final CppElementStorageRegistry elementRegistry;

    protected AbstractCppCodeItemMapper(CodeItemRepository repository, CodeItemMapperCollection cppCodeItemMappers, CppElementStorageRegistry elementRegistry) {
        super(repository, cppCodeItemMappers);
        this.elementRegistry = elementRegistry;
    }

    @Override
    protected List<Element> getContentOfIdentifier(ElementIdentifier identifier) {
        return elementRegistry.getContentOfIdentifier(identifier);
    }
    
}
