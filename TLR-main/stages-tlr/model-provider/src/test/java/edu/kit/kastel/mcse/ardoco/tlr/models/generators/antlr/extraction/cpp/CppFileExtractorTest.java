package edu.kit.kastel.mcse.ardoco.tlr.models.generators.antlr.extraction.cpp;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.cpp.CppElementExtractor;

public class CppFileExtractorTest {
    private final String sourcePath = "src/test/resources/cpp/interface/edu/";

    @Test
    void fileExtractorMainCPPTest() throws IOException {
        String filePath = sourcePath + "src/main.cpp";
        List<Element> files = extractFileFromFile(filePath);

        Assertions.assertEquals(1, files.size());
        Assertions.assertEquals("main", files.get(0).getName());
        Assertions.assertEquals("src/test/resources/cpp/interface/edu/src/main.cpp", files.get(0).getPath());
        Assertions.assertNull(files.get(0).getParent());
    }

    @Test
    void fileExtractorEntitiesCPPTest() throws IOException {
        String filePath = sourcePath + "src/Entities.cpp";
        List<Element> files = extractFileFromFile(filePath);

        Assertions.assertEquals(1, files.size());
        Assertions.assertEquals("Entities", files.get(0).getName());
        Assertions.assertEquals("src/test/resources/cpp/interface/edu/src/Entities.cpp", files.get(0).getPath());
        Assertions.assertNull(files.get(0).getParent());
    }

    @Test
    void fileExtractorEntitiesHTest() throws IOException {
        String filePath = sourcePath + "include/Entities.h";
        List<Element> files = extractFileFromFile(filePath);

        Assertions.assertEquals(1, files.size());
        Assertions.assertEquals("Entities", files.get(0).getName());
        Assertions.assertEquals("src/test/resources/cpp/interface/edu/include/Entities.h", files.get(0).getPath());
        Assertions.assertNull(files.get(0).getParent());
    }

    private List<Element> extractFileFromFile(String filePath) throws IOException {
        CppElementExtractor extractor = new CppElementExtractor();
        Path path = Path.of(filePath);
        extractor.extract(path);
        return extractor.getElements().getFiles();
    }
    
}
