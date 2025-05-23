/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.java;

import java.util.List;

import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ElementIdentifier;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.PackageElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.VariableElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.java.JavaClassElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.ElementStorage;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.ElementStorageRegistry;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.commentmatching.CommentMatcher;

/**
 * Registry for storing elements of a Java codebase.
 * Defines the set of element objects that can be stored and provides methods to
 * add and retrieve specific elements.
 */
public class JavaElementStorageRegistry extends ElementStorageRegistry {

    public JavaElementStorageRegistry() {
        super(new CommentMatcher());
    }

    public JavaElementStorageRegistry(List<VariableElement> variables, List<Element> functions, List<JavaClassElement> classes, List<Element> interfaces,
            List<Element> compilationUnits, List<PackageElement> packages) {
        this();
        addElements(Type.VARIABLE, variables);
        addElements(Type.FUNCTION, functions);
        addElements(Type.CLASS, classes);
        addElements(Type.INTERFACE, interfaces);
        addElements(Type.COMPILATIONUNIT, compilationUnits);
        addElements(Type.PACKAGE, packages);
    }

    public JavaElementStorageRegistry(JavaElementStorageRegistry registry) {
        this();
        addVariables(registry.getVariables());
        addFunctions(registry.getFunctions());
        addClasses(registry.getClasses());
        addInterfaces(registry.getInterfaces());
        addCompilationUnits(registry.getCompilationUnits());
        addPackages(registry.getPackages());
    }

    @Override
    protected void registerStorage() {
        registerStorage(Type.VARIABLE, new ElementStorage<>(VariableElement.class));
        registerStorage(Type.FUNCTION, new ElementStorage<>(Element.class));
        registerStorage(Type.CLASS, new ElementStorage<>(JavaClassElement.class));
        registerStorage(Type.INTERFACE, new ElementStorage<>(Element.class));
        registerStorage(Type.COMPILATIONUNIT, new ElementStorage<>(Element.class));
        registerStorage(Type.PACKAGE, new ElementStorage<>(PackageElement.class));
    }

    public void addVariable(VariableElement variable) {
        addElement(Type.VARIABLE, variable);
    }

    public void addFunction(Element function) {
        addElement(Type.FUNCTION, function);
    }

    public void addClass(JavaClassElement clazz) {
        addElement(Type.CLASS, clazz);
    }

    public void addInterface(Element interfaceElement) {
        addElement(Type.INTERFACE, interfaceElement);
    }

    public void addCompilationUnit(Element compilationUnit) {
        addElement(Type.COMPILATIONUNIT, compilationUnit);
    }

    public void addPackage(PackageElement packageElement) {
        addElement(Type.PACKAGE, packageElement);
    }

    public void addVariables(List<VariableElement> variables) {
        addElements(Type.VARIABLE, variables);
    }

    public void addFunctions(List<Element> functions) {
        addElements(Type.FUNCTION, functions);
    }

    public void addClasses(List<JavaClassElement> classes) {
        addElements(Type.CLASS, classes);
    }

    public void addInterfaces(List<Element> interfaces) {
        addElements(Type.INTERFACE, interfaces);
    }

    public void addCompilationUnits(List<Element> compilationUnits) {
        addElements(Type.COMPILATIONUNIT, compilationUnits);
    }

    public void addPackages(List<PackageElement> packages) {
        addElements(Type.PACKAGE, packages);
    }

    public VariableElement getVariable(ElementIdentifier identifier) {
        return new VariableElement(getElement(identifier, VariableElement.class));
    }

    public Element getFunction(ElementIdentifier identifier) {
        return new Element(getElement(identifier, Element.class));
    }

    public JavaClassElement getClass(ElementIdentifier identifier) {
        return new JavaClassElement(getElement(identifier, JavaClassElement.class));
    }

    public Element getInterface(ElementIdentifier identifier) {
        return new Element(getElement(identifier, Element.class));
    }

    public Element getCompilationUnitElement(ElementIdentifier identifier) {
        return new Element(getElement(identifier, Element.class));
    }

    public PackageElement getPackage(ElementIdentifier identifier) {
        return new PackageElement(getElement(identifier, PackageElement.class));
    }

    public List<VariableElement> getVariables() {
        return getElements(Type.VARIABLE, VariableElement.class).stream().map(VariableElement::new).toList();
    }

    public List<Element> getFunctions() {
        return getElements(Type.FUNCTION, Element.class).stream().map(Element::new).toList();
    }

    public List<JavaClassElement> getClasses() {
        return getElements(Type.CLASS, JavaClassElement.class).stream().map(JavaClassElement::new).toList();
    }

    public List<Element> getInterfaces() {
        return getElements(Type.INTERFACE, Element.class).stream().map(Element::new).toList();
    }

    public List<Element> getCompilationUnits() {
        return getElements(Type.COMPILATIONUNIT, Element.class).stream().map(Element::new).toList();
    }

    public List<PackageElement> getPackages() {
        return getElements(Type.PACKAGE, PackageElement.class).stream().map(PackageElement::new).toList();
    }

    public boolean isVariableElement(Element element) {
        return containsElement(Type.VARIABLE, element);
    }

    public boolean isFunctionElement(Element element) {
        return containsElement(Type.FUNCTION, element);
    }

    public boolean isClassElement(Element element) {
        return containsElement(Type.CLASS, element);
    }

    public boolean isInterfaceElement(Element element) {
        return containsElement(Type.INTERFACE, element);
    }

    public boolean isCompilationUnitElement(Element element) {
        return containsElement(Type.COMPILATIONUNIT, element);
    }

    public boolean isPackageElement(Element element) {
        return containsElement(Type.PACKAGE, element);
    }

    public List<VariableElement> getVariablesWithParentIdentifier(ElementIdentifier parentIdentifier) {
        List<VariableElement> variables = getContentOfIdentifier(Type.VARIABLE, parentIdentifier);
        return variables.stream().map(VariableElement::new).toList();
    }

    public List<Element> getFunctionsWithParentIdentifier(ElementIdentifier parentIdentifier) {
        List<Element> functions = getContentOfIdentifier(Type.FUNCTION, parentIdentifier);
        return functions.stream().map(Element::new).toList();
    }

    public List<JavaClassElement> getClassesWithParentIdentifier(ElementIdentifier parentIdentifier) {
        List<JavaClassElement> classes = getContentOfIdentifier(Type.CLASS, parentIdentifier);
        return classes.stream().map(JavaClassElement::new).toList();
    }

    public List<Element> getInterfacesWithParentIdentifier(ElementIdentifier parentIdentifier) {
        List<Element> interfaces = getContentOfIdentifier(Type.INTERFACE, parentIdentifier);
        return interfaces.stream().map(Element::new).toList();
    }

    public List<Element> getCompilationUnitsWithParentIdentifier(ElementIdentifier parentIdentifier) {
        List<Element> compilationUnits = getContentOfIdentifier(Type.COMPILATIONUNIT, parentIdentifier);
        return compilationUnits.stream().map(Element::new).toList();
    }

    public List<PackageElement> getPackagesWithParentIdentifier(ElementIdentifier parentIdentifier) {
        List<PackageElement> packages = getContentOfIdentifier(Type.PACKAGE, parentIdentifier);
        return packages.stream().map(PackageElement::new).toList();
    }
}
