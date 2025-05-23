/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.cpp;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.ProgrammingLanguage;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.ANTLRExtractor;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.cpp.CppElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.cpp.CppModelMapper;

/**
 * Sets up the ElementExtractor and ModelMapper for the C++ language.
 */
public class CppExtractor extends ANTLRExtractor {

    public CppExtractor(CodeItemRepository repository, String path) {
        super(repository, path, ProgrammingLanguage.CPP);
        CppElementStorageRegistry elementManager = new CppElementStorageRegistry();
        this.mapper = new CppModelMapper(repository, elementManager);
        this.elementExtractor = new CppElementExtractor(elementManager);
    }
}
