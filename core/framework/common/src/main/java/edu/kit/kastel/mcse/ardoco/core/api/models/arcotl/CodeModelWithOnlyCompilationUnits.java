/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeCompilationUnit;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItem;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;
import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;

/**
 * Code model containing only compilation units.
 * Provides endpoints and type identifiers for compilation units.
 */
@Deterministic
public final class CodeModelWithOnlyCompilationUnits extends CodeModel {

    /**
     * Creates a new code model from a DTO.
     *
     * @param codeModelDTO the code model DTO
     */
    public CodeModelWithOnlyCompilationUnits(CodeModelDTO codeModelDTO) {
        super(codeModelDTO.codeItemRepository(), codeModelDTO.content());
    }

    /**
     * Creates a new code model from a repository and content.
     *
     * @param codeItemRepository the code item repository
     * @param content            the code items
     */
    public CodeModelWithOnlyCompilationUnits(CodeItemRepository codeItemRepository, SortedSet<? extends CodeItem> content) {
        super(codeItemRepository, content);
    }

    /**
     * Returns the endpoints of this code model.
     *
     * @return list of compilation units
     */
    @Override
    public List<CodeCompilationUnit> getEndpoints() {
        List<CodeCompilationUnit> entities = new ArrayList<>();
        this.getContent().forEach(c -> entities.addAll(c.getAllCompilationUnits()));
        return entities;
    }

    /**
     * Returns the content of this code model.
     *
     * @return list of code items
     */
    @Override
    public List<? extends CodeItem> getContent() {
        this.initialize();
        return this.codeItemRepository.getCodeItemsFromIds(this.content);
    }

    /**
     * Returns the metamodel of this code model.
     *
     * @return the metamodel
     */
    @Override
    public Metamodel getMetamodel() {
        return Metamodel.CODE_ONLY_COMPILATION_UNITS;
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
        if (!(o instanceof CodeModelWithOnlyCompilationUnits codeModel) || !super.equals(o) || !Objects.equals(this.codeItemRepository,
                codeModel.codeItemRepository)) {
            return false;
        }
        return Objects.equals(this.content, codeModel.content);
    }

    /**
     * Returns the hash code for this code model.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (this.codeItemRepository != null ? this.codeItemRepository.hashCode() : 0);
        return 31 * result + (this.content != null ? this.content.hashCode() : 0);
    }
}
