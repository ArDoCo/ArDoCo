/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.cpp.mappers;

import java.util.SortedSet;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.ClassUnit;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItem;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ClassElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ElementIdentifier;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.cpp.CppElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.cpp.CppCodeItemMapperCollection;

/**
 * Responsible for mapping a C++ ClassElement to a ClassUnit.
 */
public class ClassMapper extends AbstractCppCodeItemMapper {

    public ClassMapper(CodeItemRepository codeItemRepository, CppCodeItemMapperCollection cppCodeItemMappers, CppElementStorageRegistry elementRegistry) {
        super(codeItemRepository, cppCodeItemMappers, elementRegistry);
    }

    @Override
    public CodeItem buildCodeItem(Element element) {
        ElementIdentifier comparable = new ElementIdentifier(element.getName(), element.getPath(), Type.CLASS);
        return buildClassUnit(comparable);
    }

    @Override
    public boolean supports(Element element) {
        return this.elementRegistry.isClassElement(element);
    }

    private CodeItem buildClassUnit(ElementIdentifier identifier) {
        ClassElement classElement = this.elementRegistry.getClass(identifier);
        SortedSet<CodeItem> content = buildContent(identifier);

        ClassUnit classUnit = new ClassUnit(this.codeItemRepository, classElement.getName(), content);
        classUnit.setComment(classElement.getComment());
        return classUnit;
    }

}
