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
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.java.JavaClassElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.java.JavaElementExtractor;

class JavaClassExtractorTest {
    private final String sourcePath = "src/test/resources/interface/edu/";

    @Test
    void classExtractorAClassTest() throws IOException {
        String filePath = sourcePath + "AClass.java";
        List<JavaClassElement> classes = extractClassesFromFile(filePath);
        Assertions.assertEquals(4, classes.size());

        // Assertions Class AnInnerClass
        Assertions.assertEquals("AnInnerClass", classes.get(0).getName());
        Assertions.assertEquals("AClass", classes.get(0).getParentIdentifier().name());
        Assertions.assertEquals(Type.CLASS, classes.get(0).getParentIdentifier().type());

        // Assertions Class AnotherClass (InnerClass)
        Assertions.assertEquals("AnotherClass", classes.get(1).getName());
        Assertions.assertEquals("AClass", classes.get(1).getParentIdentifier().name());
        Assertions.assertEquals(Type.CLASS, classes.get(1).getParentIdentifier().type());

        // Assertions Class AClass
        Assertions.assertEquals("AClass", classes.get(2).getName());
        Assertions.assertEquals("AClass", classes.get(2).getParentIdentifier().name());
        Assertions.assertEquals(Type.COMPILATIONUNIT, classes.get(2).getParentIdentifier().type());
        Assertions.assertEquals("Superclass", classes.get(2).getExtendsClass());
        Assertions.assertEquals("AnInterface", classes.get(2).getImplementedInterfaces().get(0));
        Assertions.assertEquals("OtherInterface", classes.get(2).getImplementedInterfaces().get(1));

        // Assertions Class AnotherClass
        Assertions.assertEquals("AnotherClass", classes.get(3).getName());
        Assertions.assertEquals("AClass", classes.get(3).getParentIdentifier().name());
        Assertions.assertEquals(Type.COMPILATIONUNIT, classes.get(3).getParentIdentifier().type());
    }

    @Test
    void classExtractorSuperclass() throws IOException {
        String filePath = sourcePath + "Superclass.java";
        List<JavaClassElement> classes = extractClassesFromFile(filePath);
        Assertions.assertEquals(1, classes.size());
        Assertions.assertEquals("Superclass", classes.get(0).getName());
        Assertions.assertEquals("Superclass", classes.get(0).getParentIdentifier().name());
        Assertions.assertEquals(Type.COMPILATIONUNIT, classes.get(0).getParentIdentifier().type());
    }

    @Test
    void classExtractorAnEnum() throws IOException {
        String filePath = sourcePath + "AnEnum.java";
        List<JavaClassElement> classes = extractClassesFromFile(filePath);
        Assertions.assertEquals(1, classes.size());
        Assertions.assertEquals("AnEnum", classes.get(0).getName());
        Assertions.assertEquals("AnEnum", classes.get(0).getParentIdentifier().name());
        Assertions.assertEquals(Type.COMPILATIONUNIT, classes.get(0).getParentIdentifier().type());
    }

    @Test
    void classExtractorAnInterface() throws IOException {
        String filePath = sourcePath + "AnInterface.java";
        List<JavaClassElement> classes = extractClassesFromFile(filePath);
        Assertions.assertTrue(classes.isEmpty());
    }

    @Test
    void classExtractorExtendedInterface() throws IOException {
        String filePath = sourcePath + "ExtendedInterface.java";
        List<JavaClassElement> classes = extractClassesFromFile(filePath);
        Assertions.assertTrue(classes.isEmpty());
    }

    @Test
    void classExtractorOtherInterfaceZwei() throws IOException {
        String filePath = sourcePath + "zwei/OtherInterface.java";
        List<JavaClassElement> classes = extractClassesFromFile(filePath);
        Assertions.assertTrue(classes.isEmpty());
    }

    @Test
    void classExtractorOtherInterfaceDrei() throws IOException {
        String filePath = sourcePath + "drei/OtherInterface.java";
        List<JavaClassElement> classes = extractClassesFromFile(filePath);
        Assertions.assertTrue(classes.isEmpty());
    }

    private List<JavaClassElement> extractClassesFromFile(String filePath) throws IOException {
        JavaElementExtractor extractor = new JavaElementExtractor();
        Path path = Path.of(filePath);
        JavaLexer lexer = new JavaLexer(CharStreams.fromPath(path));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        extractor.extractElements(tokenStream);
        return extractor.getElements().getClasses();
    }

}
