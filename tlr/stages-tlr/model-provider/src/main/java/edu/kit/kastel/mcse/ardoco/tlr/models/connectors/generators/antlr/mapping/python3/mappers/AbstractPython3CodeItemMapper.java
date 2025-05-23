/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.python3.mappers;

import java.util.List;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.ProgrammingLanguage;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ElementIdentifier;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.python3.Python3ElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.AbstractCodeItemMapper;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.CodeItemMapperCollection;

/**
 * A collection of CodeItem mappers that can be used to build CodeItems from extracted Python 3 Elements.
 */
public abstract class AbstractPython3CodeItemMapper extends AbstractCodeItemMapper {
    protected final Python3ElementStorageRegistry elementRegistry;
    private static final ProgrammingLanguage programmingLanguage = ProgrammingLanguage.PYTHON3;

    protected AbstractPython3CodeItemMapper(CodeItemRepository repository, CodeItemMapperCollection pythonCodeItemMappers,
            Python3ElementStorageRegistry elementRegistry) {
        super(repository, pythonCodeItemMappers, programmingLanguage);
        this.elementRegistry = elementRegistry;
    }

    @Override
    protected List<Element> getContentOfIdentifier(ElementIdentifier identifier) {
        return elementRegistry.getContentOfIdentifier(identifier);
    }

}
