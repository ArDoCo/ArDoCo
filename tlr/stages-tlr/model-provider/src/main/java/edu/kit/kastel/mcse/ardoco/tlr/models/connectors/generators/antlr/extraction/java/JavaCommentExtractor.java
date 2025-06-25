/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.java;

import org.antlr.v4.runtime.Token;

import edu.kit.kastel.mcse.ardoco.tlr.models.antlr4.java.JavaLexer;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.CommentExtractor;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.java.JavaElementStorageRegistry;

/**
 * Responsible for extracting comments from Java code. Defines the rules for
 * valid comments and how to get only the text of the comment without the
 * delimiters.
 */
public class JavaCommentExtractor extends CommentExtractor {

    public JavaCommentExtractor(JavaElementStorageRegistry elementManager) {
        super(elementManager);
    }

    @Override
    protected boolean isComment(Token token) {
        return token.getChannel() == JavaLexer.HIDDEN;
    }

    @Override
    protected boolean isValidComment(String comment) {
        return !comment.isEmpty() && ((comment.startsWith("//") || comment.startsWith("/*")));
    }

    @Override
    protected String cleanseComment(String text) {
        // Remove block comment delimiters (/** and */)
        text = text.replaceAll("^(?:/\\*+)|(?:\\*/)$", "").trim();

        // Remove leading '*' characters from each line but keep newlines
        text = text.replaceAll("(?m)^\\s*\\* ?", "").trim();

        // Remove inline '//' comments
        text = text.replaceAll("^//", "").trim();

        // Normalize spaces: Convert multiple spaces/newlines into a single space
        text = text.replaceAll("\\s+", " ");

        return text;
    }

}
