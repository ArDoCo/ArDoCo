/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.java.mappers;

import java.util.SortedSet;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.ClassUnit;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItem;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ElementIdentifier;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.java.JavaClassElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.java.JavaElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.java.JavaCodeItemMapperCollection;

/**
 * Responsible for mapping a Java ClassElement to a ClassUnit.
 */
public class ClassMapper extends AbstractJavaCodeItemMapper {

    public ClassMapper(CodeItemRepository codeItemRepository, JavaCodeItemMapperCollection javaCodeItemMappers, JavaElementStorageRegistry elementRegistry) {
        super(codeItemRepository, javaCodeItemMappers, elementRegistry);
    }

    @Override
    public boolean supports(Element element) {
        return this.elementRegistry.isClassElement(element);
    }

    @Override
    public CodeItem buildCodeItem(Element element) {
        ElementIdentifier comparable = new ElementIdentifier(element.getName(), element.getPath(), Type.CLASS);
        return buildClassUnit(comparable);
    }

    private ClassUnit buildClassUnit(ElementIdentifier identifier) {
        JavaClassElement classElement = elementRegistry.getClass(identifier);
        SortedSet<CodeItem> content = buildContent(identifier);

        ClassUnit classUnit = new ClassUnit(codeItemRepository, classElement.getName(), content);
        classUnit.setComment(classElement.getComment());
        return classUnit;
    }

}
