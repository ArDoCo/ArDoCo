/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.generators.antlr.extraction.cpp;

import java.io.IOException;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.tlr.models.antlr4.cpp.CPP14Lexer;
import edu.kit.kastel.mcse.ardoco.tlr.models.antlr4.cpp.CPP14Parser;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Comment;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.cpp.CppCommentExtractor;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.cpp.CppElementStorageRegistry;

class CppCommentExtractorTest {
    private final String sourcePath = "src/test/resources/cpp/interface/edu/";

    @Test
    void commentExtractorMainCPPTest() throws IOException {
        String filePath = sourcePath + "src/main.cpp";
        List<Comment> comments = extractCommentsFromFile(filePath);

        Assertions.assertEquals(4, comments.size());
        Assertions.assertEquals(1, comments.get(0).startLine());
        Assertions.assertEquals(5, comments.get(0).endLine());
        Assertions.assertEquals("Simple C++ Project Author: Your Name Description: A basic C++ project with a simple structure.", comments.get(0).text());

        Assertions.assertEquals(7, comments.get(1).startLine());
        Assertions.assertEquals(7, comments.get(1).endLine());
        Assertions.assertEquals("main.cpp", comments.get(1).text());

        Assertions.assertEquals(12, comments.get(2).startLine());
        Assertions.assertEquals(12, comments.get(2).endLine());
        Assertions.assertEquals("Create a Car object", comments.get(2).text());

        Assertions.assertEquals(16, comments.get(3).startLine());
        Assertions.assertEquals(16, comments.get(3).endLine());
        Assertions.assertEquals("Create a Person object", comments.get(3).text());
    }

    @Test
    public void commentExtractorEntitiesCPPTest() throws IOException {
        String filePath = sourcePath + "src/Entities.cpp";
        List<Comment> comments = extractCommentsFromFile(filePath);

        Assertions.assertEquals(1, comments.size());

        Assertions.assertEquals(2, comments.get(0).startLine());
        Assertions.assertEquals(2, comments.get(0).endLine());
        Assertions.assertEquals("Entities.cpp", comments.get(0).text());
    }

    @Test
    public void commentExtractorEntitiesHTest() throws IOException {
        String filePath = sourcePath + "include/Entities.h";
        List<Comment> comments = extractCommentsFromFile(filePath);

        Assertions.assertEquals(2, comments.size());

        Assertions.assertEquals(1, comments.get(0).startLine());
        Assertions.assertEquals(1, comments.get(0).endLine());
        Assertions.assertEquals("Entities.h", comments.get(0).text());

        Assertions.assertEquals(49, comments.get(1).startLine());
        Assertions.assertEquals(49, comments.get(1).endLine());
        Assertions.assertEquals("ENTITIES_H", comments.get(1).text());
    }

    private List<Comment> extractCommentsFromFile(String filePath) throws IOException {
        CharStream charStream = CharStreams.fromFileName(filePath);
        CPP14Lexer lexer = new CPP14Lexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CPP14Parser parser = new CPP14Parser(tokens);
        parser.translationUnit();

        CppElementStorageRegistry manager = new CppElementStorageRegistry();
        CppCommentExtractor extractor = new CppCommentExtractor(manager);
        extractor.extract(filePath, tokens);
        return extractor.getCurrentComments();
    }
}
