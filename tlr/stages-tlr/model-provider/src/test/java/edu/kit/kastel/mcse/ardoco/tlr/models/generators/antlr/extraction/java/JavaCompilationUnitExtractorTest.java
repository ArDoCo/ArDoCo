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

class JavaCompilationUnitExtractorTest {
    private final String sourcePath = "src/test/resources/interface/edu/";

    @Test
    void compilationUnitAClassTest() throws IOException {
        Element element = compilationUnitExtractorTest(sourcePath + "AClass.java");
        Assertions.assertEquals("AClass", element.getName());
        Assertions.assertEquals("src/test/resources/interface/edu/AClass.java", element.getPath());
        Assertions.assertEquals("edu", element.getParentIdentifier().name());
    }

    @Test
    void compilationUnitAnEnumTest() throws IOException {
        Element element = compilationUnitExtractorTest(sourcePath + "AnEnum.java");
        Assertions.assertEquals("AnEnum", element.getName());
        Assertions.assertEquals("src/test/resources/interface/edu/AnEnum.java", element.getPath());
        Assertions.assertEquals("edu", element.getParentIdentifier().name());
    }

    @Test
    void compilationUnitAnInterfaceTest() throws IOException {
        Element element = compilationUnitExtractorTest(sourcePath + "AnInterface.java");
        Assertions.assertEquals("AnInterface", element.getName());
        Assertions.assertEquals("src/test/resources/interface/edu/AnInterface.java", element.getPath());
        Assertions.assertEquals("edu", element.getParentIdentifier().name());
    }

    @Test
    void compilationUnitExtendedInterfaceTest() throws IOException {
        Element element = compilationUnitExtractorTest(sourcePath + "ExtendedInterface.java");
        Assertions.assertEquals("ExtendedInterface", element.getName());
        Assertions.assertEquals("src/test/resources/interface/edu/ExtendedInterface.java", element.getPath());
        Assertions.assertEquals("edu", element.getParentIdentifier().name());
    }

    @Test
    void compilationUnitSuperclassTest() throws IOException {
        Element element = compilationUnitExtractorTest(sourcePath + "Superclass.java");
        Assertions.assertEquals("Superclass", element.getName());
        Assertions.assertEquals("src/test/resources/interface/edu/Superclass.java", element.getPath());
        Assertions.assertEquals("edu", element.getParentIdentifier().name());
    }

    @Test
    void compilationUnitOtherInterfaceZweiTest() throws IOException {
        Element element = compilationUnitExtractorTest(sourcePath + "zwei/OtherInterface.java");
        Assertions.assertEquals("OtherInterface", element.getName());
        Assertions.assertEquals("src/test/resources/interface/edu/zwei/OtherInterface.java", element.getPath());
        Assertions.assertEquals("edu.zwei", element.getParentIdentifier().name());
    }

    @Test
    void compilationUnitOtherInterfaceDreiTest() throws IOException {
        Element element = compilationUnitExtractorTest(sourcePath + "drei/OtherInterface.java");
        Assertions.assertEquals("OtherInterface", element.getName());
        Assertions.assertEquals("src/test/resources/interface/edu/drei/OtherInterface.java", element.getPath());
        Assertions.assertEquals("edu.drei", element.getParentIdentifier().name());
    }

    private Element compilationUnitExtractorTest(String filePath) throws IOException {
        JavaElementExtractor extractor = new JavaElementExtractor();
        Path path = Path.of(filePath);
        JavaLexer lexer = new JavaLexer(CharStreams.fromPath(path));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        extractor.extractElements(tokenStream);
        List<Element> compilationUnits = extractor.getElements().getCompilationUnits();
        Assertions.assertEquals(1, compilationUnits.size());
        return compilationUnits.get(0);
    }

}
