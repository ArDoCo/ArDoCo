/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.python3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import edu.kit.kastel.mcse.ardoco.tlr.models.antlr4.python3.Python3Lexer;
import edu.kit.kastel.mcse.ardoco.tlr.models.antlr4.python3.Python3Parser;
import edu.kit.kastel.mcse.ardoco.tlr.models.antlr4.python3.Python3Parser.File_inputContext;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ClassElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Element;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.ElementIdentifier;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.PackageElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.Type;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.elements.VariableElement;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.ElementExtractor;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.extraction.PathExtractor;
import edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators.antlr.management.python3.Python3ElementStorageRegistry;

/**
 * Responsible for extracting structural elements from Python3 files. The
 * extracted elements are stored in a Python3ElementStorageRegistry.
 * The extraction process is done by building a token stream from a file via
 * ANTLR and extracting the elements from the token stream.
 * The files are identified by their suffix in the directory.
 */
public class Python3ElementExtractor extends ElementExtractor {
    private final Python3ElementStorageRegistry elementRegistry;

    public Python3ElementExtractor() {
        super();
        this.elementRegistry = new Python3ElementStorageRegistry();
        this.commentExtractor = new Python3CommentExtractor(elementRegistry);
    }

    public Python3ElementExtractor(Python3ElementStorageRegistry elementRegistry) {
        super();
        this.elementRegistry = elementRegistry;
        this.commentExtractor = new Python3CommentExtractor(elementRegistry);
    }

    @Override
    public Python3ElementStorageRegistry getElements() {
        return new Python3ElementStorageRegistry(elementRegistry);
    }

    @Override
    protected List<Path> getFiles(String directoryPath) {
        Path dir = Path.of(directoryPath);
        List<Path> pythonFiles = new ArrayList<>();
        try {
            Files.walk(dir).filter(Files::isRegularFile).filter(f -> f.toString().endsWith(".py")).forEach(pythonFiles::add);
        } catch (IOException e) {
            logger.error("I/O operation failed", e);
        }
        return pythonFiles;
    }

    @Override
    protected CommonTokenStream buildTokens(Path file) throws IOException {
        CharStream charStream = CharStreams.fromPath(file);
        Python3Lexer lexer = new Python3Lexer(charStream);
        return new CommonTokenStream(lexer);
    }

    @Override
    public void extractElements(CommonTokenStream tokens) {
        File_inputContext ctx = buildContext(tokens);
        visitFile_input(ctx);
        addModules(ctx);
    }

    private File_inputContext buildContext(CommonTokenStream tokenStream) {
        Python3Parser parser = new Python3Parser(tokenStream);
        return parser.file_input();
    }

    public void visitFile_input(Python3Parser.File_inputContext ctx) {
        ElementIdentifier parentIdentifier = new ElementIdentifier(PathExtractor.extractNameFromPath(ctx), PathExtractor.extractPath(ctx), Type.MODULE);
        if (ctx.stmt() != null) {
            for (Python3Parser.StmtContext stmt : ctx.stmt()) {
                visitStmt(stmt, parentIdentifier);
            }
        }
    }

    public void visitStmt(Python3Parser.StmtContext ctx, ElementIdentifier parentIdentifier) {
        if (ctx.simple_stmts() != null) {
            visitSimple_stmts(ctx.simple_stmts(), parentIdentifier);
        } else if (ctx.compound_stmt() != null) {
            visitCompound_stmt(ctx.compound_stmt(), parentIdentifier);
        }
    }

    public void visitSimple_stmts(Python3Parser.Simple_stmtsContext ctx, ElementIdentifier parentIdentifier) {
        for (Python3Parser.Simple_stmtContext simpleStmt : ctx.simple_stmt()) {
            visitSimple_stmt(simpleStmt, parentIdentifier);
        }
    }

