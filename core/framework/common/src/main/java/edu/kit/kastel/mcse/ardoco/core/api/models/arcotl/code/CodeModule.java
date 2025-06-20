/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represents a module in the code model.
 * Can contain other code items, such as packages, compilation units, or assemblies.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({ //
        @JsonSubTypes.Type(value = CodeAssembly.class, name = "CodeAssembly"),//
        @JsonSubTypes.Type(value = CodeCompilationUnit.class, name = "CodeCompilationUnit"), //
        @JsonSubTypes.Type(value = CodePackage.class, name = "CodePackage"), //
})
@JsonTypeName("CodeModule")
public sealed class CodeModule extends CodeItem permits CodeAssembly, CodeCompilationUnit, CodePackage {

    @Serial
    private static final long serialVersionUID = -7941299662945801101L;

    @JsonProperty
    private String parentId;
    @JsonProperty
    private List<String> content;

    CodeModule() {
        // Jackson
    }

    /**
     * Creates a new code module with the specified name and content.
     *
     * @param codeItemRepository the code item repository
     * @param name               the name of the code module
     * @param content            the content of the code module
     */
    public CodeModule(CodeItemRepository codeItemRepository, String name, SortedSet<? extends CodeItem> content) {
        super(codeItemRepository, name);
        this.content = new ArrayList<>();
        for (var codeItem : content) {
            this.content.add(codeItem.getId());
        }
        this.parentId = null;
    }

    /**
     * Returns the content IDs of this code module.
     *
     * @return list of content IDs
     */
    @JsonGetter("content")
    protected List<String> getContentIds() {
        return this.content;
    }

    /**
     * Returns the content of this code module as a list of code items.
     *
     * @return list of code items
     */
    @Override
    public List<CodeItem> getContent() {
        return this.codeItemRepository.getCodeItemsFromIds(this.content);
    }

    /**
     * Sets the content of this code module.
     *
     * @param content list of code items to set as content
     */
    public void setContent(List<? extends CodeItem> content) {
        this.content = new ArrayList<>();
        for (var codeItem : content) {
            this.content.add(codeItem.getId());
        }
    }

    /**
     * Adds a code item to the content of this code module.
     *
     * @param content the code item to add
     */
    public void addContent(CodeItem content) {
        this.content.add(content.getId());
    }

    /**
     * Adds a list of code items to the content of this code module.
     *
     * @param content list of code items to add
     */
    public void addContent(List<? extends CodeItem> content) {
        this.content.addAll(content.stream().map(CodeItem::getId).toList());
    }

    /**
     * Returns the parent code module of this code module, if any.
     *
     * @return parent code module, or null if none
     */
    public CodeModule getParent() {
        CodeItem codeItem = this.codeItemRepository.getCodeItem(this.parentId);
        if (codeItem instanceof CodeModule codeModule) {
            return codeModule;
        }
        return null;
    }

    /**
     * Checks if this code module has a parent.
     *
     * @return true if this code module has a parent, false otherwise
     */
    public boolean hasParent() {
        return this.getParent() != null;
    }

    /**
     * Sets the parent code module for this code module.
     *
     * @param parent the parent code module to set
     */
    public void setParent(CodeModule parent) {
        this.parentId = parent.getId();
        if (!this.codeItemRepository.containsCodeItem(this.parentId)) {
            this.codeItemRepository.addCodeItem(parent);
        }
    }

    /**
     * Returns all compilation units in this code module as a sorted set.
     *
     * @return sorted set of compilation units
     */
    @Override
    public SortedSet<CodeCompilationUnit> getAllCompilationUnits() {
        SortedSet<CodeCompilationUnit> result = new TreeSet<>();
        this.getContent().forEach(c -> result.addAll(c.getAllCompilationUnits()));
        return result;
    }

    /**
     * Returns all code packages in this code module as a sorted set.
     *
     * @return sorted set of code packages
     */
    @Override
    public SortedSet<CodePackage> getAllPackages() {
        SortedSet<CodePackage> result = new TreeSet<>();
        this.getContent().forEach(c -> result.addAll(c.getAllPackages()));
        return result;
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
        if (!(o instanceof CodeModule that) || !super.equals(o) || !Objects.equals(this.parentId, that.parentId)) {
            return false;
        }
        return Objects.equals(this.content, that.content);
    }

    /**
     * Returns the hash code for this code module.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (this.parentId != null ? this.parentId.hashCode() : 0);
        return 31 * result + (this.content != null ? this.content.hashCode() : 0);
    }
}
