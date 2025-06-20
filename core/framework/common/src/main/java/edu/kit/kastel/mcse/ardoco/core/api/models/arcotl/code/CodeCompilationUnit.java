/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represents a compilation unit in the code model.
 * Can serve as the code endpoint of a trace link.
 */
@JsonTypeName("CodeCompilationUnit")
public final class CodeCompilationUnit extends CodeModule {

    @Serial
    private static final long serialVersionUID = 6749513760670983294L;

    @JsonProperty
    private List<String> pathElements;
    @JsonProperty
    private String extension;
    @JsonProperty
    private String language;

    /**
     * Default constructor for Jackson.
     */
    @SuppressWarnings("unused")
    private CodeCompilationUnit() {
        // Jackson
    }

    /**
     * Creates a new CodeCompilationUnit.
     *
     * @param codeItemRepository the code item repository
     * @param name               the name of the compilation unit
     * @param content            the content of the compilation unit
     * @param pathElements       the path elements
     * @param extension          the file extension
     * @param language           the programming language
     */
    public CodeCompilationUnit(CodeItemRepository codeItemRepository, String name, SortedSet<? extends CodeItem> content, List<String> pathElements,
            String extension, String language) {
        super(codeItemRepository, name, content);
        this.pathElements = new ArrayList<>(pathElements);
        this.extension = extension;
        this.language = language;
    }

    /**
     * Returns the programming language of this compilation unit.
     *
     * @return the language
     */
    public String getLanguage() {
        return this.language;
    }

    /**
     * Returns all datatypes contained in this compilation unit.
     *
     * @return list of all datatypes
     */
    @Override
    public List<Datatype> getAllDataTypes() {
        List<Datatype> result = new ArrayList<>();
        this.getContent().forEach(c -> result.addAll(c.getAllDataTypes()));
        return result;
    }

    /**
     * Returns all compilation units contained in this unit (itself).
     *
     * @return set containing this compilation unit
     */
    @Override
    public SortedSet<CodeCompilationUnit> getAllCompilationUnits() {
        SortedSet<CodeCompilationUnit> result = new TreeSet<>();
        result.add(this);
        return result;
    }

    /**
     * Returns the names of parent packages.
     *
     * @return list of parent package names
     */
    public List<String> getParentPackageNames() {
        List<String> parents = new ArrayList<>();
        CodeModule parent = this;
        while (parent.hasParent()) {
            parent = parent.getParent();
            if (parent instanceof CodePackage) {
                parents.addFirst(parent.getName());
            }
        }
        return parents;
    }

    /**
     * Returns the path elements of this compilation unit.
     *
     * @return list of path elements
     */
    public List<String> getPathElements() {
        return new ArrayList<>(this.pathElements);
    }

    /**
     * Returns the full path of this compilation unit.
     *
     * @return the path
     */
    public String getPath() {
        StringBuilder pathBuilder = new StringBuilder();
        for (String pathElement : this.pathElements) {
            pathBuilder.append(pathElement).append("/");
        }
        String ending = "";
        if (!this.extension.isEmpty()) {
            ending = "." + this.extension;
        }
        pathBuilder.append(this.getName()).append(ending);
        return pathBuilder.toString();
    }

    /**
     * Returns the type of this compilation unit as a string.
     *
     * @return the type
     */
    @Override
    public Optional<String> getType() {
        // Assumption mostly one class per unit
        var content = this.getContent().stream().filter(it -> this.getName().contains(it.getName())).findFirst().orElse(null);
        if (content instanceof ClassUnit) {
            return Optional.of("Class");
        }
        if (content instanceof InterfaceUnit) {
            return Optional.of("Interface");
        }
        if (this.getPath().endsWith("package-info.java")) {
            return Optional.of("PackageInfo");
        }
        if (this.getPath().endsWith(".java")) {
            // Default to Class
            return Optional.of("Class");
        }
        if (ProgrammingLanguages.SHELL.equals(this.getLanguage())) {
            return Optional.of("ShellScript");
        }
        return Optional.empty();
    }

    /**
     * Returns a string representation of this compilation unit.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return this.getPath();
    }

    /**
     * Checks equality with another object.
     *
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CodeCompilationUnit that) || !super.equals(o) || !Objects.equals(this.pathElements, that.pathElements) || !Objects.equals(
                this.extension, that.extension)) {
            return false;
        }
        return Objects.equals(this.language, that.language);
    }

    /**
     * Returns the hash code for this compilation unit.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (this.pathElements != null ? this.pathElements.hashCode() : 0);
        result = 31 * result + (this.extension != null ? this.extension.hashCode() : 0);
        return 31 * result + (this.language != null ? this.language.hashCode() : 0);
    }
}
