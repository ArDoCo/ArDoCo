/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItem;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.ProgrammingLanguage;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ElementIdentifier;

/**
 * Responsible for mapping extracted elements to the Arcotl model.
 * Can recursively build a CodeItem from an Element and a
 * CodeItemMapperCollection.
 */
public abstract class AbstractCodeItemMapper implements CodeItemMapper {
    protected final CodeItemRepository codeItemRepository;
    protected final CodeItemMapperCollection collection;
    protected final ProgrammingLanguage language;

    protected AbstractCodeItemMapper(CodeItemRepository repository, CodeItemMapperCollection mappers, ProgrammingLanguage language) {
        this.codeItemRepository = repository;
        this.collection = mappers;
        this.language = language;
    }

    @Override
    public abstract CodeItem buildCodeItem(Element element);

    @Override
    public abstract boolean supports(Element element);

    protected SortedSet<CodeItem> buildContent(ElementIdentifier identifier) {
        SortedSet<CodeItem> content = new TreeSet<>();
        List<Element> elements = getContentOfIdentifier(identifier);
        for (Element element : elements) {
            CodeItem codeItem = buildCodeItemFromStrategy(element);
            if (codeItem != null) {
                content.add(codeItem);
            }
        }
        return content;
    }

    protected abstract List<Element> getContentOfIdentifier(ElementIdentifier identifier);

    protected CodeItem buildCodeItemFromStrategy(Element element) {
        return collection.buildCodeItem(element);
    }

}
