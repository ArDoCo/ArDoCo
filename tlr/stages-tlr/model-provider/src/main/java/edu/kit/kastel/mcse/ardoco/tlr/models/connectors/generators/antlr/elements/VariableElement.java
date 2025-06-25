/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements;

/**
 * Represents a variable in the extracted code. Additionally to the information
 * of an element, it contains the data type of the variable if mentioned in code
 */
public class VariableElement extends Element {
    private static final Type type = Type.VARIABLE;
    private final String dataType; // The data type the variable is of

    public VariableElement(String name, String path, String dataType, ElementIdentifier parentIdentifier) {
        super(name, path, type, parentIdentifier);
        this.dataType = dataType;
    }

    public VariableElement(String name, String path, String dataType, ElementIdentifier parentIdentifier, int startLine, int endLine) {
        super(name, path, type, parentIdentifier, startLine, endLine);
        this.dataType = dataType;
    }

    public VariableElement(VariableElement variableElement) {
        super(variableElement);
        this.dataType = variableElement.getDataType();
    }

    public String getDataType() {
        return dataType;
    }

}
