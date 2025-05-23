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
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.python3.Python3ElementStorageRegistry;

class Python3ElementManagerTest {
    private Python3ElementStorageRegistry elementManager;

    @Test
    void addVariablesTest() {
        elementManager = new Python3ElementStorageRegistry();
        List<VariableElement> variables = getCorrectVariablesList();
        elementManager.addVariables(variables);
        Assertions.assertTrue(elementManager.getVariables().containsAll(variables));
    }

    @Test
    void addVariablesTestWithNull() {
        elementManager = new Python3ElementStorageRegistry();
        List<VariableElement> variables = getInCorrectVariablesList();
        elementManager.addVariables(variables);
        Assertions.assertEquals(2, elementManager.getVariables().size());
        Assertions.assertEquals(variables.get(0), elementManager.getVariables().get(0));
        Assertions.assertEquals(variables.get(2), elementManager.getVariables().get(1));
    }

    @Test
    void addFunctionsTest() {
        elementManager = new Python3ElementStorageRegistry();
        List<Element> functions = getCorrectFunctionsList();
        elementManager.addFunctions(functions);
        Assertions.assertTrue(elementManager.getFunctions().containsAll(functions));
    }

    @Test
    void addFunctionsTestWithNull() {
        elementManager = new Python3ElementStorageRegistry();
        List<Element> functions = getIncorrectFunctionsList();
        elementManager.addFunctions(functions);
        Assertions.assertEquals(2, elementManager.getFunctions().size());
        Assertions.assertEquals(functions.get(0), elementManager.getFunctions().get(0));
        Assertions.assertEquals(functions.get(2), elementManager.getFunctions().get(1));
    }

    @Test
    void addClassesTest() {
        elementManager = new Python3ElementStorageRegistry();
        List<ClassElement> classes = getCorrectClassesList();
        elementManager.addClasses(classes);
        Assertions.assertTrue(elementManager.getClasses().containsAll(classes));
    }

    @Test
    void addClassesTestWithNull() {
        elementManager = new Python3ElementStorageRegistry();
        List<ClassElement> classes = getIncorrectClassesList();
        elementManager.addClasses(classes);
        Assertions.assertEquals(2, elementManager.getClasses().size());
        Assertions.assertEquals(classes.get(0), elementManager.getClasses().get(0));
        Assertions.assertEquals(classes.get(2), elementManager.getClasses().get(1));
    }

    @Test
    void getVariableTest() {
        elementManager = new Python3ElementStorageRegistry();
        List<VariableElement> variables = getCorrectVariablesList();
        elementManager.addVariables(variables);
        Assertions.assertTrue(elementManager.getVariables().containsAll(variables));
    }

    @Test
    void getVariableTestWithNull() {
        elementManager = new Python3ElementStorageRegistry();
        List<VariableElement> variables = getInCorrectVariablesList();
        elementManager.addVariables(variables);

        Assertions.assertEquals(2, elementManager.getVariables().size());
        Assertions.assertEquals(variables.get(0), elementManager.getVariables().get(0));
        Assertions.assertEquals(variables.get(2), elementManager.getVariables().get(1));
    }

    @Test
    void getFunctionTest() {
        elementManager = new Python3ElementStorageRegistry();
        List<Element> functions = getCorrectFunctionsList();
        elementManager.addFunctions(functions);
        Assertions.assertTrue(elementManager.getFunctions().containsAll(functions));
    }

    @Test
    void getFunctionTestWithNull() {
        elementManager = new Python3ElementStorageRegistry();
        List<Element> functions = getIncorrectFunctionsList();
        elementManager.addFunctions(functions);

        Assertions.assertEquals(2, elementManager.getFunctions().size());
        Assertions.assertEquals(functions.get(0), elementManager.getFunctions().get(0));
        Assertions.assertEquals(functions.get(2), elementManager.getFunctions().get(1));
    }

    @Test
    void getClassTest() {
        elementManager = new Python3ElementStorageRegistry();
        List<ClassElement> classes = getCorrectClassesList();
        elementManager.addClasses(classes);
        Assertions.assertTrue(elementManager.getClasses().containsAll(classes));
    }

    @Test
    void getClassTestWithNull() {
        elementManager = new Python3ElementStorageRegistry();
        List<ClassElement> classes = getIncorrectClassesList();
        elementManager.addClasses(classes);

        Assertions.assertEquals(2, elementManager.getClasses().size());
        Assertions.assertEquals(classes.get(0), elementManager.getClasses().get(0));
        Assertions.assertEquals(classes.get(2), elementManager.getClasses().get(1));
    }

