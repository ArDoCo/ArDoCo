/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.generators.antlr.elements;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ClassElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ElementIdentifier;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.VariableElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.cpp.CppElementStorageRegistry;

class CppElementManagerTest {
    private CppElementStorageRegistry elementManager;

    @Test
    void addVariablesTest() {
        elementManager = new CppElementStorageRegistry();
        List<VariableElement> variables = getCorrectVariablesList();
        for (VariableElement variable : variables) {
            elementManager.addVariable(variable);
        }

        Assertions.assertEquals(3, elementManager.getVariables().size());
        Assertions.assertEquals(variables.get(0), elementManager.getVariables().get(0));
        Assertions.assertEquals(variables.get(1), elementManager.getVariables().get(1));
        Assertions.assertEquals(variables.get(2), elementManager.getVariables().get(2));
    }

    @Test
    void addVariablesNullTest() {
        elementManager = new CppElementStorageRegistry();
        List<VariableElement> variables = getNullVariables();
        for (VariableElement variable : variables) {
            elementManager.addVariable(variable);
        }

        Assertions.assertEquals(2, elementManager.getVariables().size());
        Assertions.assertEquals(variables.get(0), elementManager.getVariables().get(0));
        Assertions.assertEquals(variables.get(2), elementManager.getVariables().get(1));
    }

    @Test
    void addFunctionsTest() {
        elementManager = new CppElementStorageRegistry();
        List<Element> functions = getCorrectFunctionsList();
        for (Element function : functions) {
            elementManager.addFunction(function);
        }

        Assertions.assertEquals(3, elementManager.getFunctions().size());
        Assertions.assertEquals(functions.get(0), elementManager.getFunctions().get(0));
        Assertions.assertEquals(functions.get(1), elementManager.getFunctions().get(1));
        Assertions.assertEquals(functions.get(2), elementManager.getFunctions().get(2));
    }

    @Test
    void addFunctionsNullTest() {
        elementManager = new CppElementStorageRegistry();
        List<Element> functions = getNullFunctions();
        for (Element function : functions) {
            elementManager.addFunction(function);
        }

        Assertions.assertEquals(2, elementManager.getFunctions().size());
        Assertions.assertEquals(functions.get(0), elementManager.getFunctions().get(0));
        Assertions.assertEquals(functions.get(2), elementManager.getFunctions().get(1));
    }

    @Test
    void addClassesTest() {
        elementManager = new CppElementStorageRegistry();
        List<ClassElement> classes = getCorrectClassesList();
        for (ClassElement clazz : classes) {
            elementManager.addClass(clazz);
        }

        Assertions.assertEquals(3, elementManager.getClasses().size());
        Assertions.assertEquals(classes.get(0), elementManager.getClasses().get(0));
        Assertions.assertEquals(classes.get(1), elementManager.getClasses().get(1));
        Assertions.assertEquals(classes.get(2), elementManager.getClasses().get(2));
    }

    @Test
    void addClassesNullTest() {
        elementManager = new CppElementStorageRegistry();
        List<ClassElement> classes = getNullClasses();
        for (ClassElement clazz : classes) {
            elementManager.addClass(clazz);
        }

        Assertions.assertEquals(2, elementManager.getClasses().size());
        Assertions.assertEquals(classes.get(0), elementManager.getClasses().get(0));
        Assertions.assertEquals(classes.get(2), elementManager.getClasses().get(1));
    }

    @Test
    void addNamespacesTest() {
        elementManager = new CppElementStorageRegistry();
        List<Element> namespaces = getCorrectNamespacesList();
        for (Element namespace : namespaces) {
            elementManager.addNamespace(namespace);
        }

        Assertions.assertEquals(3, elementManager.getNamespaces().size());
        Assertions.assertEquals(namespaces.get(0), elementManager.getNamespaces().get(0));
        Assertions.assertEquals(namespaces.get(1), elementManager.getNamespaces().get(1));
        Assertions.assertEquals(namespaces.get(2), elementManager.getNamespaces().get(2));
    }

