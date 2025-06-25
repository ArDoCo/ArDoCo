/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.python3.mappers;

import java.util.SortedSet;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.ClassUnit;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItem;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ClassElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ElementIdentifier;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.python3.Python3ElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.CodeItemMapperCollection;

/**
 * Responsible for mapping a Python3 ClassElement to a ClassUnit.
 */
public class ClassMapper extends AbstractPython3CodeItemMapper {

    public ClassMapper(CodeItemRepository repository, CodeItemMapperCollection pythonCodeItemMappers, Python3ElementStorageRegistry elementRegistry) {
        super(repository, pythonCodeItemMappers, elementRegistry);
    }

    @Override
    public CodeItem buildCodeItem(Element element) {
        ElementIdentifier comparable = new ElementIdentifier(element.getName(), element.getPath(), Type.CLASS);
        return buildClassUnit(comparable);
    }

    @Override
    public boolean supports(Element element) {
        return elementRegistry.isClassElement(element);
    }

    private ClassUnit buildClassUnit(ElementIdentifier identifier) {
        ClassElement classElement = elementRegistry.getClass(identifier);
        SortedSet<CodeItem> content = buildContent(identifier);

        ClassUnit classUnit = new ClassUnit(codeItemRepository, classElement.getName(), content);
        classUnit.setComment(classElement.getComment());
        return classUnit;
    }

}