    @Test
    void getVariablesWithParentSimpleTest() {
        elementManager = new Python3ElementStorageRegistry();
        List<VariableElement> variables = getCorrectVariablesList();
        elementManager.addVariables(variables);
        ElementIdentifier parent = new ElementIdentifier("parentOfVars", "path", Type.FUNCTION);
        List<VariableElement> vars = elementManager.getVariablesWithParentIdentifier(parent);
        Assertions.assertTrue(vars.containsAll(variables));
    }

    @Test
    void getVariablesWithNullParentTest() {
        elementManager = new Python3ElementStorageRegistry();
        List<VariableElement> variables = getInCorrectVariablesList();
        elementManager.addVariables(variables);
        ElementIdentifier parent = new ElementIdentifier("parentOfVars", "path", Type.FUNCTION);
        List<VariableElement> vars = elementManager.getVariablesWithParentIdentifier(parent);
        Assertions.assertTrue(vars.isEmpty());
    }

    @Test
    void getFunctionsWithParentSimpleTest() {
        elementManager = new Python3ElementStorageRegistry();
        List<Element> functions = getCorrectFunctionsList();
        elementManager.addFunctions(functions);
        ElementIdentifier parent = new ElementIdentifier("parentOfFc", "path", Type.CLASS);
        List<Element> funcs = elementManager.getContentOfIdentifier(parent);
        Assertions.assertTrue(funcs.containsAll(functions));
    }

    @Test
    void getFunctionsWithNullParentTest() {
        elementManager = new Python3ElementStorageRegistry();
        List<Element> functions = getIncorrectFunctionsList();
        elementManager.addFunctions(functions);
        ElementIdentifier parent = new ElementIdentifier("parentOfFc", "path", Type.CLASS);
        List<Element> funcs = elementManager.getContentOfIdentifier(parent);
        Assertions.assertTrue(funcs.isEmpty());
    }

    @Test
    void getClassWithParentSimpleTest() {
        elementManager = new Python3ElementStorageRegistry();
        List<ClassElement> classes = getCorrectClassesList();
        elementManager.addClasses(classes);
        ElementIdentifier parent = new ElementIdentifier("parentOfCl", "path", Type.MODULE);
        List<ClassElement> clss = elementManager.getClassesWithParentIdentifier(parent);
        Assertions.assertTrue(clss.containsAll(classes));
    }

    @Test
    void getClassWithNullParentTest() {
        elementManager = new Python3ElementStorageRegistry();
        List<ClassElement> classes = getIncorrectClassesList();
        elementManager.addClasses(classes);
        ElementIdentifier parent = new ElementIdentifier("parentOfCl", "path", Type.MODULE);
        List<ClassElement> clss = elementManager.getClassesWithParentIdentifier(parent);
        Assertions.assertTrue(clss.isEmpty());
    }

    @Test
    void getVariableWrongParentTest() {
        elementManager = new Python3ElementStorageRegistry();
        List<VariableElement> variables = getCorrectVariablesList();
        elementManager.addVariables(variables);
        ElementIdentifier parent = new ElementIdentifier("parentOfVars", "path", Type.CLASS);
        List<VariableElement> vars = elementManager.getVariablesWithParentIdentifier(parent);
        Assertions.assertTrue(vars.isEmpty());
    }

    @Test
    void getFunctioneWithWrongParentTest() {
        elementManager = new Python3ElementStorageRegistry();
        List<Element> functions = getCorrectFunctionsList();
        elementManager.addFunctions(functions);
        ElementIdentifier parent = new ElementIdentifier("parentOfFc", "path", Type.FUNCTION);
        List<Element> funcs = elementManager.getContentOfIdentifier(parent);
        Assertions.assertTrue(funcs.isEmpty());
    }

    @Test
    void getClassWithWrongParentTest() {
        elementManager = new Python3ElementStorageRegistry();
        List<ClassElement> classes = getCorrectClassesList();
        elementManager.addClasses(classes);
        ElementIdentifier parent = new ElementIdentifier("parentOfCl", "path", Type.FUNCTION);
        List<ClassElement> clss = elementManager.getClassesWithParentIdentifier(parent);
        Assertions.assertTrue(clss.isEmpty());
    }