    @Test
    void addNamespacesNullTest() {
        elementManager = new CppElementStorageRegistry();
        List<Element> namespaces = getNullNamespaces();
        for (Element namespace : namespaces) {
            elementManager.addNamespace(namespace);
        }

        Assertions.assertEquals(2, elementManager.getNamespaces().size());
        Assertions.assertEquals(namespaces.get(0), elementManager.getNamespaces().get(0));
        Assertions.assertEquals(namespaces.get(2), elementManager.getNamespaces().get(1));
    }

    @Test
    void getVariableTest() {
        elementManager = new CppElementStorageRegistry();
        List<VariableElement> variables = getCorrectVariablesList();
        for (VariableElement variable : variables) {
            elementManager.addVariable(variable);
        }
        ElementIdentifier parent0 = new ElementIdentifier(variables.get(0).getName(), variables.get(0).getPath(), Type.VARIABLE);
        ElementIdentifier parent1 = new ElementIdentifier(variables.get(1).getName(), variables.get(1).getPath(), Type.VARIABLE);
        ElementIdentifier parent2 = new ElementIdentifier(variables.get(2).getName(), variables.get(2).getPath(), Type.VARIABLE);

        Assertions.assertEquals(3, elementManager.getVariables().size());
        Assertions.assertEquals(variables.get(0), elementManager.getVariable(parent0));
        Assertions.assertEquals(variables.get(1), elementManager.getVariable(parent1));
        Assertions.assertEquals(variables.get(2), elementManager.getVariable(parent2));
    }

    @Test
    void getVariableNullTest() {
        elementManager = new CppElementStorageRegistry();
        List<VariableElement> variables = getNullVariables();
        for (VariableElement variable : variables) {
            elementManager.addVariable(variable);
        }
        ElementIdentifier parent0 = new ElementIdentifier(variables.get(0).getName(), variables.get(0).getPath(), Type.VARIABLE);
        ElementIdentifier parent2 = new ElementIdentifier(variables.get(2).getName(), variables.get(2).getPath(), Type.VARIABLE);

        Assertions.assertEquals(2, elementManager.getVariables().size());
        Assertions.assertEquals(variables.get(0), elementManager.getVariable(parent0));
        Assertions.assertEquals(variables.get(2), elementManager.getVariable(parent2));
    }

    @Test
    void getNamespaceTest() {
        elementManager = new CppElementStorageRegistry();
        List<Element> namespaces = getCorrectNamespacesList();
        for (Element namespace : namespaces) {
            elementManager.addNamespace(namespace);
        }

        ElementIdentifier parent0 = new ElementIdentifier(namespaces.get(0).getName(), namespaces.get(0).getPath(), Type.NAMESPACE);
        ElementIdentifier parent1 = new ElementIdentifier(namespaces.get(1).getName(), namespaces.get(1).getPath(), Type.NAMESPACE);
        ElementIdentifier parent2 = new ElementIdentifier(namespaces.get(2).getName(), namespaces.get(2).getPath(), Type.NAMESPACE);

        Assertions.assertEquals(3, elementManager.getNamespaces().size());
        Assertions.assertEquals(namespaces.get(0), elementManager.getNamespace(parent0));
        Assertions.assertEquals(namespaces.get(1), elementManager.getNamespace(parent1));
        Assertions.assertEquals(namespaces.get(2), elementManager.getNamespace(parent2));
    }

    @Test
    void getNamespaceNullTest() {
        elementManager = new CppElementStorageRegistry();
        List<Element> namespaces = getNullNamespaces();
        for (Element namespace : namespaces) {
            elementManager.addNamespace(namespace);
        }

        ElementIdentifier parent0 = new ElementIdentifier(namespaces.get(0).getName(), namespaces.get(0).getPath(), Type.NAMESPACE);
        ElementIdentifier parent2 = new ElementIdentifier(namespaces.get(2).getName(), namespaces.get(2).getPath(), Type.NAMESPACE);

        Assertions.assertEquals(2, elementManager.getNamespaces().size());
        Assertions.assertEquals(namespaces.get(0), elementManager.getNamespace(parent0));
        Assertions.assertEquals(namespaces.get(2), elementManager.getNamespace(parent2));
    }

