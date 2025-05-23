/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.java.mappers;

import java.util.SortedSet;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItem;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodePackage;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ElementIdentifier;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.PackageElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.java.JavaElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.java.JavaCodeItemMapperCollection;

/**
 * Responsible for mapping a Java PackageElement to a CodePackage.
 */
public class PackageMapper extends AbstractJavaCodeItemMapper {

    public PackageMapper(CodeItemRepository codeItemRepository, JavaCodeItemMapperCollection javaCodeItemMappers, JavaElementStorageRegistry elementRegistry) {
        super(codeItemRepository, javaCodeItemMappers, elementRegistry);
    }

    @Override
    public boolean supports(Element element) {
        return elementRegistry.isPackageElement(element);
    }

    @Override
    public CodeItem buildCodeItem(Element element) {
        ElementIdentifier comparable = new ElementIdentifier(element.getName(), element.getPath(), Type.PACKAGE);
        return buildCodePackage(comparable);
    }

    private CodePackage buildCodePackage(ElementIdentifier identifier) {
        PackageElement packageElement = elementRegistry.getPackage(identifier);
        SortedSet<CodeItem> content = buildContent(identifier);

        CodePackage codePackage = new CodePackage(codeItemRepository, packageElement.getShortName(), content);
        codePackage.setComment(packageElement.getComment());
        return codePackage;
    }
}
