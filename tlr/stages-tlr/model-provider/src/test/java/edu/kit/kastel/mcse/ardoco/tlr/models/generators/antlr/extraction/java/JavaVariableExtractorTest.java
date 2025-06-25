/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.generators.antlr.extraction.java;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.tlr.models.antlr4.java.JavaLexer;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.VariableElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.java.JavaElementExtractor;

class JavaVariableExtractorTest {
    private final String sourcePath = "src/test/resources/interface/edu/";

    @Test
    void variableExtractorAClassTest() throws IOException {
        String filePath = sourcePath + "AClass.java";
        List<VariableElement> variables = extractVariablesFromFile(filePath);
        Assertions.assertEquals(3, variables.size());
        Assertions.assertEquals("s", variables.get(0).getName());
        Assertions.assertEquals("Superclass", variables.get(0).getDataType());

        Assertions.assertEquals("x", variables.get(1).getName());
        Assertions.assertEquals("int", variables.get(1).getDataType());
        Assertions.assertEquals("anEnum", variables.get(2).getName());
        Assertions.assertEquals("AnEnum", variables.get(2).getDataType());

        // Parent Test for sake of completeness
        Assertions.assertEquals("AClass", variables.get(0).getParentIdentifier().name());
        Assertions.assertEquals(Type.CLASS, variables.get(0).getParentIdentifier().type());
        Assertions.assertEquals("AClass", variables.get(1).getParentIdentifier().name());
        Assertions.assertEquals(Type.CLASS, variables.get(1).getParentIdentifier().type());
        Assertions.assertEquals("aMethod", variables.get(2).getParentIdentifier().name());
        Assertions.assertEquals(Type.FUNCTION, variables.get(2).getParentIdentifier().type());

    }

    @Test
    void variableExtractorAnEnum() throws IOException {
        String filePath = sourcePath + "AnEnum.java";
        List<VariableElement> variables = extractVariablesFromFile(filePath);
        Assert.assertTrue(variables.isEmpty());
    }

    @Test
    void variableExtractorAnInterface() throws IOException {
        String filePath = sourcePath + "AnInterface.java";
        List<VariableElement> variables = extractVariablesFromFile(filePath);
        Assert.assertTrue(variables.isEmpty());
    }

    @Test
    void variableExtractorExtendedInterface() throws IOException {
        String filePath = sourcePath + "ExtendedInterface.java";
        List<VariableElement> variables = extractVariablesFromFile(filePath);
        Assert.assertTrue(variables.isEmpty());
    }

    @Test
    void variableExtractorSuperclass() throws IOException {
        String filePath = sourcePath + "Superclass.java";
        List<VariableElement> variables = extractVariablesFromFile(filePath);
        Assert.assertTrue(variables.isEmpty());
    }

    @Test
    void variableExtractorOtherInterfaceZwei() throws IOException {
        String filePath = sourcePath + "zwei/OtherInterface.java";
        List<VariableElement> variables = extractVariablesFromFile(filePath);
        Assert.assertTrue(variables.isEmpty());
    }

    @Test
    void variableExtractorOtherInterfaceDrei() throws IOException {
        String filePath = sourcePath + "drei/OtherInterface.java";
        List<VariableElement> variables = extractVariablesFromFile(filePath);
        Assert.assertTrue(variables.isEmpty());
    }

    private List<VariableElement> extractVariablesFromFile(String filePath) throws IOException {
        JavaElementExtractor extractor = new JavaElementExtractor();
        Path path = Path.of(filePath);
        JavaLexer lexer = new JavaLexer(CharStreams.fromPath(path));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        extractor.extractElements(tokenStream);
        return extractor.getElements().getVariables();
    }

}
