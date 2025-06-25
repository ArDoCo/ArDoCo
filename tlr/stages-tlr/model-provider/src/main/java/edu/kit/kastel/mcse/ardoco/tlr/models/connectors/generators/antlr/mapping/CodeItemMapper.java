/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItem;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;

/**
 * A CodeItemMapper is responsible for mapping an Element to a CodeItem.
 * It supports the mapping of a specific type of Element.
 */
public interface CodeItemMapper {

    public CodeItem buildCodeItem(Element element);

    public boolean supports(Element element);

}
