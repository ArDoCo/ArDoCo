/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.generators.antlr.elements;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ElementIdentifier;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.PackageElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.VariableElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.java.JavaClassElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.java.JavaElementStorageRegistry;

class JavaElementManagerTest {
    private JavaElementStorageRegistry elementManager;

    @Test
    void addVariableTest() {
        elementManager = new JavaElementStorageRegistry();
        List<VariableElement> variables = getCorrectVariablesList();
        for (VariableElement variable : variables) {
            elementManager.addVariable(variable);
        }
        Assertions.assertTrue(elementManager.getVariables().containsAll(variables));
    }

    @Test
    void addVariablesNullTest() {
        elementManager = new JavaElementStorageRegistry();
        List<VariableElement> variables = getIncorrectVariablesList();
        for (VariableElement variable : variables) {
            elementManager.addVariable(variable);
        }
        Assertions.assertEquals(2, elementManager.getVariables().size());
        Assertions.assertEquals(variables.get(0), elementManager.getVariables().get(0));
        Assertions.assertEquals(variables.get(2), elementManager.getVariables().get(1));
    }

    @Test
    void addFunctionTest() {
        elementManager = new JavaElementStorageRegistry();
        List<Element> functions = getCorrectFunctionsList();
        for (Element function : functions) {
            elementManager.addFunction(function);
        }
        Assertions.assertTrue(elementManager.getFunctions().containsAll(functions));
    }

    @Test
    void addFunctionNullTest() {
        elementManager = new JavaElementStorageRegistry();
        List<Element> functions = getIncorrectFunctionsList();
        for (Element function : functions) {
            elementManager.addFunction(function);
        }
        Assertions.assertEquals(2, elementManager.getFunctions().size());
        Assertions.assertEquals(functions.get(0), elementManager.getFunctions().get(0));
        Assertions.assertEquals(functions.get(2), elementManager.getFunctions().get(1));
    }

    @Test
    void addClassTest() {
        elementManager = new JavaElementStorageRegistry();
        List<JavaClassElement> classes = getCorrectClassList();
        for (JavaClassElement clazz : classes) {
            elementManager.addClass(clazz);
        }
        Assertions.assertTrue(elementManager.getClasses().containsAll(classes));
    }

    @Test
    void addClassNullTest() {
        elementManager = new JavaElementStorageRegistry();
        List<JavaClassElement> classes = getIncorrectClassList();
        for (JavaClassElement clazz : classes) {
            elementManager.addClass(clazz);
        }
        Assertions.assertEquals(2, elementManager.getClasses().size());
        Assertions.assertEquals(classes.get(0), elementManager.getClasses().get(0));
        Assertions.assertEquals(classes.get(2), elementManager.getClasses().get(1));
    }

    @Test
    void addInterfaceTest() {
        elementManager = new JavaElementStorageRegistry();
        List<Element> interfaces = getCorrectInterfaceList();
        for (Element interf : interfaces) {
            elementManager.addInterface(interf);
        }
        Assertions.assertTrue(elementManager.getInterfaces().containsAll(interfaces));
    }

    @Test
    void addInterfaceNullTest() {
        elementManager = new JavaElementStorageRegistry();
        List<Element> interfaces = getIncorrectInterfaceList();
        for (Element interf : interfaces) {
            elementManager.addInterface(interf);
        }
        Assertions.assertEquals(2, elementManager.getInterfaces().size());
        Assertions.assertEquals(interfaces.get(0), elementManager.getInterfaces().get(0));
        Assertions.assertEquals(interfaces.get(2), elementManager.getInterfaces().get(1));
    }

    @Test
    void addCompilationUnitTest() {
        elementManager = new JavaElementStorageRegistry();
        List<Element> compilationUnits = getCorrectCompilationUnitList();
        for (Element compilationUnit : compilationUnits) {
            elementManager.addCompilationUnit(compilationUnit);
        }
        Assertions.assertTrue(elementManager.getCompilationUnits().containsAll(compilationUnits));
    }

    @Test
    void addCompilationUnitNullTest() {
        elementManager = new JavaElementStorageRegistry();
        List<Element> compilationUnits = getIncorrectCompilationUnitList();
        for (Element compilationUnit : compilationUnits) {
            elementManager.addCompilationUnit(compilationUnit);
        }
        Assertions.assertEquals(2, elementManager.getCompilationUnits().size());
        Assertions.assertEquals(compilationUnits.get(0), elementManager.getCompilationUnits().get(0));
        Assertions.assertEquals(compilationUnits.get(2), elementManager.getCompilationUnits().get(1));
    }

