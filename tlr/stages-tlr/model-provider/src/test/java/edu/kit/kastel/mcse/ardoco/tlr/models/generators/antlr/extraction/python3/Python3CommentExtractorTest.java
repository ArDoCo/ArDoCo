/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.generators.antlr.extraction.python3;

import java.io.IOException;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.tlr.models.antlr4.python3.Python3Lexer;
import edu.kit.kastel.mcse.ardoco.tlr.models.antlr4.python3.Python3Parser;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Comment;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.python3.Python3CommentExtractor;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.python3.Python3ElementStorageRegistry;

class Python3CommentExtractorTest {
    private final String sourcePath = "src/test/resources/python/interface/edu/";

    @Test
    void commentExtractorAPyClassTest() throws IOException {
        String filePath = sourcePath + "APyClass.py";
        List<Comment> comments = extractCommentsFromFile(filePath);
        Assertions.assertEquals(4, comments.size());

        // Detailed Assertions
        Assertions.assertEquals(2, comments.get(0).startLine());
        Assertions.assertEquals(2, comments.get(0).endLine());
        Assertions.assertEquals("This is a Comment for AClass", comments.get(0).text());

        Assertions.assertEquals(3, comments.get(1).startLine());
        Assertions.assertEquals(3, comments.get(1).endLine());
        Assertions.assertEquals("This is an inline comment for class_variable of AClass", comments.get(1).text());

        Assertions.assertEquals(15, comments.get(2).startLine());
        Assertions.assertEquals(18, comments.get(2).endLine());
        Assertions.assertEquals("This is a multiple line comment for InnerClass1", comments.get(2).text());

        Assertions.assertEquals(31, comments.get(3).startLine());
        Assertions.assertEquals(34, comments.get(3).endLine());
        Assertions.assertEquals("This is a multiple line comment for InnerClass2", comments.get(3).text());
    }

    private List<Comment> extractCommentsFromFile(String filePath) throws IOException {
        CharStream input = CharStreams.fromFileName(filePath);
        Python3Lexer lexer = new Python3Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Python3Parser parser = new Python3Parser(tokens);
        parser.file_input();
        Python3ElementStorageRegistry manager = new Python3ElementStorageRegistry();

        Python3CommentExtractor extractor = new Python3CommentExtractor(manager);
        extractor.extract(filePath, tokens);
        return extractor.getCurrentComments();
    }

}