    @Test
    void getClassTest() {
        elementManager = new CppElementStorageRegistry();
        List<ClassElement> classes = getCorrectClassesList();
        for (ClassElement clazz : classes) {
            elementManager.addClass(clazz);
        }

        ElementIdentifier parent0 = new ElementIdentifier(classes.get(0).getName(), classes.get(0).getPath(), Type.CLASS);
        ElementIdentifier parent1 = new ElementIdentifier(classes.get(1).getName(), classes.get(1).getPath(), Type.CLASS);
        ElementIdentifier parent2 = new ElementIdentifier(classes.get(2).getName(), classes.get(2).getPath(), Type.CLASS);

        Assertions.assertEquals(3, elementManager.getClasses().size());
        Assertions.assertEquals(classes.get(0), elementManager.getClass(parent0));
        Assertions.assertEquals(classes.get(1), elementManager.getClass(parent1));
        Assertions.assertEquals(classes.get(2), elementManager.getClass(parent2));
    }

    @Test
    void getClassNullTest() {
        elementManager = new CppElementStorageRegistry();
        List<ClassElement> classes = getNullClasses();
        for (ClassElement clazz : classes) {
            elementManager.addClass(clazz);
        }

        ElementIdentifier parent0 = new ElementIdentifier(classes.get(0).getName(), classes.get(0).getPath(), Type.CLASS);
        ElementIdentifier parent2 = new ElementIdentifier(classes.get(2).getName(), classes.get(2).getPath(), Type.CLASS);

        Assertions.assertEquals(2, elementManager.getClasses().size());
        Assertions.assertEquals(classes.get(0), elementManager.getClass(parent0));
        Assertions.assertEquals(classes.get(2), elementManager.getClass(parent2));
    }

    @Test
    void getFunctionTest() {
        elementManager = new CppElementStorageRegistry();
        List<Element> functions = getCorrectFunctionsList();
        for (Element function : functions) {
            elementManager.addFunction(function);
        }

        ElementIdentifier parent0 = new ElementIdentifier(functions.get(0).getName(), functions.get(0).getPath(), Type.FUNCTION);
        ElementIdentifier parent1 = new ElementIdentifier(functions.get(1).getName(), functions.get(1).getPath(), Type.FUNCTION);
        ElementIdentifier parent2 = new ElementIdentifier(functions.get(2).getName(), functions.get(2).getPath(), Type.FUNCTION);

        Assertions.assertEquals(3, elementManager.getFunctions().size());
        Assertions.assertEquals(functions.get(0), elementManager.getFunction(parent0));
        Assertions.assertEquals(functions.get(1), elementManager.getFunction(parent1));
        Assertions.assertEquals(functions.get(2), elementManager.getFunction(parent2));
    }

    @Test
    void getFunctionNullTest() {
        elementManager = new CppElementStorageRegistry();
        List<Element> functions = getNullFunctions();
        for (Element function : functions) {
            elementManager.addFunction(function);
        }

        ElementIdentifier parent0 = new ElementIdentifier(functions.get(0).getName(), functions.get(0).getPath(), Type.FUNCTION);
        ElementIdentifier parent2 = new ElementIdentifier(functions.get(2).getName(), functions.get(2).getPath(), Type.FUNCTION);

        Assertions.assertEquals(2, elementManager.getFunctions().size());
        Assertions.assertEquals(functions.get(0), elementManager.getFunction(parent0));
        Assertions.assertEquals(functions.get(2), elementManager.getFunction(parent2));
    }

    @Test
    void getVariablesWithParentSimpleTest() {
        elementManager = new CppElementStorageRegistry();
        List<VariableElement> variables = getCorrectVariablesList();
        for (VariableElement variable : variables) {
            elementManager.addVariable(variable);
        }

        ElementIdentifier parentOfVars = new ElementIdentifier("parentOfVars", "path", Type.FUNCTION);

        List<VariableElement> elements = elementManager.getVariablesWithParentIdentifier(parentOfVars);

        Assertions.assertEquals(3, elements.size());
        Assertions.assertEquals(variables.get(0), elements.get(0));
        Assertions.assertEquals(variables.get(1), elements.get(1));
        Assertions.assertEquals(variables.get(2), elements.get(2));
    }

