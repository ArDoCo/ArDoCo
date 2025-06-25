/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.generators.antlr.extraction.cpp;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.tlr.models.antlr4.cpp.CPP14Lexer;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ClassElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.cpp.CppElementExtractor;

class CppClassExtractorTest {
    String sourcePath = "src/test/resources/cpp/interface/edu/";

    @Test
    public void testCppClassExtractorMainCPPTest() throws IOException {
        String filePath = sourcePath + "src/main.cpp";
        List<ClassElement> classes = extractClassElementsFromFile(filePath);

        // Test the first class
        Assertions.assertEquals(0, classes.size());
    }

    @Test
    public void testCppClassExtractorEntitiesCPPTest() throws IOException {
        String filePath = sourcePath + "src/Entities.cpp";
        List<ClassElement> classes = extractClassElementsFromFile(filePath);

        // Test the first class
        Assertions.assertEquals(0, classes.size());
    }

    @Test
    public void testCppClassExtractorEntitiesHTest() throws IOException {
        String filePath = sourcePath + "include/Entities.h";
        List<ClassElement> classes = extractClassElementsFromFile(filePath);

        // Test the first class
        Assertions.assertEquals(5, classes.size());
        Assertions.assertEquals("Car", classes.get(0).getName());
        Assertions.assertEquals("Entities", classes.get(0).getParentIdentifier().name());
        Assertions.assertEquals(Type.NAMESPACE, classes.get(0).getParentIdentifier().type());
        Assertions.assertEquals("Person", classes.get(1).getName());
        Assertions.assertEquals("Entities", classes.get(1).getParentIdentifier().name());
        Assertions.assertEquals(Type.NAMESPACE, classes.get(1).getParentIdentifier().type());
        Assertions.assertEquals("Garage", classes.get(2).getName());
        Assertions.assertEquals("Entities", classes.get(2).getParentIdentifier().name());
        Assertions.assertEquals(Type.NAMESPACE, classes.get(2).getParentIdentifier().type());
        Assertions.assertEquals("Mechanic", classes.get(3).getName());
        Assertions.assertEquals("Garage", classes.get(3).getParentIdentifier().name());
        Assertions.assertEquals(Type.CLASS, classes.get(3).getParentIdentifier().type());
        Assertions.assertEquals("Child", classes.get(4).getName());
        Assertions.assertEquals("Entities", classes.get(4).getParentIdentifier().name());
        Assertions.assertEquals(Type.NAMESPACE, classes.get(4).getParentIdentifier().type());
        Assertions.assertEquals(1, classes.get(4).getInherits().size());
        Assertions.assertEquals("Person", classes.get(4).getInherits().get(0));
    }

    private List<ClassElement> extractClassElementsFromFile(String filePath) throws IOException {
        CppElementExtractor extractor = new CppElementExtractor();
        Path path = Path.of(filePath);
        CPP14Lexer lexer = new CPP14Lexer(CharStreams.fromPath(path));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        extractor.extractElements(tokenStream);
        return extractor.getElements().getClasses();
    }
}
