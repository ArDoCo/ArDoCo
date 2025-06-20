/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code;

import java.io.Serial;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represents a package in the code model.
 * Groups related code items and can contain subpackages and compilation units.
 */
@JsonTypeName("CodePackage")
public final class CodePackage extends CodeModule {

    @Serial
    private static final long serialVersionUID = -5224168387357601602L;

    private CodePackage() {
        // Jackson
    }

    /**
     * Creates a new code package with the specified name.
     *
     * @param codeItemRepository the code item repository
     * @param name               the name of the code package
     */
    public CodePackage(CodeItemRepository codeItemRepository, String name) {
        super(codeItemRepository, name, new TreeSet<>());
    }

    /**
     * Creates a new code package with the specified name and content.
     *
     * @param codeItemRepository the code item repository
     * @param name               the name of the code package
     * @param content            the content of the code package
     */
    public CodePackage(CodeItemRepository codeItemRepository, String name, SortedSet<? extends CodeItem> content) {
        super(codeItemRepository, name, content);
    }

    /**
     * Returns the subpackages contained in this code package.
     *
     * @return sorted set of subpackages
     */
    public SortedSet<CodePackage> getSubpackages() {
        SortedSet<CodePackage> codePackages = new TreeSet<>();
        for (CodeItem packageElement : this.getContent()) {
            if (packageElement instanceof CodePackage codePackage) {
                codePackages.add(codePackage);
            }
        }
        return codePackages;
    }

    /**
     * Returns the compilation units contained in this code package.
     *
     * @return sorted set of compilation units
     */
    public SortedSet<CodeCompilationUnit> getCompilationUnits() {
        SortedSet<CodeCompilationUnit> compilationUnits = new TreeSet<>();
        for (CodeItem packageElement : this.getContent()) {
            if (packageElement instanceof CodeCompilationUnit compilationUnit) {
                compilationUnits.add(compilationUnit);
            }
        }
        return compilationUnits;
    }

    /**
     * Returns the type of this code item as an optional string.
     *
     * @return type of this code item
     */
    @Override
    public Optional<String> getType() {
        return Optional.of("Package");
    }

    /**
     * Returns all code packages in this code package as a sorted set, including itself and all nested packages.
     *
     * @return sorted set of all code packages
     */
    @Override
    public SortedSet<CodePackage> getAllPackages() {
        SortedSet<CodePackage> result = new TreeSet<>();
        result.add(this);
        this.getContent().forEach(c -> result.addAll(c.getAllPackages()));
        return result;
    }
}
