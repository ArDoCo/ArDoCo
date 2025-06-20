/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import edu.kit.kastel.mcse.ardoco.core.api.entity.CodeEntity;

/**
 * Abstract base class for items in the code model.
 * Provides methods to access content and relationships between code elements.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({ //
        @JsonSubTypes.Type(value = CodeModule.class, name = "CodeModule"),//
        @JsonSubTypes.Type(value = ComputationalObject.class, name = "ComputationalObject"), //
        @JsonSubTypes.Type(value = Datatype.class, name = "Datatype") //
})
public abstract sealed class CodeItem extends CodeEntity permits CodeModule, ComputationalObject, Datatype {

    @Serial
    private static final long serialVersionUID = 7089107378955018027L;

    @JsonIgnore
    protected CodeItemRepository codeItemRepository;

    CodeItem() {
        // Jackson
        super(null);
    }

    /**
     * Creates a new code item with the specified name.
     *
     * @param codeItemRepository the code item repository
     * @param name               the name of the code item
     */
    protected CodeItem(CodeItemRepository codeItemRepository, String name) {
        super(name);
        this.codeItemRepository = Objects.requireNonNull(codeItemRepository);
        this.codeItemRepository.addCodeItem(this);
    }

    /**
     * Registers the current code item repository for this code item.
     *
     * @param codeItemRepository the code item repository to register
     */
    void registerCurrentCodeItemRepository(CodeItemRepository codeItemRepository) {
        this.codeItemRepository = codeItemRepository;
    }

    /**
     * Returns the content of this code item.
     *
     * @return list of content code items
     */
    public List<CodeItem> getContent() {
        return new ArrayList<>();
    }

    /**
     * Returns all data types contained in this code item.
     *
     * @return list of all data types
     */
    public List<Datatype> getAllDataTypes() {
        return new ArrayList<>();
    }

    /**
     * Returns all data types and this code item itself as a sorted set.
     *
     * @return sorted set of all data types and this code item
     */
    public SortedSet<CodeItem> getAllDataTypesAndSelf() {
        SortedSet<CodeItem> result = new TreeSet<>(this.getAllDataTypes());
        result.add(this);
        return result;
    }

    /**
     * Returns all declared methods in this code item as a sorted set.
     *
     * @return sorted set of declared methods
     */
    public SortedSet<ControlElement> getDeclaredMethods() {
        SortedSet<ControlElement> methods = new TreeSet<>();
        for (CodeItem codeItem : this.getContent()) {
            if (codeItem instanceof ControlElement codeMethod) {
                methods.add(codeMethod);
            }
        }
        return methods;
    }

    /**
     * Returns all compilation units in this code item as a sorted set.
     *
     * @return sorted set of compilation units
     */
    public SortedSet<CodeCompilationUnit> getAllCompilationUnits() {
        return new TreeSet<>();
    }

    /**
     * Returns all code packages in this code item as a sorted set.
     *
     * @return sorted set of code packages
     */
    public SortedSet<CodePackage> getAllPackages() {
        return new TreeSet<>();
    }
}
