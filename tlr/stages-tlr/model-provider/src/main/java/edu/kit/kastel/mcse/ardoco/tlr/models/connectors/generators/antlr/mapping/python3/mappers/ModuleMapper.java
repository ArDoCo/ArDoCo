/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.python3.mappers;

import java.util.SortedSet;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeAssembly;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItem;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ElementIdentifier;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.python3.Python3ElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.CodeItemMapperCollection;

/**
 * Responsible for mapping a Python3 ModuleElement to a CodeAssembly.
 */
public class ModuleMapper extends AbstractPython3CodeItemMapper {

    public ModuleMapper(CodeItemRepository repository, CodeItemMapperCollection pythonCodeItemMappers, Python3ElementStorageRegistry elementRegistry) {
        super(repository, pythonCodeItemMappers, elementRegistry);
    }

    @Override
    public boolean supports(Element element) {
        return elementRegistry.isModuleElement(element);
    }

    @Override
    public CodeItem buildCodeItem(Element element) {
        ElementIdentifier comparable = new ElementIdentifier(element.getName(), element.getPath(), Type.MODULE);
        return buildCodeAssembly(comparable);
    }

    private CodeAssembly buildCodeAssembly(ElementIdentifier identifier) {
        Element module = elementRegistry.getModule(identifier);
        SortedSet<CodeItem> content = buildContent(identifier);
        CodeAssembly codeAssembly = new CodeAssembly(codeItemRepository, module.getName(), content, this.language.name());
        codeAssembly.setComment(module.getComment());
        return codeAssembly;
    }

}
