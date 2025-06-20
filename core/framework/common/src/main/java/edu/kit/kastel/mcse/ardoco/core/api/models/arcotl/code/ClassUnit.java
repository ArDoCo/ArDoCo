/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represents a class unit in the code model.
 * Contains code items representing the contents of a class, such as methods and fields.
 */
@JsonTypeName("ClassUnit")
public final class ClassUnit extends Datatype {

    @Serial
    private static final long serialVersionUID = 354013115794534271L;

    @JsonProperty
    private final List<String> content;

    /**
     * Default constructor for Jackson.
     */
    @SuppressWarnings("unused")
    private ClassUnit() {
        // Jackson
        this.content = new ArrayList<>();
    }

    /**
     * Creates a new class unit with the specified name and content.
     *
     * @param codeItemRepository the code item repository
     * @param name               the name of the class unit
     * @param content            the content of the class unit
     */
    public ClassUnit(CodeItemRepository codeItemRepository, String name, SortedSet<? extends CodeItem> content) {
        super(codeItemRepository, name);
        this.content = new ArrayList<>();
        for (var codeItem : content) {
            this.content.add(codeItem.getId());
        }
    }

    /**
     * Returns the content IDs of this class unit.
     *
     * @return list of content IDs
     */
    @JsonGetter("content")
    public List<String> getContentIds() {
        return new ArrayList<>(this.content);
    }

    /**
     * Returns the content of this class unit as a list of code items.
     *
     * @return list of code items
     */
    @Override
    public List<CodeItem> getContent() {
        return this.codeItemRepository.getCodeItemsFromIds(this.content);
    }

    /**
     * Returns all datatypes contained in this class unit, including itself and all nested datatypes.
     *
     * @return list of all datatypes
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
        if (!(o instanceof ClassUnit classUnit) || !super.equals(o)) {
            return false;
        }
        return this.content.equals(classUnit.content);
    }

    /**
     * Returns the hash code for this class unit.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        return 31 * result + this.content.hashCode();
    }
}
