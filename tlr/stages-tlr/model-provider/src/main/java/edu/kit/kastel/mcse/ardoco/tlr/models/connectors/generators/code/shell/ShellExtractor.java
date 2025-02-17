/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.code.shell;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.CodeModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.code.CodeExtractor;

public final class ShellExtractor extends CodeExtractor {

    private CodeModel extractedModel = null;

    public ShellExtractor(CodeItemRepository codeItemRepository, String path) {
        super(codeItemRepository, path);
    }

    @Override
    public synchronized CodeModel extractModel() {
        if (extractedModel == null) {
            this.extractedModel = parseCode(new File(path));
        }
        return this.extractedModel;
    }

    private CodeModel parseCode(File file) {
        Path startingDir = Paths.get(file.toURI());
        ShellVisitor shellScriptVisitor = new ShellVisitor(fileTypePredictor, codeItemRepository, startingDir);
        // walk all files and run the ShellScriptVisitor
        try {
            Files.walkFileTree(startingDir, shellScriptVisitor);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        return shellScriptVisitor.getCodeModel();
    }
}
