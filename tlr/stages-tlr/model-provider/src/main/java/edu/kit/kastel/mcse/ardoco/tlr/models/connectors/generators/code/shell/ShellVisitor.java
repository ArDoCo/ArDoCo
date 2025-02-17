/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.code.shell;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.CodeModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeCompilationUnit;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItem;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.ProgrammingLanguages;
import edu.kit.kastel.mcse.ardoco.magika.FileTypePredictor;

public class ShellVisitor implements FileVisitor<Path> {
    private static final Logger logger = LoggerFactory.getLogger(ShellVisitor.class);

    private final Path startingDir;
    private final SortedSet<CodeItem> codeEndpoints;
    private final CodeItemRepository codeItemRepository;
    private final FileTypePredictor fileTypePredictor;

    public ShellVisitor(FileTypePredictor fileTypePredictor, CodeItemRepository codeItemRepository, Path startingDir) {
        this.fileTypePredictor = fileTypePredictor;
        this.codeItemRepository = codeItemRepository;
        this.startingDir = startingDir;
        codeEndpoints = new TreeSet<>();
    }

    public CodeModel getCodeModel() {
        return new CodeModel(codeItemRepository, codeEndpoints);
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        String fileName = path.getFileName().toString();
        String code = "";
        try (FileReader reader = new FileReader(path.toFile())) {
            code = IOUtils.toString(reader);
        } catch (IOException e) {
            logger.warn("Exception when reading file", e);
        }
        if (!isShellFile(code)) {
            return FileVisitResult.CONTINUE;
        }

        String extension = FilenameUtils.getExtension(fileName);
        String fileNameWithoutExtension = FilenameUtils.removeExtension(fileName);
        CodeCompilationUnit sourceFile = extractShellFile(path, fileNameWithoutExtension, extension);
        codeEndpoints.add(sourceFile);
        return FileVisitResult.CONTINUE;
    }

    private CodeCompilationUnit extractShellFile(Path path, String fileNameWithoutExtension, String extension) {
        List<String> pathElements = new ArrayList<>();

        // relativize path
        URI sourceFileUri = path.toUri();
        String relativePathString = startingDir.toUri().relativize(sourceFileUri).toString();
        Path relativePath = Path.of(relativePathString);

        for (int i = 0; i < relativePath.getNameCount() - 1; i++) {
            pathElements.add(relativePath.getName(i).toString());
        }
        return new CodeCompilationUnit(codeItemRepository, fileNameWithoutExtension, new TreeSet<>(), pathElements, extension, ProgrammingLanguages.SHELL);
    }

    private boolean isShellFile(String code) {
        return fileTypePredictor.predictBytes(code.getBytes()).label().equals("shell");
    }
}
