/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.cpp;

import java.util.List;

import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ClassElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ElementIdentifier;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.VariableElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.ElementStorage;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.ElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.commentmatching.CommentMatcher;

/**
 * Registry for storing elements of a C++ codebase.
 * Defines the set of element objects that can be stored and provides methods to
 * add and retrieve specific elements.
 */
public class CppElementStorageRegistry extends ElementStorageRegistry {

    public CppElementStorageRegistry() {
        super(new CommentMatcher());
    }

    public CppElementStorageRegistry(List<VariableElement> variables, List<Element> functions, List<ClassElement> classes, List<Element> namespaces,
            List<Element> files) {
        this();
        addElements(Type.VARIABLE, variables);
        addElements(Type.FUNCTION, functions);
        addElements(Type.CLASS, classes);
        addElements(Type.NAMESPACE, namespaces);
        addElements(Type.FILE, files);
    }

    public CppElementStorageRegistry(CppElementStorageRegistry registry) {
        this();
        addVariables(registry.getVariables());
        addFunctions(registry.getFunctions());
        addClasses(registry.getClasses());
        addNamespaces(registry.getNamespaces());
        addFiles(registry.getFiles());
    }

    @Override
    protected void registerStorage() {
        registerStorage(Type.VARIABLE, new ElementStorage<VariableElement>(VariableElement.class));
        registerStorage(Type.FUNCTION, new ElementStorage<Element>(Element.class));
        registerStorage(Type.CLASS, new ElementStorage<ClassElement>(ClassElement.class));
        registerStorage(Type.NAMESPACE, new ElementStorage<Element>(Element.class));
        registerStorage(Type.FILE, new ElementStorage<Element>(Element.class));
    }

    public void addVariable(VariableElement variable) {
        addElement(Type.VARIABLE, variable);
    }

    public void addFunction(Element function) {
        addElement(Type.FUNCTION, function);
    }

    public void addClass(ClassElement clazz) {
        addElement(Type.CLASS, clazz);
    }

    public void addNamespace(Element namespace) {
        addElement(Type.NAMESPACE, namespace);
    }

    public void addFile(Element file) {
        addElement(Type.FILE, file);
    }

    public void addVariables(List<VariableElement> variables) {
        addElements(Type.VARIABLE, variables);
    }

    public void addFunctions(List<Element> functions) {
        addElements(Type.FUNCTION, functions);
    }

    public void addClasses(List<ClassElement> classes) {
        addElements(Type.CLASS, classes);
    }

    public void addNamespaces(List<Element> namespaces) {
        addElements(Type.NAMESPACE, namespaces);
    }

    public void addFiles(List<Element> files) {
        addElements(Type.FILE, files);
    }

    public VariableElement getVariable(ElementIdentifier identifier) {
        return new VariableElement(getElement(identifier, VariableElement.class));
    }

    public Element getFunction(ElementIdentifier identifier) {
        return new Element(getElement(identifier, Element.class));
    }

    public ClassElement getClass(ElementIdentifier identifier) {
        return new ClassElement(getElement(identifier, ClassElement.class));
    }

    public Element getNamespace(ElementIdentifier identifier) {
        return new Element(getElement(identifier, Element.class));
    }

    public Element getFile(ElementIdentifier identifier) {
        return new Element(getElement(identifier, Element.class));
    }

    public List<VariableElement> getVariables() {
        return getElements(Type.VARIABLE, VariableElement.class).stream().map(VariableElement::new).toList();
    }

    public List<Element> getFunctions() {
        return getElements(Type.FUNCTION, Element.class).stream().map(Element::new).toList();
    }

    public List<ClassElement> getClasses() {
        return getElements(Type.CLASS, ClassElement.class).stream().map(ClassElement::new).toList();
    }

    public List<Element> getNamespaces() {
        return getElements(Type.NAMESPACE, Element.class).stream().map(Element::new).toList();
    }

    public List<Element> getFiles() {
        return getElements(Type.FILE, Element.class).stream().map(Element::new).toList();
    }

    public boolean isNamespaceElement(Element element) {
        return containsElement(Type.NAMESPACE, element);
    }

    public boolean isVariableElement(Element element) {
        return containsElement(Type.VARIABLE, element);
    }

    public boolean isClassElement(Element element) {
        return containsElement(Type.CLASS, element);
    }

    public boolean isFunctionElement(Element element) {
        return containsElement(Type.FUNCTION, element);
    }

    public boolean isFileElement(Element element) {
        return containsElement(Type.FILE, element);
    }

    public List<VariableElement> getVariablesWithParentIdentifier(ElementIdentifier parentIdentifier) {
        List<VariableElement> variables = getContentOfIdentifier(Type.VARIABLE, parentIdentifier);
        return variables.stream().map(VariableElement::new).toList();
    }

    public List<Element> getFunctionsWithParentIdentifier(ElementIdentifier parentIdentifier) {
        List<Element> functions = getContentOfIdentifier(Type.FUNCTION, parentIdentifier);
        return functions.stream().map(Element::new).toList();
    }

    public List<ClassElement> getClassesWithParentIdentifier(ElementIdentifier parentIdentifier) {
        List<ClassElement> classes = getContentOfIdentifier(Type.CLASS, parentIdentifier);
        return classes.stream().map(ClassElement::new).toList();
    }

    public List<Element> getNamespacesWithParentIdentifier(ElementIdentifier parentIdentifier) {
        List<Element> namespaces = getContentOfIdentifier(Type.NAMESPACE, parentIdentifier);
        return namespaces.stream().map(Element::new).toList();
    }
}
