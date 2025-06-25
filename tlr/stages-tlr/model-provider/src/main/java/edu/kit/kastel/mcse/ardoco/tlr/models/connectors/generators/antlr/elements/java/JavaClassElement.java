/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.java;

import java.util.ArrayList;
import java.util.List;

import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ElementIdentifier;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;

/**
 * Represents a Java Class. Contains information about the class name, path, its
 * parent in the tree, inheritance and implemented interfaces.
 */

public class JavaClassElement extends Element {
    private static final Type type = Type.CLASS;
    private String extendsClass; // The name of the class it extends
    private final List<String> implementedInterfaces; // The names of the interfaces it implements

    public JavaClassElement(String name, String path, ElementIdentifier parentIdentifier, int startLine, int endLine) {
        super(name, path, type, parentIdentifier, startLine, endLine);
        this.extendsClass = "";
        this.implementedInterfaces = new ArrayList<String>();
    }

    public JavaClassElement(String name, String path, ElementIdentifier parentIdentifier, String extendsClass, List<String> implementedInterfaces,
            int startLine, int endLine) {
        super(name, path, type, parentIdentifier, startLine, endLine);
        this.extendsClass = extendsClass;
        this.implementedInterfaces = implementedInterfaces;
    }

    public JavaClassElement(JavaClassElement classElement) {
        super(classElement);
        this.extendsClass = classElement.getExtendsClass();
        this.implementedInterfaces = new ArrayList<>(classElement.getImplementedInterfaces());
    }

    public String getExtendsClass() {
        return extendsClass;
    }

    public void addImplementedInterfaces(List<String> interfaces) {
        this.implementedInterfaces.addAll(interfaces);
    }

    public List<String> getImplementedInterfaces() {
        return implementedInterfaces;
    }

}
