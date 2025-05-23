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
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.VariableElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.python3.Python3ElementExtractor;

class Python3VariableExtractorTest {
    String sourcePath = "src/test/resources/python/interface/edu/";

    @Test
    void testPyAClassVariableExtractor() throws IOException {
        String filePath = sourcePath + "APyClass.py";
        List<VariableElement> variables = extractVariablesFromFile(filePath);
        Assertions.assertEquals(10, variables.size());

        // Test the first variable
        Assertions.assertEquals("class_variable", variables.get(0).getName());
        Assertions.assertEquals("str", variables.get(0).getDataType());
        Assertions.assertEquals("AClass", variables.get(0).getParentIdentifier().name());
        Assertions.assertEquals(Type.CLASS, variables.get(0).getParentIdentifier().type());

        // Test the second variable
        Assertions.assertEquals("self.name", variables.get(1).getName());
        Assertions.assertEquals("any", variables.get(1).getDataType());
        Assertions.assertEquals("__init__", variables.get(1).getParentIdentifier().name());
        Assertions.assertEquals(Type.FUNCTION, variables.get(1).getParentIdentifier().type());

        // Test the third variable
        Assertions.assertEquals("self.instance_variable", variables.get(2).getName());
        Assertions.assertEquals("str", variables.get(2).getDataType());
        Assertions.assertEquals("__init__", variables.get(2).getParentIdentifier().name());
        Assertions.assertEquals(Type.FUNCTION, variables.get(2).getParentIdentifier().type());

        // Test the fourth variable
        Assertions.assertEquals("class_variable", variables.get(3).getName());
        Assertions.assertEquals("str", variables.get(3).getDataType());
        Assertions.assertEquals("InnerClass1", variables.get(3).getParentIdentifier().name());
        Assertions.assertEquals(Type.CLASS, variables.get(3).getParentIdentifier().type());

        // Test the fifth variable
        Assertions.assertEquals("self.value", variables.get(4).getName());
        Assertions.assertEquals("any", variables.get(4).getDataType());
        Assertions.assertEquals("__init__", variables.get(4).getParentIdentifier().name());
        Assertions.assertEquals(Type.FUNCTION, variables.get(4).getParentIdentifier().type());

        // Test the sixth variable
        Assertions.assertEquals("self.instance_variable", variables.get(5).getName());
        Assertions.assertEquals("str", variables.get(5).getDataType());
        Assertions.assertEquals("__init__", variables.get(5).getParentIdentifier().name());
        Assertions.assertEquals(Type.FUNCTION, variables.get(5).getParentIdentifier().type());

        // Test the seventh variable
        Assertions.assertEquals("class_variable", variables.get(6).getName());
        Assertions.assertEquals("str", variables.get(6).getDataType());
        Assertions.assertEquals("InnerClass2", variables.get(6).getParentIdentifier().name());
        Assertions.assertEquals(Type.CLASS, variables.get(6).getParentIdentifier().type());

        // Test the eighth variable
        Assertions.assertEquals("self.value", variables.get(7).getName());
        Assertions.assertEquals("any", variables.get(7).getDataType());
        Assertions.assertEquals("__init__", variables.get(7).getParentIdentifier().name());
        Assertions.assertEquals(Type.FUNCTION, variables.get(7).getParentIdentifier().type());

        // Test the ninth variable
        Assertions.assertEquals("self.instance_variable", variables.get(8).getName());
        Assertions.assertEquals("str", variables.get(8).getDataType());
        Assertions.assertEquals("__init__", variables.get(8).getParentIdentifier().name());
        Assertions.assertEquals(Type.FUNCTION, variables.get(8).getParentIdentifier().type());

        // Test the tenth variable
        Assertions.assertEquals("class_variable", variables.get(9).getName());
        Assertions.assertEquals("str", variables.get(9).getDataType());
        Assertions.assertEquals("BClass", variables.get(9).getParentIdentifier().name());
        Assertions.assertEquals(Type.CLASS, variables.get(9).getParentIdentifier().type());
    }

    private List<VariableElement> extractVariablesFromFile(String filePath) throws IOException {
        Python3ElementExtractor extractor = new Python3ElementExtractor();
        Path path = Path.of(filePath);
        Python3Lexer lexer = new Python3Lexer(CharStreams.fromPath(path));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        extractor.extractElements(tokenStream);
        return extractor.getElements().getVariables();
    }

}
