/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItem;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;

/**
 * Code model with compilation units and packages.
 * Provides endpoints and type identifiers for code items.
 */
@Deterministic
public final class CodeModelWithCompilationUnitsAndPackages extends CodeModel {
    private final CodeModelWithOnlyCompilationUnits codeModel;

    /**
     * Creates a new code model from a DTO.
     *
     * @param codeModelDTO the code model DTO
     */
    public CodeModelWithCompilationUnitsAndPackages(CodeModelDTO codeModelDTO) {
        super(codeModelDTO.codeItemRepository(), codeModelDTO.content());
        this.codeModel = new CodeModelWithOnlyCompilationUnits(codeModelDTO);
    }

    /**
     * Creates a new code model from a repository and content.
     *
     * @param codeItemRepository the code item repository
     * @param content            the code items
     */
    public CodeModelWithCompilationUnitsAndPackages(CodeItemRepository codeItemRepository, SortedSet<? extends CodeItem> content) {
        super(codeItemRepository, content);
        this.codeModel = new CodeModelWithOnlyCompilationUnits(codeItemRepository, content);
    }

    /**
     * Returns the content of this code model.
     *
     * @return list of code items
     */
    @Override
    public List<? extends CodeItem> getContent() {
        return this.getEndpoints();
    }

    /**
     * Returns the endpoints of this code model.
     *
     * @return list of code items
     */
    @Override
    public List<CodeItem> getEndpoints() {
        // The order is more than important here! Otherwise, ArDoCo Heuristics might not work properly.
        List<CodeItem> entities = new ArrayList<>(codeModel.getAllPackages());
        codeModel.getContent().forEach(c -> entities.addAll(c.getAllCompilationUnits()));
        return entities;
    }

    /**
     * Returns the metamodel of this code model.
     *
     * @return the metamodel
     */
    @Override
    public Metamodel getMetamodel() {
        return Metamodel.CODE_WITH_COMPILATION_UNITS_AND_PACKAGES;
    }

    /**
     * Returns the type identifiers of the code items in this model.
     *
     * @return sorted set of type identifiers
     */
    @Override
    public SortedSet<String> getTypeIdentifiers() {
        SortedSet<String> identifiers = new TreeSet<>();
        for (var codeItem : this.getContent()) {
            var type = codeItem.getType();
            type.ifPresent(identifiers::add);
        }
        return identifiers;
    }
}