    @Test
    void getVariablesWithParentNullTest() {
        elementManager = new CppElementStorageRegistry();
        List<VariableElement> variables = getNullVariables();
        for (VariableElement variable : variables) {
            elementManager.addVariable(variable);
        }

        ElementIdentifier parentOfVars = new ElementIdentifier("parentOfVars", "path", Type.FUNCTION);

        List<VariableElement> elements = elementManager.getVariablesWithParentIdentifier(parentOfVars);

        Assertions.assertEquals(0, elements.size());
    }

    @Test
    void getVariablesWrongParentTest() {
        elementManager = new CppElementStorageRegistry();
        List<VariableElement> variables = getCorrectVariablesList();
        for (VariableElement variable : variables) {
            elementManager.addVariable(variable);
        }

        ElementIdentifier parentOfVars = new ElementIdentifier("wrongParent", "path", Type.FUNCTION);

        List<VariableElement> elements = elementManager.getVariablesWithParentIdentifier(parentOfVars);

        Assertions.assertEquals(0, elements.size());
    }

    @Test
    void getVariablesWithDifferentParentsTest() {
        elementManager = new CppElementStorageRegistry();
        List<VariableElement> variables = getCorrectVariablesList();
        VariableElement diffVar = new VariableElement("diffVar", "path", "int", new ElementIdentifier("diffVarParent", "path", Type.FUNCTION));
        variables.add(diffVar);
        for (VariableElement variable : variables) {
            elementManager.addVariable(variable);
        }

        ElementIdentifier parentOfVars = new ElementIdentifier("parentOfVars", "path", Type.FUNCTION);
        ElementIdentifier parentOfVars2 = new ElementIdentifier("diffVarParent", "path", Type.FUNCTION);

        List<VariableElement> elements = elementManager.getVariablesWithParentIdentifier(parentOfVars);
        List<VariableElement> elements2 = elementManager.getVariablesWithParentIdentifier(parentOfVars2);

        Assertions.assertEquals(3, elements.size());
        Assertions.assertEquals(1, elements2.size());
        Assertions.assertEquals(variables.get(0), elements.get(0));
        Assertions.assertEquals(variables.get(1), elements.get(1));
        Assertions.assertEquals(variables.get(2), elements.get(2));
        Assertions.assertEquals(diffVar, elements2.get(0));
    }

    @Test
    void getFunctionsWithParentSimpleTest() {
        elementManager = new CppElementStorageRegistry();
        List<Element> functions = getCorrectFunctionsList();
        for (Element function : functions) {
            elementManager.addFunction(function);
        }

        ElementIdentifier parentOfFc = new ElementIdentifier("parentOfFc", "path", Type.CLASS);

        List<Element> elements = elementManager.getFunctionsWithParentIdentifier(parentOfFc);

        Assertions.assertEquals(3, elements.size());
        Assertions.assertEquals(functions.get(0), elements.get(0));
        Assertions.assertEquals(functions.get(1), elements.get(1));
        Assertions.assertEquals(functions.get(2), elements.get(2));
    }

    @Test
    void getFunctionsWithParentNullTest() {
        elementManager = new CppElementStorageRegistry();
        List<Element> functions = getNullFunctions();
        for (Element function : functions) {
            elementManager.addFunction(function);
        }

        ElementIdentifier parentOfFc = new ElementIdentifier("parentOfFc", "path", Type.CLASS);

        List<Element> elements = elementManager.getFunctionsWithParentIdentifier(parentOfFc);

        Assertions.assertEquals(0, elements.size());
    }

    @Test
    void getFunctionsWithDifferentParentsTest() {
        elementManager = new CppElementStorageRegistry();
        List<Element> functions = getCorrectFunctionsList();
        Element diffFc = new Element("diffFc", "path", Type.FUNCTION, new ElementIdentifier("diffFcParent", "path", Type.CLASS));
        functions.add(diffFc);
        for (Element function : functions) {
            elementManager.addFunction(function);
        }

        ElementIdentifier parentOfFc = new ElementIdentifier("parentOfFc", "path", Type.CLASS);
        ElementIdentifier parentOfFc2 = new ElementIdentifier("diffFcParent", "path", Type.CLASS);

        List<Element> elements = elementManager.getFunctionsWithParentIdentifier(parentOfFc);
        List<Element> elements2 = elementManager.getFunctionsWithParentIdentifier(parentOfFc2);

        Assertions.assertEquals(3, elements.size());
        Assertions.assertEquals(1, elements2.size());
        Assertions.assertEquals(functions.get(0), elements.get(0));
        Assertions.assertEquals(functions.get(1), elements.get(1));
        Assertions.assertEquals(functions.get(2), elements.get(2));
        Assertions.assertEquals(diffFc, elements2.get(0));
    }

