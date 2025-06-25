/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.generators.antlr.extraction.python3;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.tlr.models.antlr4.python3.Python3Lexer;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.python3.Python3ElementExtractor;

class Python3ModuleExtractorTest {
    String sourcePath = "src/test/resources/python/interface/edu/";

    @Test
    void testPyClassModuleExtraction() throws IOException {
        String filePath = sourcePath + "APyClass.py";
        List<Element> modules = extractModuleElement(filePath);
        // Assertions
        Assertions.assertEquals(1, modules.size());
        Assertions.assertEquals("APyClass", modules.get(0).getName());
        Assertions.assertEquals("src/test/resources/python/interface/edu/APyClass.py", modules.get(0).getPath());
        Assertions.assertEquals(Type.PACKAGE, modules.get(0).getParentIdentifier().type());
    }

    @Test
    void testOtherPyAbstractBaseClassModuleExtraction() throws IOException {
        String filePath = sourcePath + "drei/OtherPyAbstractBaseClass.py";
        List<Element> modules = extractModuleElement(filePath);
        // Assertions
        Assertions.assertEquals(1, modules.size());
        Assertions.assertEquals("OtherPyAbstractBaseClass", modules.get(0).getName());
        Assertions.assertEquals("src/test/resources/python/interface/edu/drei/OtherPyAbstractBaseClass.py", modules.get(0).getPath());
        Assertions.assertEquals(Type.PACKAGE, modules.get(0).getParentIdentifier().type());
    }

    private List<Element> extractModuleElement(String filePath) throws IOException {
        Python3ElementExtractor extractor = new Python3ElementExtractor();
        Path path = Path.of(filePath);
        Python3Lexer lexer = new Python3Lexer(CharStreams.fromPath(path));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        extractor.extractElements(tokenStream);
        return extractor.getElements().getModules();
    }
}
