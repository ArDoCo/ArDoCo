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

class Python3ControlExtractorTest {
    String sourcePath = "src/test/resources/python/interface/edu/";

    @Test
    void testPyAClassControlExtractor() throws IOException {
        String filePath = sourcePath + "APyClass.py";
        List<Element> controls = extractBasicElementsFromFile(filePath);
        Assertions.assertEquals(6, controls.size());

        // Test the first control
        Assertions.assertEquals("__init__", controls.get(0).getName());
        Assertions.assertEquals("AClass", controls.get(0).getParentIdentifier().name());
        Assertions.assertEquals(Type.CLASS, controls.get(0).getParentIdentifier().type());

        // Test the second control
        Assertions.assertEquals("display_name", controls.get(1).getName());
        Assertions.assertEquals("AClass", controls.get(1).getParentIdentifier().name());
        Assertions.assertEquals(Type.CLASS, controls.get(1).getParentIdentifier().type());

        // Test the third control
        Assertions.assertEquals("__init__", controls.get(2).getName());
        Assertions.assertEquals("InnerClass1", controls.get(2).getParentIdentifier().name());
        Assertions.assertEquals(Type.CLASS, controls.get(2).getParentIdentifier().type());

        // Test the fourth control
        Assertions.assertEquals("display_value", controls.get(3).getName());
        Assertions.assertEquals("InnerClass1", controls.get(3).getParentIdentifier().name());
        Assertions.assertEquals(Type.CLASS, controls.get(3).getParentIdentifier().type());

        // Test the fifth control
        Assertions.assertEquals("__init__", controls.get(4).getName());
        Assertions.assertEquals("InnerClass2", controls.get(4).getParentIdentifier().name());
        Assertions.assertEquals(Type.CLASS, controls.get(4).getParentIdentifier().type());

        // Test the sixth control
        Assertions.assertEquals("display_value", controls.get(5).getName());
        Assertions.assertEquals("InnerClass2", controls.get(5).getParentIdentifier().name());
        Assertions.assertEquals(Type.CLASS, controls.get(5).getParentIdentifier().type());
    }

    @Test
    void testPyModuleControlExtractor() throws IOException {
        String filePath = sourcePath + "APyModule.py";
        List<Element> controls = extractBasicElementsFromFile(filePath);
        Assertions.assertEquals(3, controls.size());

        // Test the first control
        Assertions.assertEquals("greet", controls.get(0).getName());
        Assertions.assertEquals("APyModule", controls.get(0).getParentIdentifier().name());
        Assertions.assertEquals(Type.MODULE, controls.get(0).getParentIdentifier().type());

        // Test the second control
        Assertions.assertEquals("add", controls.get(1).getName());
        Assertions.assertEquals("APyModule", controls.get(1).getParentIdentifier().name());
        Assertions.assertEquals(Type.MODULE, controls.get(1).getParentIdentifier().type());

        // Test the third control
        Assertions.assertEquals("subtract", controls.get(2).getName());
        Assertions.assertEquals("APyModule", controls.get(2).getParentIdentifier().name());
        Assertions.assertEquals(Type.MODULE, controls.get(2).getParentIdentifier().type());
    }

    @Test
    void testPyMetaclassExtractor() throws IOException {
        String filePath = sourcePath + "APyMetaclass.py";
        List<Element> controls = extractBasicElementsFromFile(filePath);
        Assertions.assertEquals(2, controls.size());

        // Test the first control
        Assertions.assertEquals("__new__", controls.get(0).getName());
        Assertions.assertEquals("APyMetaclass", controls.get(0).getParentIdentifier().name());
        Assertions.assertEquals(Type.CLASS, controls.get(0).getParentIdentifier().type());

        // Test the second control
        Assertions.assertEquals("__init__", controls.get(1).getName());
        Assertions.assertEquals("APyMetaclass", controls.get(1).getParentIdentifier().name());
        Assertions.assertEquals(Type.CLASS, controls.get(1).getParentIdentifier().type());
    }

    @Test
    void testPyEnumControlExtractor() throws IOException {
        String filePath = sourcePath + "APyEnum.py";
        List<Element> controls = extractBasicElementsFromFile(filePath);
        Assertions.assertEquals(0, controls.size());
    }

    private List<Element> extractBasicElementsFromFile(String filePath) throws IOException {
        Python3ElementExtractor extractor = new Python3ElementExtractor();
        Path path = Path.of(filePath);
        Python3Lexer lexer = new Python3Lexer(CharStreams.fromPath(path));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        extractor.extractElements(tokenStream);
        return extractor.getElements().getFunctions();
    }
}
