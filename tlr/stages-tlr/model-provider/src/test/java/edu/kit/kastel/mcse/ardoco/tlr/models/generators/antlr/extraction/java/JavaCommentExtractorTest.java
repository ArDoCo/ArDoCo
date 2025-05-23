/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.generators.antlr.extraction.java;

import java.io.IOException;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.tlr.models.antlr4.java.JavaLexer;
import edu.kit.kastel.mcse.ardoco.tlr.models.antlr4.java.JavaParser;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Comment;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.java.JavaCommentExtractor;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.java.JavaElementStorageRegistry;

class JavaCommentExtractorTest {
    private final String sourcePath = "src/test/resources/interface/edu/";

    @Test
    void commentExtractorAClassTest() throws IOException {
        String filePath = sourcePath + "AClass.java";
        List<Comment> comments = extractCommentsFromFile(filePath);
        Assertions.assertEquals(3, comments.size());
        Assertions.assertEquals(5, comments.get(0).startLine());
        Assertions.assertEquals(7, comments.get(0).endLine());
        Assertions.assertEquals("This is a Test Java Doc Comment", comments.get(0).text());

        Assertions.assertEquals(17, comments.get(1).startLine());
        Assertions.assertEquals(17, comments.get(1).endLine());
        Assertions.assertEquals("This is a Test Line Comment", comments.get(1).text());

        Assertions.assertEquals(20, comments.get(2).startLine());
        Assertions.assertEquals(27, comments.get(2).endLine());
        Assertions.assertEquals("This is a Test Block Comment", comments.get(2).text());
    }

    @Test
    void commentExtractorSuperclassTest() throws IOException {
        String filePath = sourcePath + "Superclass.java";
        List<Comment> comments = extractCommentsFromFile(filePath);
        Assertions.assertEquals(1, comments.size());
        Assertions.assertEquals(3, comments.get(0).startLine());
        Assertions.assertEquals(6, comments.get(0).endLine());
        Assertions.assertEquals("This is a Test Java Doc Comment over multiple lines", comments.get(0).text());

    }

    @Test
    void commentExtractorAnEnumTest() throws IOException {
        String filePath = sourcePath + "AnEnum.java";
        List<Comment> comments = extractCommentsFromFile(filePath);
        Assertions.assertEquals(0, comments.size());
    }

    @Test
    void commentExtractorAnInterfaceTest() throws IOException {
        String filePath = sourcePath + "AnInterface.java";
        List<Comment> comments = extractCommentsFromFile(filePath);
        Assertions.assertEquals(0, comments.size());
    }

    @Test
    void commentExtractorExtendedInterfaceTest() throws IOException {
        String filePath = sourcePath + "ExtendedInterface.java";
        List<Comment> comments = extractCommentsFromFile(filePath);
        Assertions.assertEquals(0, comments.size());
    }

    @Test
    void commentExtractorZweiOtherInterfaceTest() throws IOException {
        String filePath = sourcePath + "zwei/OtherInterface.java";
        List<Comment> comments = extractCommentsFromFile(filePath);
        Assertions.assertEquals(0, comments.size());
    }

    @Test
    void commentExtractorDreiOtherInterfaceTest() throws IOException {
        String filePath = sourcePath + "drei/OtherInterface.java";
        List<Comment> comments = extractCommentsFromFile(filePath);
        Assertions.assertEquals(0, comments.size());
    }

    private List<Comment> extractCommentsFromFile(String filePath) throws IOException {
        CharStream input = CharStreams.fromFileName(filePath);
        JavaLexer lexer = new JavaLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);
        parser.compilationUnit();

        JavaElementStorageRegistry manager = new JavaElementStorageRegistry();
        JavaCommentExtractor extractor = new JavaCommentExtractor(manager);
        extractor.extract(filePath, tokens);
        return extractor.getCurrentComments();
    }

}
