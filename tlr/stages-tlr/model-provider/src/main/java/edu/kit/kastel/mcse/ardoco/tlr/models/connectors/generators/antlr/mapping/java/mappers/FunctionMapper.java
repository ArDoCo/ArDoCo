/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.java.mappers;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItem;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.ControlElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ElementIdentifier;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.java.JavaElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.java.JavaCodeItemMapperCollection;

/**
 * Responsible for mapping a Java function element to a control element.
 */
public class FunctionMapper extends AbstractJavaCodeItemMapper {

    public FunctionMapper(CodeItemRepository codeItemRepository, JavaCodeItemMapperCollection javaCodeItemMappers, JavaElementStorageRegistry elementRegistry) {
        super(codeItemRepository, javaCodeItemMappers, elementRegistry);
    }

    @Override
    public boolean supports(Element element) {
        return elementRegistry.isFunctionElement(element);
    }

    @Override
    public CodeItem buildCodeItem(Element element) {
        ElementIdentifier comparable = new ElementIdentifier(element.getName(), element.getPath(), Type.FUNCTION);
        return buildControlElement(comparable);
    }

    private CodeItem buildControlElement(ElementIdentifier identifier) {
        Element function = this.elementRegistry.getFunction(identifier);

        ControlElement controlElement = new ControlElement(codeItemRepository, function.getName());
        controlElement.setComment(function.getComment());
        return controlElement;
    }

}
