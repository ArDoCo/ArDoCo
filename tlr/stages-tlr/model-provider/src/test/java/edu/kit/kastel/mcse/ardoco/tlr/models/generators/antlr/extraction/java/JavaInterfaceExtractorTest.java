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
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.java.JavaElementExtractor;

class JavaInterfaceExtractorTest {
    private final String sourcePath = "src/test/resources/interface/edu/";

    @Test
    void interfaceExtractorAnInterfaceTest() throws IOException {
        String filePath = sourcePath + "AnInterface.java";
        List<Element> interfaces = extractInterfacesFromFile(filePath);
        Assertions.assertEquals(1, interfaces.size());
        Assertions.assertEquals("AnInterface", interfaces.get(0).getName());
        Assertions.assertEquals("AnInterface", interfaces.get(0).getParentIdentifier().name());
        Assertions.assertEquals(Type.COMPILATIONUNIT, interfaces.get(0).getParentIdentifier().type());
    }

    @Test
    void interfaceExtractorExtendedInterface() throws IOException {
        String filePath = sourcePath + "ExtendedInterface.java";
        List<Element> interfaces = extractInterfacesFromFile(filePath);
        Assertions.assertEquals(1, interfaces.size());
        Assertions.assertEquals("ExtendedInterface", interfaces.get(0).getName());
        Assertions.assertEquals("ExtendedInterface", interfaces.get(0).getParentIdentifier().name());
        Assertions.assertEquals(Type.COMPILATIONUNIT, interfaces.get(0).getParentIdentifier().type());
    }

    @Test
    void interfaceExtractorOtherInterfaceZwei() throws IOException {
        String filePath = sourcePath + "zwei/OtherInterface.java";
        List<Element> interfaces = extractInterfacesFromFile(filePath);
        Assertions.assertEquals(1, interfaces.size());
        Assertions.assertEquals("OtherInterface", interfaces.get(0).getName());
        Assertions.assertEquals("OtherInterface", interfaces.get(0).getParentIdentifier().name());
        Assertions.assertEquals(Type.COMPILATIONUNIT, interfaces.get(0).getParentIdentifier().type());
    }

    @Test
    void interfaceExtractorOtherInterfaceDrei() throws IOException {
        String filePath = sourcePath + "drei/OtherInterface.java";
        List<Element> interfaces = extractInterfacesFromFile(filePath);
        Assertions.assertEquals(1, interfaces.size());
        Assertions.assertEquals("OtherInterface", interfaces.get(0).getName());
        Assertions.assertEquals("OtherInterface", interfaces.get(0).getParentIdentifier().name());
        Assertions.assertEquals(Type.COMPILATIONUNIT, interfaces.get(0).getParentIdentifier().type());
    }

    @Test
    void interfaceExtractorAClass() throws IOException {
        String filePath = sourcePath + "AClass.java";
        List<Element> interfaces = extractInterfacesFromFile(filePath);
        Assertions.assertTrue(interfaces.isEmpty());
    }

    @Test
    void interfaceExtractorSuperclass() throws IOException {
        String filePath = sourcePath + "Superclass.java";
        List<Element> interfaces = extractInterfacesFromFile(filePath);
        Assertions.assertTrue(interfaces.isEmpty());
    }

    @Test
    void interfaceExtractorAnEnum() throws IOException {
        String filePath = sourcePath + "AnEnum.java";
        List<Element> interfaces = extractInterfacesFromFile(filePath);
        Assertions.assertTrue(interfaces.isEmpty());
    }

    private List<Element> extractInterfacesFromFile(String filePath) throws IOException {
        JavaElementExtractor extractor = new JavaElementExtractor();
        Path path = Path.of(filePath);
        JavaLexer lexer = new JavaLexer(CharStreams.fromPath(path));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        extractor.extractElements(tokenStream);
        return extractor.getElements().getInterfaces();

    }

}
