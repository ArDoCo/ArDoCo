/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.cpp.mappers;

import java.util.SortedSet;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeAssembly;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItem;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ElementIdentifier;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.cpp.CppElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.cpp.CppCodeItemMapperCollection;

/**
 * Responsible for mapping a C++ File to a CodeAssembly.
 */
public class FileMapper extends AbstractCppCodeItemMapper {

    public FileMapper(CodeItemRepository codeItemRepository, CppCodeItemMapperCollection cppCodeItemMappers, CppElementStorageRegistry elementRegistry) {
        super(codeItemRepository, cppCodeItemMappers, elementRegistry);
    }

    @Override
    public CodeItem buildCodeItem(Element element) {
        ElementIdentifier comparable = new ElementIdentifier(element.getName(), element.getPath(), Type.FILE);
        return buildFileCodeAssembly(comparable);
    }

    @Override
    public boolean supports(Element element) {
        return this.elementRegistry.isFileElement(element);
    }

    private CodeItem buildFileCodeAssembly(ElementIdentifier identifier) {
        Element file = this.elementRegistry.getFile(identifier);
        SortedSet<CodeItem> content = buildContent(identifier);
        CodeAssembly codeAssembly = new CodeAssembly(this.codeItemRepository, file.getName(), content, this.language.name());
        return codeAssembly;
    }
}