    public void visitCompound_stmt(Python3Parser.Compound_stmtContext ctx, ElementIdentifier parentIdentifier) {
        if (ctx.funcdef() != null) {
            visitFuncdef(ctx.funcdef(), parentIdentifier);
        } else if (ctx.classdef() != null) {
            visitClassdef(ctx.classdef(), parentIdentifier);
        } else if (ctx.decorated() != null) {
            if (ctx.decorated().classdef() != null) {
                visitClassdef(ctx.decorated().classdef(), parentIdentifier);
            } else if (ctx.decorated().funcdef() != null) {
                visitFuncdef(ctx.decorated().funcdef(), parentIdentifier);
            }
        }
    }

    public ElementIdentifier visitClassdef(Python3Parser.ClassdefContext ctx, ElementIdentifier parentIdentifier) {
        if (ctx.name() == null) {
            return null;
        }
        String name = ctx.name().getText();
        String path = PathExtractor.extractPath(ctx);
        List<String> childClassOf = getParentClasses(ctx);
        ElementIdentifier identifier = new ElementIdentifier(name, path, Type.CLASS);
        int startLine = ctx.getStart().getLine();
        int endLine = ctx.getStop().getLine();

        if (ctx.block() != null && ctx.block().stmt() != null) {
            for (Python3Parser.StmtContext stmt : ctx.block().stmt()) {
                visitStmt(stmt, identifier);
            }
        }

        addClassElement(name, path, parentIdentifier, childClassOf, startLine, endLine);
        return identifier;
    }

    public ElementIdentifier visitFuncdef(Python3Parser.FuncdefContext ctx, ElementIdentifier parentIdentifier) {
        if (ctx.name() == null) {
            return null;
        }
        String name = ctx.name().getText();
        String path = PathExtractor.extractPath(ctx);
        ElementIdentifier identifier = new ElementIdentifier(name, path, Type.FUNCTION);
        int startLine = ctx.getStart().getLine();
        int endLine = ctx.getStop().getLine();

        if (ctx.block() != null && ctx.block().stmt() != null) {
            for (Python3Parser.StmtContext stmt : ctx.block().stmt()) {
                visitStmt(stmt, identifier);
            }
        }
        addFunctionElement(name, path, parentIdentifier, startLine, endLine);
        return identifier;
    }

    public void visitSimple_stmt(Python3Parser.Simple_stmtContext ctx, ElementIdentifier parentIdentifier) {
        if (ctx.expr_stmt() != null) {
            visitExpr_stmt(ctx.expr_stmt(), parentIdentifier);
        }
    }

    public void visitExpr_stmt(Python3Parser.Expr_stmtContext ctx, ElementIdentifier parentIdentifier) {
        if (ctx.ASSIGN() != null && ctx.testlist_star_expr().size() > 1) {
            extractVariablesFromExprStmt(ctx, parentIdentifier);
        }
    }

    private List<String> getParentClasses(Python3Parser.ClassdefContext ctx) {
        List<String> parentClasses = new ArrayList<>();
        if (ctx.arglist() != null) {
            for (Python3Parser.ArgumentContext arg : ctx.arglist().argument()) {
                parentClasses.add(arg.getText());
            }
        }
        return parentClasses;
    }

    private void extractVariablesFromExprStmt(Python3Parser.Expr_stmtContext ctx, ElementIdentifier parentIdentifier) {
        if (ctx.testlist_star_expr().size() < 2) {
            return;
        }
        List<String> varNames = extractVariableNames(ctx.testlist_star_expr(0));
        List<String> values = extractVariableNames(ctx.testlist_star_expr(1));
        List<String> types = inferTypesFromValues(values);
        String path = PathExtractor.extractPath(ctx);
        int startLine = ctx.getStart().getLine();
        int endLine = ctx.getStop().getLine();

        if (varNames.size() != values.size()) {
            throw new IllegalArgumentException("The number of variable names and values does not match");
        }

        for (int i = 0; i < varNames.size(); i++) {
            addVariableElement(varNames.get(i), path, types.get(i), parentIdentifier, values.get(i), startLine, endLine);
        }
    }

    private List<String> extractVariableNames(Python3Parser.Testlist_star_exprContext variableDeclarators) {
        List<String> variableNames = new ArrayList<>();
        for (Python3Parser.TestContext testCtx : variableDeclarators.test()) {
            String name = testCtx.getText();
            variableNames.add(name);
        }
        return variableNames;
    }

