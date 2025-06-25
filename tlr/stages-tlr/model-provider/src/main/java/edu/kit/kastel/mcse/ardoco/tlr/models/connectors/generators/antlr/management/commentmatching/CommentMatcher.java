/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.commentmatching;

import java.util.List;

import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Comment;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;

/**
 * Responsible for matching comments to structural elements.
 * The matching is based on the line distance between the comment and the
 * element.
 * The rules for what is considered the closest elements must be implemented in
 * the subclasses.
 */
public class CommentMatcher {

    public CommentMatcher() {
    }

    public void matchComments(List<Comment> comments, List<Element> allElements) {
        for (Comment comment : comments) {
            Element closestElement = findClosestElement(comment, allElements);

            if (closestElement != null) {
                setCommentToElement(closestElement, comment);
            }
        }
    }

    private Element findClosestElement(Comment comment, List<Element> allElements) {
        int closestLineDifferenceSoFar = Integer.MAX_VALUE;
        Element closestElement = null;

        for (Element element : allElements) {
            if (hasSamePath(comment, element)) {
                int calculatedLineDifference = calculateDistance(comment, element);

                if (calculatedLineDifference < closestLineDifferenceSoFar) {
                    closestLineDifferenceSoFar = calculatedLineDifference;
                    closestElement = element;
                }
            }
        }
        return closestElement;
    }

    private boolean hasSamePath(Comment comment, Element element) {
        return element.getIdentifier().path().equals(comment.path());
    }

    private void setCommentToElement(Element element, Comment comment) {
        element.setComment(comment.text());
    }

    private int calculateDistance(Comment comment, Element element) {
        int elementStartLine = element.getStartLine();
        int commentStartLine = comment.startLine();
        int commentEndLine = comment.endLine();

        int lineDifference = calculateDifference(elementStartLine, commentStartLine, commentEndLine);

        return lineDifference;
    }

    protected int calculateDifference(int elementStartLine, int commentStartLine, int commentEndLine) {
        // default max value
        int lineDifference = Integer.MAX_VALUE;
        // Comments Ideally just before Element
        if (commentStartLine == elementStartLine + 1) {
            return 0;
        }

        // Comment before element
        if (commentStartLine <= elementStartLine) {
            lineDifference = Math.abs(elementStartLine - commentEndLine);
        }
        return lineDifference;
    }
}