    @Test
    void getVariableWithDifferentParentTest() {
        elementManager = new Python3ElementStorageRegistry();
        List<VariableElement> variables = getCorrectVariablesList();
        VariableElement var = new VariableElement("var4", "path", "string", new ElementIdentifier("diffVarParent", "path", Type.FUNCTION));
        variables.add(var);
        elementManager.addVariables(variables);
        ElementIdentifier parent = new ElementIdentifier("parentOfVars", "path", Type.FUNCTION);
        ElementIdentifier parent2 = new ElementIdentifier("diffVarParent", "path", Type.FUNCTION);
        List<VariableElement> vars = elementManager.getVariablesWithParentIdentifier(parent);
        List<VariableElement> vars2 = elementManager.getVariablesWithParentIdentifier(parent2);

        Assertions.assertEquals(3, vars.size());
        Assertions.assertEquals(1, vars2.size());
        Assertions.assertEquals(variables.get(0), vars.get(0));
        Assertions.assertEquals(variables.get(1), vars.get(1));
        Assertions.assertEquals(variables.get(2), vars.get(2));
        Assertions.assertEquals(variables.get(3), vars2.get(0));
    }

    @Test
    void getFunctionWithDifferentParentTest() {
        elementManager = new Python3ElementStorageRegistry();
        List<Element> functions = getCorrectFunctionsList();
        Type type = Type.FUNCTION;
        Element func = new Element("d", "path", type, new ElementIdentifier("diffFcParent", "path", Type.CLASS));
        functions.add(func);
        elementManager.addFunctions(functions);
        ElementIdentifier parent = new ElementIdentifier("parentOfFc", "path", Type.CLASS);
        ElementIdentifier parent2 = new ElementIdentifier("diffFcParent", "path", Type.CLASS);
        List<Element> funcs = elementManager.getContentOfIdentifier(parent);
        List<Element> funcs2 = elementManager.getContentOfIdentifier(parent2);

        Assertions.assertEquals(3, funcs.size());
        Assertions.assertEquals(1, funcs2.size());
        Assertions.assertEquals(functions.get(0), funcs.get(0));
        Assertions.assertEquals(functions.get(1), funcs.get(1));
        Assertions.assertEquals(functions.get(2), funcs.get(2));
        Assertions.assertEquals(functions.get(3), funcs2.get(0));
    }

    @Test
    void getClassWithDifferentParentTest() {
        elementManager = new Python3ElementStorageRegistry();
        List<ClassElement> classes = getCorrectClassesList();
        ClassElement cl = new ClassElement("d", "path", new ElementIdentifier("diffClParent", "path", Type.MODULE));
        classes.add(cl);
        elementManager.addClasses(classes);
        ElementIdentifier parent = new ElementIdentifier("parentOfCl", "path", Type.MODULE);
        ElementIdentifier parent2 = new ElementIdentifier("diffClParent", "path", Type.MODULE);
        List<ClassElement> clss = elementManager.getClassesWithParentIdentifier(parent);
        List<ClassElement> clss2 = elementManager.getClassesWithParentIdentifier(parent2);

        Assertions.assertEquals(3, clss.size());
        Assertions.assertEquals(1, clss2.size());
        Assertions.assertEquals(classes.get(0), clss.get(0));
        Assertions.assertEquals(classes.get(1), clss.get(1));
        Assertions.assertEquals(classes.get(2), clss.get(2));
        Assertions.assertEquals(classes.get(3), clss2.get(0));
    }

    private List<VariableElement> getCorrectVariablesList() {
        List<VariableElement> variables = new ArrayList<>();
        String path = "path";
        String dataType = "string";
        ElementIdentifier parent = new ElementIdentifier("parentOfVars", "path", Type.FUNCTION);

        variables.add(new VariableElement("var1", path, dataType, parent));
        variables.add(new VariableElement("var2", path, dataType, parent));
        variables.add(new VariableElement("var3", path, dataType, parent));
        return variables;
    }

    private List<VariableElement> getInCorrectVariablesList() {
        List<VariableElement> variables = new ArrayList<>();
        String path = "path";
        String dataType = "string";

        variables.add(new VariableElement("var1", path, dataType, null));
        variables.add(null);
        variables.add(new VariableElement("var3", path, "", null));
        return variables;
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

    private List<Element> getIncorrectFunctionsList() {
        List<Element> functions = new ArrayList<>();
        functions.add(new Element("a", "path", null));
        functions.add(null);
        functions.add(new Element("c", "path", null));
        return functions;
    }

    private List<ClassElement> getCorrectClassesList() {
        List<ClassElement> classes = new ArrayList<>();
        String path = "path";
        ElementIdentifier parent = new ElementIdentifier("parentOfCl", path, Type.MODULE);

        classes.add(new ClassElement("a", path, parent));
        classes.add(new ClassElement("b", path, parent));
        classes.add(new ClassElement("c", path, parent));
        return classes;
    }

    private List<ClassElement> getIncorrectClassesList() {
        List<ClassElement> classes = new ArrayList<>();
        classes.add(new ClassElement("a", "path", null));
        classes.add(null);
        classes.add(new ClassElement("c", "path", null));
        return classes;
    }
}