    @Test
    void getClassWithParentSimpleTest() {
        elementManager = new CppElementStorageRegistry();
        List<ClassElement> classes = getCorrectClassesList();
        for (ClassElement clazz : classes) {
            elementManager.addClass(clazz);
        }

        ElementIdentifier parentOfCs = new ElementIdentifier("parentOfCs", "path", Type.NAMESPACE);

        List<ClassElement> elements = elementManager.getClassesWithParentIdentifier(parentOfCs);

        Assertions.assertEquals(3, elements.size());
        Assertions.assertEquals(classes.get(0), elements.get(0));
        Assertions.assertEquals(classes.get(1), elements.get(1));
        Assertions.assertEquals(classes.get(2), elements.get(2));
    }

    @Test
    void getClassWithParentNullTest() {
        elementManager = new CppElementStorageRegistry();
        List<ClassElement> classes = getNullClasses();
        for (ClassElement clazz : classes) {
            elementManager.addClass(clazz);
        }

        ElementIdentifier parentOfCs = new ElementIdentifier("parentOfCs", "path", Type.NAMESPACE);

        List<ClassElement> elements = elementManager.getClassesWithParentIdentifier(parentOfCs);

        Assertions.assertEquals(0, elements.size());
    }

    @Test
    void getClassWithDifferentParentsTest() {
        elementManager = new CppElementStorageRegistry();
        List<ClassElement> classes = getCorrectClassesList();
        ClassElement diffCs = new ClassElement("diffCs", "path", new ElementIdentifier("diffCsParent", "path", Type.NAMESPACE));
        classes.add(diffCs);
        for (ClassElement clazz : classes) {
            elementManager.addClass(clazz);
        }

        ElementIdentifier parentOfCs = new ElementIdentifier("parentOfCs", "path", Type.NAMESPACE);
        ElementIdentifier parentOfCs2 = new ElementIdentifier("diffCsParent", "path", Type.NAMESPACE);

        List<ClassElement> elements = elementManager.getClassesWithParentIdentifier(parentOfCs);
        List<ClassElement> elements2 = elementManager.getClassesWithParentIdentifier(parentOfCs2);

        Assertions.assertEquals(3, elements.size());
        Assertions.assertEquals(1, elements2.size());
        Assertions.assertEquals(classes.get(0), elements.get(0));
        Assertions.assertEquals(classes.get(1), elements.get(1));
        Assertions.assertEquals(classes.get(2), elements.get(2));
        Assertions.assertEquals(diffCs, elements2.get(0));
    }

    @Test
    void getNamespaceWithParentSimpleTest() {
        elementManager = new CppElementStorageRegistry();
        List<Element> namespaces = getCorrectNamespacesList();
        for (Element namespace : namespaces) {
            elementManager.addNamespace(namespace);
        }

        ElementIdentifier parentOfNs = new ElementIdentifier("parentOfNs", "path", Type.FILE);

        List<Element> elements = elementManager.getNamespacesWithParentIdentifier(parentOfNs);

        Assertions.assertEquals(3, elements.size());
        Assertions.assertEquals(namespaces.get(0), elements.get(0));
        Assertions.assertEquals(namespaces.get(1), elements.get(1));
        Assertions.assertEquals(namespaces.get(2), elements.get(2));
    }

    @Test
    void getNamespaceWithParentNullTest() {
        elementManager = new CppElementStorageRegistry();
        List<Element> namespaces = getNullNamespaces();
        for (Element namespace : namespaces) {
            elementManager.addNamespace(namespace);
        }

        ElementIdentifier parentOfNs = new ElementIdentifier("parentOfNs", "path", Type.FILE);

        List<Element> elements = elementManager.getNamespacesWithParentIdentifier(parentOfNs);

        Assertions.assertEquals(0, elements.size());
    }

