/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.commentmatching;

/**
 * Responsible for matching comments from C++ code to structural elements.
 * Defines the rules for what is considered the closest distance between a
 * comment and an element.
 */
public class Python3CommentMatcher extends CommentMatcher {

    public Python3CommentMatcher() {
        super();
    }

    @Override
    protected int calculateDifference(int elementStartLine, int commentStartLine, int commentEndLine) {
        // Comments Ideally just after Element
        if (commentStartLine == elementStartLine + 1) {
            return 0;
        }

        // Comment before element
        if (commentEndLine < elementStartLine) {
            return Integer.MAX_VALUE;
        }

        return Math.abs(elementStartLine - commentEndLine);
    }
}