    @Test
    void addPackageTest() {
        elementManager = new JavaElementStorageRegistry();
        List<PackageElement> packages = getCorrectPackageList();
        for (PackageElement pack : packages) {
            elementManager.addPackage(pack);
        }
        Assertions.assertTrue(elementManager.getPackages().containsAll(packages));
    }

    @Test
    void addPackageNullTest() {
        elementManager = new JavaElementStorageRegistry();
        List<PackageElement> packages = getIncorrectPackageList();
        for (PackageElement pack : packages) {
            elementManager.addPackage(pack);
        }
        Assertions.assertEquals(2, elementManager.getPackages().size());
        Assertions.assertEquals(packages.get(0), elementManager.getPackages().get(0));
        Assertions.assertEquals(packages.get(2), elementManager.getPackages().get(1));
    }

    @Test
    void getVariableTest() {
        elementManager = new JavaElementStorageRegistry();
        List<VariableElement> variables = getCorrectVariablesList();
        for (VariableElement variable : variables) {
            elementManager.addVariable(variable);
        }
        Assertions.assertTrue(elementManager.getVariables().containsAll(variables));
    }

    @Test
    void getVariableNullTest() {
        elementManager = new JavaElementStorageRegistry();
        List<VariableElement> variables = getIncorrectVariablesList();
        for (VariableElement variable : variables) {
            elementManager.addVariable(variable);
        }
        Assertions.assertEquals(variables.get(0), elementManager.getVariables().get(0));
        Assertions.assertEquals(variables.get(2), elementManager.getVariables().get(1));
    }

    @Test
    void getFunctionTest() {
        elementManager = new JavaElementStorageRegistry();
        List<Element> functions = getCorrectFunctionsList();
        for (Element function : functions) {
            elementManager.addFunction(function);
        }
        Assertions.assertTrue(elementManager.getFunctions().containsAll(functions));
    }

    @Test
    void getFunctionNullTest() {
        elementManager = new JavaElementStorageRegistry();
        List<Element> functions = getIncorrectFunctionsList();
        for (Element function : functions) {
            elementManager.addFunction(function);
        }
        Assertions.assertEquals(functions.get(0), elementManager.getFunctions().get(0));
        Assertions.assertEquals(functions.get(2), elementManager.getFunctions().get(1));
    }

    @Test
    void getClassTest() {
        elementManager = new JavaElementStorageRegistry();
        List<JavaClassElement> classes = getCorrectClassList();
        for (JavaClassElement clazz : classes) {
            elementManager.addClass(clazz);
        }
        Assertions.assertTrue(elementManager.getClasses().containsAll(classes));
    }

    @Test
    void getClassNullTest() {
        elementManager = new JavaElementStorageRegistry();
        List<JavaClassElement> classes = getIncorrectClassList();
        for (JavaClassElement clazz : classes) {
            elementManager.addClass(clazz);
        }
        Assertions.assertEquals(classes.get(0), elementManager.getClasses().get(0));
        Assertions.assertEquals(classes.get(2), elementManager.getClasses().get(1));
    }

    @Test
    void getInterfaceTest() {
        elementManager = new JavaElementStorageRegistry();
        List<Element> interfaces = getCorrectInterfaceList();
        for (Element interf : interfaces) {
            elementManager.addInterface(interf);
        }
        Assertions.assertTrue(elementManager.getInterfaces().containsAll(interfaces));
    }

    @Test
    void getInterfaceNullTest() {
        elementManager = new JavaElementStorageRegistry();
        List<Element> interfaces = getIncorrectInterfaceList();
        for (Element interf : interfaces) {
            elementManager.addInterface(interf);
        }
        Assertions.assertEquals(interfaces.get(0), elementManager.getInterfaces().get(0));
        Assertions.assertEquals(interfaces.get(2), elementManager.getInterfaces().get(1));
    }

    @Test
    void getCompilationUnitTest() {
        elementManager = new JavaElementStorageRegistry();
        List<Element> compilationUnits = getCorrectCompilationUnitList();
        for (Element compilationUnit : compilationUnits) {
            elementManager.addCompilationUnit(compilationUnit);
        }
        Assertions.assertTrue(elementManager.getCompilationUnits().containsAll(compilationUnits));
    }