    @Test
    void getNamespaceWithDifferentParentsTest() {
        elementManager = new CppElementStorageRegistry();
        List<Element> namespaces = getCorrectNamespacesList();
        Element diffNs = new Element("diffNs", "path", Type.NAMESPACE, new ElementIdentifier("diffNsParent", "path", Type.FILE));
        namespaces.add(diffNs);
        for (Element namespace : namespaces) {
            elementManager.addNamespace(namespace);
        }

        ElementIdentifier parentOfNs = new ElementIdentifier("parentOfNs", "path", Type.FILE);
        ElementIdentifier parentOfNs2 = new ElementIdentifier("diffNsParent", "path", Type.FILE);

        List<Element> elements = elementManager.getNamespacesWithParentIdentifier(parentOfNs);
        List<Element> elements2 = elementManager.getNamespacesWithParentIdentifier(parentOfNs2);

        Assertions.assertEquals(3, elements.size());
        Assertions.assertEquals(1, elements2.size());
        Assertions.assertEquals(namespaces.get(0), elements.get(0));
        Assertions.assertEquals(namespaces.get(1), elements.get(1));
        Assertions.assertEquals(namespaces.get(2), elements.get(2));
        Assertions.assertEquals(diffNs, elements2.get(0));
    }

    private List<Element> getCorrectFunctionsList() {
        List<Element> functions = new ArrayList<>();
        String path = "path";
        Type type = Type.FUNCTION;
        ElementIdentifier parent = new ElementIdentifier("parentOfFc", path, Type.CLASS);

        functions.add(new Element("a", path, type, parent));
        functions.add(new Element("b", path, type, parent));
        functions.add(new Element("c", path, type, parent));
        return functions;
    }

    private List<Element> getNullFunctions() {
        List<Element> functions = new ArrayList<>();
        functions.add(new Element("a", "path", Type.FUNCTION));
        functions.add(null);
        functions.add(new Element("c", "path", Type.FUNCTION));
        return functions;
    }

    private List<ClassElement> getCorrectClassesList() {
        List<ClassElement> classes = new ArrayList<>();
        String path = "path";
        ElementIdentifier parent = new ElementIdentifier("parentOfCs", path, Type.NAMESPACE);

        classes.add(new ClassElement("a", path, parent));
        classes.add(new ClassElement("b", path, parent));
        classes.add(new ClassElement("c", path, parent));
        return classes;
    }

    private List<ClassElement> getNullClasses() {
        List<ClassElement> classes = new ArrayList<>();
        classes.add(new ClassElement("a", "path", null));
        classes.add(null);
        classes.add(new ClassElement("c", "path", null));
        return classes;
    }

    private List<Element> getCorrectNamespacesList() {
        List<Element> namespaces = new ArrayList<>();
        String path = "path";
        Type type = Type.NAMESPACE;
        ElementIdentifier parent = new ElementIdentifier("parentOfNs", path, Type.FILE);

        namespaces.add(new Element("a", path, type, parent));
        namespaces.add(new Element("b", path, type, parent));
        namespaces.add(new Element("c", path, type, parent));
        return namespaces;
    }

    private List<Element> getNullNamespaces() {
        List<Element> namespaces = new ArrayList<>();
        namespaces.add(new Element("a", "path", Type.NAMESPACE));
        namespaces.add(null);
        namespaces.add(new Element("c", "path", Type.NAMESPACE));
        return namespaces;
    }

    private List<VariableElement> getCorrectVariablesList() {
        List<VariableElement> variables = new ArrayList<>();
        String path = "path";
        String dataType = "int";
        ElementIdentifier parent = new ElementIdentifier("parentOfVars", "path", Type.FUNCTION);
        variables.add(new VariableElement("a", path, dataType, parent));
        variables.add(new VariableElement("b", path, dataType, parent));
        variables.add(new VariableElement("c", path, dataType, parent));
        return variables;
    }

    private List<VariableElement> getNullVariables() {
        List<VariableElement> variables = new ArrayList<>();
        variables.add(new VariableElement("a", "path", "string", null));
        variables.add(null);
        variables.add(new VariableElement("c", "path", "int", null));
        return variables;
    }
}
