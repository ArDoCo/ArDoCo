/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.CodeModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.ProgrammingLanguage;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.mapping.ModelMapper;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.code.CodeExtractor;

/**
 * Is responsible for the process of extracting code elements from a directory
 * and mapping them to a valid code model using ANTLR.
 * The extraction process and the mapping process are separated to allow for a
 * more flexible implementation.
 * The extraction process is done by the ElementExtractor and the mapping
 * process is done by the ModelMapper.
 */
public class ANTLRExtractor extends CodeExtractor {
    private final ProgrammingLanguage language;
    protected ModelMapper mapper; // Needs to be initialized in the constructor of implementing subclasses
    protected ElementExtractor elementExtractor; // Needs to be initialized in the constructor of implementing subclasses
    private boolean contentExtracted;

    protected ANTLRExtractor(CodeItemRepository codeItemRepository, String path, ProgrammingLanguage language) {
        super(codeItemRepository, path);
        this.language = language;
        this.contentExtracted = false;
    }

    @Override
    public synchronized CodeModel extractModel() {
        if (!contentExtracted) {
            extractContent();
            mapToCodeModel();
            contentExtracted = true;
        }
        return this.mapper.getCodeModel();
    }

    public ElementExtractor getElementExtractor() {
        return elementExtractor;
    }

    public ProgrammingLanguage getLanguage() {
        return language;
    }

    public ModelMapper getMapper() {
        return mapper;
    }

    private void mapToCodeModel() {
        this.mapper.mapToCodeModel();
    }

    private void extractContent() {
        this.elementExtractor.extract(this.path);
    }
}
