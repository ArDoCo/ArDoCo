/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.code;

import static edu.kit.kastel.mcse.ardoco.core.common.JsonHandling.createObjectMapper;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.CodeModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.CodeModelDTO;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.CodeModelWithCompilationUnitsAndPackages;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.CodeModelWithOnlyCompilationUnits;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.magika.FileTypePredictor;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.Extractor;

public abstract class CodeExtractor extends Extractor {
    private static final Logger logger = LoggerFactory.getLogger(CodeExtractor.class);

    protected static final FileTypePredictor fileTypePredictor = new FileTypePredictor();

    private static final String CODE_MODEL_FILE_NAME = "codeModel.acm";
    protected final CodeItemRepository codeItemRepository;

    protected CodeExtractor(CodeItemRepository codeItemRepository, String path, Metamodel metamodelToExtract) {
        super(path, metamodelToExtract);
        this.codeItemRepository = codeItemRepository;
    }

    @Override
    public abstract CodeModel extractModel();

    public void writeOutCodeModel(CodeModel codeModel, File outputFile) {
        ObjectMapper objectMapper = createObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        try {
            objectMapper.writeValue(outputFile, codeModel.createCodeModelDTO());
        } catch (IOException e) {
            logger.warn("An exception occurred when writing the code model.", e);
        }
    }

    /**
     * Writes the code model to the default location, i.e., to the folder of the code with the name "codeModel.acm"
     *
     * @param codeModel the code model to write
     */
    public void writeOutCodeModel(CodeModel codeModel) {
        File file = new File(getCodeModelFileString());
        writeOutCodeModel(codeModel, file);
    }

    public static CodeModel readInCodeModel(File codeModelFile, Metamodel metamodelToExtract) {
        if (codeModelFile != null && codeModelFile.isFile()) {
            logger.info("Reading in existing code model.");
            ObjectMapper objectMapper = createObjectMapper();
            objectMapper.registerModule(new Jdk8Module());
            try {
                CodeModelDTO content = objectMapper.readValue(codeModelFile, CodeModelDTO.class);

                return switch (metamodelToExtract) {
                    case CODE_WITH_COMPILATION_UNITS_AND_PACKAGES -> new CodeModelWithCompilationUnitsAndPackages(content);
                    case CODE_ONLY_COMPILATION_UNITS -> new CodeModelWithOnlyCompilationUnits(content);
                    default -> throw new IllegalStateException("Unexpected value: " + metamodelToExtract);
                };

            } catch (IOException e) {
                logger.warn("An exception occurred when reading the code model.", e);
            }
        }
        return null;
    }

    private String getCodeModelFileString() {
        return path + File.separator + CODE_MODEL_FILE_NAME;
    }

}
