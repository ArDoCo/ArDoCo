/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.cpp;

import org.antlr.v4.runtime.Token;

import edu.kit.kastel.mcse.ardoco.tlr.models.antlr4.cpp.CPP14Lexer;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.CommentExtractor;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.cpp.CppElementStorageRegistry;

/**
 * Extracts comments from C++ code. Defines the rules for valid comments and how
 * to get only the text of the comment without the delimiters.
 */
public class CppCommentExtractor extends CommentExtractor {

    public CppCommentExtractor(CppElementStorageRegistry elementManager) {
        super(elementManager);
    }

    @Override
    protected boolean isComment(Token token) {
        return token.getType() == CPP14Lexer.LineComment || token.getType() == CPP14Lexer.BlockComment;
    }

    @Override
    protected boolean isValidComment(String comment) {
        return !comment.isEmpty() && (comment.startsWith("//") || comment.startsWith("/*"));
    }

    @Override
    protected String cleanseComment(String text) {
        // Remove block comment delimiters (/* and */)
        text = text.replaceAll("^(?:/\\*+)|(?:\\*/)$", "").trim();

        // Remove leading '*' characters from each line while keeping line breaks
        text = text.replaceAll("(?m)^\\s*\\* ?", "");

        // Remove '//' from the start of each line
        text = text.replaceAll("(?m)^\\s*// ?", "");

        // Normalize multiple newlines and spaces → Collapse multiple newlines into a
        // single space
        text = text.replaceAll("[\\r\\n]+", " ");

        return text.trim();
    }

}