    private List<String> inferTypesFromValues(List<String> values) {
        List<String> types = new ArrayList<>();

        for (String value : values) {
            types.add(inferTypeFromValue(value));
        }
        return types;
    }

    private String inferTypeFromValue(String value) {
        if (value.matches("^-?\\d+$")) {
            return "int";
        } else if (value.matches("^-?\\d+\\.\\d+$")) {
            return "float";
        } else if (value.matches("^\".*\"$")) {
            return "str";
        } else if (value.equals("True") || value.equals("False")) {
            return "bool";
        } else {
            /*
             * Later need to check if it is a Class Object cannot be done here
             * as it requires all classes to be parsed already
             */
            return "any";
        }
    }

    private void addVariableElement(String varName, String path, String type, ElementIdentifier parentIdentifier, String value, int startLine, int endLine) {
        VariableElement variable = new VariableElement(varName, path, type, parentIdentifier, startLine, endLine);
        elementRegistry.addVariable(variable);
    }

    private void addFunctionElement(String name, String path, ElementIdentifier parentIdentifier, int startLine, int endLine) {
        Type type = Type.FUNCTION;
        Element function = new Element(name, path, type, parentIdentifier, startLine, endLine);
        elementRegistry.addFunction(function);
    }

    private void addClassElement(String name, String path, ElementIdentifier parentIdentifier, List<String> childClassOf, int startLine, int endLine) {
        ClassElement python3ClassElement = new ClassElement(name, path, parentIdentifier, startLine, endLine, childClassOf);
        elementRegistry.addClass(python3ClassElement);
    }

    private void addModules(Python3Parser.File_inputContext ctx) {
        Type type = Type.MODULE;
        String name = PathExtractor.extractNameFromPath(ctx);
        String path = PathExtractor.extractPath(ctx);
        String packagePath = path.substring(0, path.lastIndexOf("/") + 1);
        String packageName = addPackage(packagePath);
        ElementIdentifier parentIdentifier = new ElementIdentifier(packageName, packagePath, Type.PACKAGE);
        Element module = new Element(name, path, type, parentIdentifier);
        elementRegistry.addModule(module);
    }

    private String addPackage(String packagePath) {
        List<PackageElement> packageElements = elementRegistry.getPackages();
        String closestParentName = "";
        String closestParentPath = "";
        String packageName = "";

        for (PackageElement packageElement : packageElements) {
            if (packageElement.getPath().equals(packagePath)) {
                return packageElement.getName();
            }
            if (packagePath.startsWith(packageElement.getPath()) && packageElement.getPath().length() > closestParentPath.length()) {
                closestParentName = packageElement.getName();
                closestParentPath = packageElement.getPath();
            }
        }

        if (!closestParentPath.isEmpty()) {
            ElementIdentifier parentIdentifier = new ElementIdentifier(closestParentName, closestParentPath, Type.PACKAGE);
            packageName = packagePath.substring(closestParentPath.length(), packagePath.length() - 1);
            PackageElement packageElement = new PackageElement(packageName, packagePath, parentIdentifier);
            elementRegistry.addPackage(packageElement);
        } else {
            packageName = packagePath.substring(0, packagePath.length() - 1);
            PackageElement packageElement = new PackageElement(packageName, packagePath);
            elementRegistry.addPackage(packageElement);
        }

        updatePackageParentIdentifiers(packageName, packagePath);

        return packageName;
    }

    private void updatePackageParentIdentifiers(String packageName, String packagePath) {
        List<PackageElement> packageElements = elementRegistry.getPackages();
        for (PackageElement packageElement : packageElements) {
            if (packageElement.getPath().startsWith(packagePath) && packageElement.getPath().length() > packagePath.length()) {
                ElementIdentifier parentIdentifier = new ElementIdentifier(packageName, packagePath, Type.PACKAGE);
                packageElement.updateParentIdentifier(parentIdentifier);
                packageElement.updateShortName(packageElement.getPath().substring(packagePath.length(), packageElement.getPath().length() - 1));
            }
        }
    }

}
