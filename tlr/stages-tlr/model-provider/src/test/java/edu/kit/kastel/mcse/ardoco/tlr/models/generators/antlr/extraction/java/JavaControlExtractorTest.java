/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.generators.antlr.extraction.java;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.tlr.models.antlr4.java.JavaLexer;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.java.JavaElementExtractor;

class JavaControlExtractorTest {
    private final String sourcePath = "src/test/resources/interface/edu/";

    @Test
    void controlExtractorAClassTest() throws IOException {
        String filePath = sourcePath + "AClass.java";
        List<Element> controls = extractBasicElementsFromFile(filePath);
        Assertions.assertEquals(1, controls.size());
        Assertions.assertEquals("aMethod", controls.get(0).getName());
        Assertions.assertEquals("AClass", controls.get(0).getParentIdentifier().name());
    }

    @Test
    void controlExtractorAnEnum() throws IOException {
        String filePath = sourcePath + "AnEnum.java";
        List<Element> controls = extractBasicElementsFromFile(filePath);
        Assertions.assertTrue(controls.isEmpty());
    }

    @Test
    void controlExtractorAnInterface() throws IOException {
        String filePath = sourcePath + "AnInterface.java";
        List<Element> controls = extractBasicElementsFromFile(filePath);
        Assertions.assertTrue(controls.isEmpty());
    }

    @Test
    void controlExtractorSuperclass() throws IOException {
        String filePath = sourcePath + "Superclass.java";
        List<Element> controls = extractBasicElementsFromFile(filePath);
        Assertions.assertTrue(controls.isEmpty());
    }

    @Test
    void controlExtractorExtendedInterface() throws IOException {
        String filePath = sourcePath + "ExtendedInterface.java";
        List<Element> controls = extractBasicElementsFromFile(filePath);
        Assertions.assertTrue(controls.isEmpty());
    }

    @Test
    void controlExtractorOtherInterfaceZwei() throws IOException {
        String filePath = sourcePath + "zwei/OtherInterface.java";
        List<Element> controls = extractBasicElementsFromFile(filePath);
        Assertions.assertTrue(controls.isEmpty());
    }

    @Test
    void controlExtractorOtherInterfaceDrei() throws IOException {
        String filePath = sourcePath + "drei/OtherInterface.java";
        List<Element> controls = extractBasicElementsFromFile(filePath);
        Assertions.assertTrue(controls.isEmpty());
    }

    private List<Element> extractBasicElementsFromFile(String filePath) throws IOException {
        JavaElementExtractor extractor = new JavaElementExtractor();
        Path path = Path.of(filePath);
        JavaLexer lexer = new JavaLexer(CharStreams.fromPath(path));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        extractor.extractElements(tokenStream);
        return extractor.getElements().getFunctions();
    }

}
