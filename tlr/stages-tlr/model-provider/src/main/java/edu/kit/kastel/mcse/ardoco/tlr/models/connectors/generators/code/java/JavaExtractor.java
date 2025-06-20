/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.code.java;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.CodeModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.code.CodeExtractor;

/**
 * Extractor for Java code. Extracts a CMTL instance.
 */
@Deterministic
public final class JavaExtractor extends CodeExtractor {

    private static final Logger logger = LoggerFactory.getLogger(JavaExtractor.class);

    private CodeModel codeModel;

    /**
     * Creates a new JavaExtractor instance.
     *
     * @param codeItemRepository the code item repository
     * @param path               the path to the code
     * @param metamodelToExtract the metamodel to extract
     */
    public JavaExtractor(CodeItemRepository codeItemRepository, String path, Metamodel metamodelToExtract) {
        super(codeItemRepository, path, metamodelToExtract);
    }

    /**
     * Extracts a code model, i.e. a CMTL instance, from Java code.
     *
     * @return the extracted code model
     */
    @Override
    public synchronized CodeModel extractModel() {
        if (codeModel == null) {
            Path directoryPath = Path.of(path);
            SortedMap<String, CompilationUnit> compUnitMap = parseDirectory(directoryPath);
            JavaModel javaModel = new JavaModel(codeItemRepository, compUnitMap);
            this.codeModel = javaModel.getCodeModel(metamodelToExtract);
        }
        return this.codeModel;
    }

    private static SortedMap<String, CompilationUnit> parseDirectory(Path dir) {
        ASTParser parser = getJavaParser();
        final String[] sources = getJavaFiles(dir);
        final String[] encodings = new String[sources.length];
        Arrays.fill(encodings, StandardCharsets.UTF_8.toString());
        final SortedMap<String, CompilationUnit> compilationUnits = new TreeMap<>();
        parser.setEnvironment(new String[0], new String[0], new String[0], false);
        parser.createASTs(sources, encodings, new String[0], new FileASTRequestor() {
            @Override
            public void acceptAST(final String sourceFilePath, final CompilationUnit ast) {
                URI sourceFileUri = Path.of(sourceFilePath).toUri();
                String relativeSourceFilePath = dir.toUri().relativize(sourceFileUri).toString();
                compilationUnits.put(relativeSourceFilePath, ast);
            }
        }, new NullProgressMonitor());
        return compilationUnits;
    }

    private static ASTParser getJavaParser() {
        String javaCoreVersion = JavaCore.latestSupportedJavaVersion();
        final ASTParser parser = ASTParser.newParser(AST.getJLSLatest());
        parser.setResolveBindings(true);
        parser.setStatementsRecovery(true);
        parser.setCompilerOptions(Map.of(JavaCore.COMPILER_SOURCE, javaCoreVersion, JavaCore.COMPILER_COMPLIANCE, javaCoreVersion,
                JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, javaCoreVersion));
        return parser;
    }

    private static String[] getJavaFiles(Path dir) {
        var predictions = fileTypePredictor.predictFileTypesFromFolderRecursively(dir);

        var javaFiles = predictions.entrySet()
                .stream()
                .filter(entry -> entry.getValue().label().equals("java"))
                .map(Map.Entry::getKey)
                .map(Path::toAbsolutePath)
                .map(Path::normalize)
                .map(Path::toString)
                .toArray(String[]::new);

        logger.debug("# Java files found: {}", javaFiles.length);
        return javaFiles;
    }
}
