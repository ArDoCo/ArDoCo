/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.python3.mappers;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItem;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.ControlElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ElementIdentifier;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.python3.Python3ElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.python3.Python3CodeItemMapperCollection;

/**
 * A mapper for Python3 functions.
 */
public class FunctionMapper extends AbstractPython3CodeItemMapper {

    public FunctionMapper(CodeItemRepository codeItemRepository, Python3CodeItemMapperCollection pythonCodeItemMappers,
            Python3ElementStorageRegistry elementRegistry) {
        super(codeItemRepository, pythonCodeItemMappers, elementRegistry);
    }

    @Override
    public CodeItem buildCodeItem(Element element) {
        ElementIdentifier comparable = new ElementIdentifier(element.getName(), element.getPath(), Type.FUNCTION);
        return buildControlElement(comparable);
    }

    @Override
    public boolean supports(Element element) {
        return elementRegistry.isFunctionElement(element);
    }

    private CodeItem buildControlElement(ElementIdentifier identifier) {
        Element function = this.elementRegistry.getFunction(identifier);

        ControlElement controlElement = new ControlElement(codeItemRepository, function.getName());
        controlElement.setComment(function.getComment());
        return controlElement;
    }

}