    @Test
    void getCompilationUnitNullTest() {
        elementManager = new JavaElementStorageRegistry();
        List<Element> compilationUnits = getIncorrectCompilationUnitList();
        for (Element compilationUnit : compilationUnits) {
            elementManager.addCompilationUnit(compilationUnit);
        }
        Assertions.assertEquals(compilationUnits.get(0), elementManager.getCompilationUnits().get(0));
        Assertions.assertEquals(compilationUnits.get(2), elementManager.getCompilationUnits().get(1));
    }

    @Test
    void getPackageTest() {
        elementManager = new JavaElementStorageRegistry();
        List<PackageElement> packages = getCorrectPackageList();
        for (PackageElement pack : packages) {
            elementManager.addPackage(pack);
        }
        Assertions.assertTrue(elementManager.getPackages().containsAll(packages));
    }

    @Test
    void getPackageNullTest() {
        elementManager = new JavaElementStorageRegistry();
        List<PackageElement> packages = getIncorrectPackageList();
        for (PackageElement pack : packages) {
            elementManager.addPackage(pack);
        }
        Assertions.assertEquals(packages.get(0), elementManager.getPackages().get(0));
        Assertions.assertEquals(packages.get(2), elementManager.getPackages().get(1));
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

    private List<VariableElement> getIncorrectVariablesList() {
        List<VariableElement> variables = new ArrayList<>();
        variables.add(new VariableElement("a", "path", "string", null));
        variables.add(null);
        variables.add(new VariableElement("c", "path", "int", null));
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

    private List<JavaClassElement> getCorrectClassList() {
        List<JavaClassElement> classes = new ArrayList<>();
        String path = "path";
        ElementIdentifier parent = new ElementIdentifier("parentOfCl", path, Type.COMPILATIONUNIT);
        classes.add(new JavaClassElement("a", path, parent, 0, 0));
        classes.add(new JavaClassElement("b", path, parent, 0, 0));
        classes.add(new JavaClassElement("c", path, parent, 0, 0));
        return classes;
    }

    private List<JavaClassElement> getIncorrectClassList() {
        List<JavaClassElement> classes = new ArrayList<>();
        classes.add(new JavaClassElement("a", "path", null, 0, 0));
        classes.add(null);
        classes.add(new JavaClassElement("c", "path", null, 0, 0));
        return classes;
    }

    private List<Element> getCorrectInterfaceList() {
        List<Element> interfaces = new ArrayList<>();
        String path = "path";
        Type type = Type.INTERFACE;
        ElementIdentifier parent = new ElementIdentifier("parentOfIn", path, Type.COMPILATIONUNIT);
        interfaces.add(new Element("a", path, type, parent));
        interfaces.add(new Element("b", path, type, parent));
        interfaces.add(new Element("c", path, type, parent));
        return interfaces;
    }

    private List<Element> getIncorrectInterfaceList() {
        List<Element> interfaces = new ArrayList<>();
        interfaces.add(new Element("a", "path", null));
        interfaces.add(null);
        interfaces.add(new Element("c", "path", null));
        return interfaces;
    }

    private List<Element> getCorrectCompilationUnitList() {
        List<Element> compilationUnits = new ArrayList<>();
        String path = "path";
        Type type = Type.COMPILATIONUNIT;
        ElementIdentifier parent = new ElementIdentifier("parentOfCu", path, Type.PACKAGE);
        compilationUnits.add(new Element("a", path, type, parent));
        compilationUnits.add(new Element("b", path, type, parent));
        compilationUnits.add(new Element("c", path, type, parent));
        return compilationUnits;
    }

    private List<Element> getIncorrectCompilationUnitList() {
        List<Element> compilationUnits = new ArrayList<>();
        compilationUnits.add(new Element("a", "path", null));
        compilationUnits.add(null);
        compilationUnits.add(new Element("c", "path", null));
        return compilationUnits;
    }

    private List<PackageElement> getCorrectPackageList() {
        List<PackageElement> packages = new ArrayList<>();
        String path = "path";
        packages.add(new PackageElement("a", path));
        packages.add(new PackageElement("b", path));
        packages.add(new PackageElement("c", path));
        return packages;
    }

    private List<PackageElement> getIncorrectPackageList() {
        List<PackageElement> packages = new ArrayList<>();
        packages.add(new PackageElement("a", "path"));
        packages.add(null);
        packages.add(new PackageElement("c", "path/c"));
        return packages;
    }

}
