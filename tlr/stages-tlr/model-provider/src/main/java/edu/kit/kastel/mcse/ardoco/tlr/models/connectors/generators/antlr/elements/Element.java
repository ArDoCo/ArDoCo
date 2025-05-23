/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements;

import java.util.Objects;

/**
 * Represents a structural element in the code. Contains information about the
 * name, path, parent in the tree, start and end line, and comment.
 */

public class Element {
    protected ElementIdentifier identifierOfParent;
    protected final ElementIdentifier identifier;
    private int startLine;
    private int endLine;
    private String comment;

    public Element(String name, String path, Type type) {
        this(new ElementIdentifier(name, path, type));
    }

    public Element(ElementIdentifier identifier) {
        this.identifier = identifier;
        this.comment = "";
        this.startLine = -1;
        this.endLine = -1;
    }

    public Element(ElementIdentifier identifier, ElementIdentifier identifierOfParent) {
        this(identifier);
        this.identifierOfParent = identifierOfParent;
    }

    public Element(ElementIdentifier identifier, ElementIdentifier identifierOfParent, int startLine, int endLine) {
        this(identifier, identifierOfParent);
        this.startLine = startLine;
        this.endLine = endLine;
    }

    public Element(String name, String path, Type type, ElementIdentifier identifierOfParent) {
        this(name, path, type);
        this.identifierOfParent = identifierOfParent;
    }

    public Element(String name, String path, Type type, ElementIdentifier identifierOfParent, int startLine, int endLine) {
        this(name, path, type);
        this.identifierOfParent = identifierOfParent;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    public Element(Element elementToCopy) {
        this.identifier = new ElementIdentifier(elementToCopy.getIdentifier().name(), elementToCopy.getIdentifier().path(), elementToCopy.getIdentifier()
                .type());
        if (elementToCopy.getParentIdentifier() != null) {
            this.identifierOfParent = new ElementIdentifier(elementToCopy.getParentIdentifier().name(), elementToCopy.getParentIdentifier().path(),
                    elementToCopy.getParentIdentifier().type());
        }
        this.startLine = elementToCopy.getStartLine();
        this.endLine = elementToCopy.getEndLine();
        this.comment = elementToCopy.getComment();

    }

    public ElementIdentifier getParentIdentifier() {
        return this.identifierOfParent;
    }

    public ElementIdentifier getIdentifier() {
        return identifier;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public String getComment() {
        return comment;
    }

    public String getPath() {
        return identifier.path();
    }

    public String getName() {
        return identifier.name();
    }

    public void setStartLine(int fromLine) {
        this.startLine = fromLine;
    }

    public void setEndLine(int toLine) {
        this.endLine = toLine;
    }

    public void setComment(String comment) {
        if (!this.comment.equals("")) {
            this.comment += "\n";
        }

        this.comment += comment;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Element) {
            Element basicElement = (Element) obj;
            return Objects.equals(identifier, basicElement.getIdentifier()) && Objects.equals(basicElement.getParentIdentifier(), this
                    .getParentIdentifier()) && basicElement.getStartLine() == this.getStartLine() && basicElement.getEndLine() == this.getEndLine();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, identifierOfParent);
    }
}
