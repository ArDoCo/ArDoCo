/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a class. Contains information about the class name, path, its
 * parent in the tree, inheritance.
 */
public class ClassElement extends Element {
    private final List<String> inherits; // Expected to be a list of the names of classes it inherits from
    private static final Type type = Type.CLASS;

    public ClassElement(String name, String path, ElementIdentifier parentIdentifier) {
        super(name, path, type, parentIdentifier);
        this.inherits = new ArrayList<>();
    }

    public ClassElement(String name, String path, ElementIdentifier parentIdentifier, int startLine, int endLine, List<String> inherits) {
        super(name, path, type, parentIdentifier, startLine, endLine);
        this.inherits = inherits;
    }

    public ClassElement(String name, String path, ElementIdentifier parentIdentifier, int startLine, int endLine) {
        super(name, path, type, parentIdentifier, startLine, endLine);
        this.inherits = new ArrayList<>();
    }

    public ClassElement(ElementIdentifier identifier, ElementIdentifier identifierOfParent, int startLine, int endLine, List<String> inherits) {
        super(identifier, identifierOfParent, startLine, endLine);
        this.inherits = inherits;
    }

    public ClassElement(ClassElement classElement) {
        super(classElement);
        this.inherits = new ArrayList<>(classElement.getInherits());
    }

    public List<String> getInherits() {
        return this.inherits;
    }

}
