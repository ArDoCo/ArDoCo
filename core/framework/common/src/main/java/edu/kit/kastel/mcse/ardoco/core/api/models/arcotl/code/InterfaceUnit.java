/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represents an interface unit in the code model.
 * Contains code items representing the contents of an interface, such as method signatures.
 */
@JsonTypeName("InterfaceUnit")
public final class InterfaceUnit extends Datatype {

    @Serial
    private static final long serialVersionUID = 7746781256077022392L;

    @JsonProperty
    private List<String> content;

    /**
     * Default constructor for deserialization frameworks.
     */
    @SuppressWarnings("unused")
    private InterfaceUnit() {
        // Jackson
    }

    /**
     * Creates a new interface unit with the specified name and content.
     *
     * @param codeItemRepository the code item repository
     * @param name               the name of the interface unit
     * @param content            the content of the interface unit
     */
    public InterfaceUnit(CodeItemRepository codeItemRepository, String name, SortedSet<? extends CodeItem> content) {
        super(codeItemRepository, name);
        this.content = new ArrayList<>();
        for (var codeItem : content) {
            this.content.add(codeItem.getId());
        }
    }

    /**
     * Returns the content IDs of this interface unit.
     *
     * @return list of content IDs
     */
    @JsonGetter("content")
    public List<String> getContentIds() {
        return new ArrayList<>(this.content);
    }

    /**
     * Returns the content of this interface unit as a list of code items.
     *
     * @return list of code items
     */
    @Override
    public List<CodeItem> getContent() {
        return this.codeItemRepository.getCodeItemsFromIds(this.content);
    }

    /**
     * Returns all data types contained in this interface unit.
     *
     * @return list of all data types
     */
    @Override
    public List<Datatype> getAllDataTypes() {
        List<Datatype> result = new ArrayList<>();
        result.add(this);
        this.getContent().forEach(c -> result.addAll(c.getAllDataTypes()));
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
        if (!(o instanceof InterfaceUnit that) || !super.equals(o)) {
            return false;
        }

        return Objects.equals(this.content, that.content);
    }

    /**
     * Returns the hash code for this interface unit.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        return 31 * result + (this.content != null ? this.content.hashCode() : 0);
    }
}
