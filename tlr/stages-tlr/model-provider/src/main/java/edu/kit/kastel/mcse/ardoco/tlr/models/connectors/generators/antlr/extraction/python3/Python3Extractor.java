/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.python3;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.ProgrammingLanguage;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.ANTLRExtractor;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.python3.Python3ElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.python3.Python3ModelMapper;

/**
 * Sets up the ElementExtractor and ModelMapper for the Python3 language.
 */
public class Python3Extractor extends ANTLRExtractor {

    public Python3Extractor(CodeItemRepository repository, String path) {
        super(repository, path, ProgrammingLanguage.PYTHON3);
        Python3ElementStorageRegistry elementManager = new Python3ElementStorageRegistry();
        this.mapper = new Python3ModelMapper(repository, elementManager);
        this.elementExtractor = new Python3ElementExtractor(elementManager);
    }

}
