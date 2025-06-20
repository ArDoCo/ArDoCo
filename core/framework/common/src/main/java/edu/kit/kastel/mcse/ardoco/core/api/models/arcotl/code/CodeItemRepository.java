/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Repository for storing and managing {@link CodeItem} instances by their IDs.
 */
public class CodeItemRepository implements Serializable {

    @Serial
    private static final long serialVersionUID = 7081204548135982601L;

    @JsonProperty
    private SortedMap<String, CodeItem> repository = new TreeMap<>();
    @JsonIgnore
    private boolean initialized = false;

    /**
     * Returns a copy of the repository map.
     *
     * @return map of code item IDs to code items
     */
    public SortedMap<String, CodeItem> getRepository() {
        return new TreeMap<>(this.repository);
    }

    void addCodeItem(CodeItem codeItem) {
        this.repository.put(codeItem.getId(), codeItem);
    }

    boolean containsCodeItem(String id) {
        return this.repository.containsKey(id);
    }

    CodeItem getCodeItem(String id) {
        if (id == null) {
            return null;
        }
        return this.repository.get(id);
    }

    /**
     * Returns a list of {@link CodeItem} instances for the given list of IDs.
     *
     * @param codeItemIds list of code item IDs
     * @return list of code items corresponding to the IDs
     */
    public List<CodeItem> getCodeItemsFromIds(List<String> codeItemIds) {
        return codeItemIds.stream().map(this::getCodeItem).filter(Objects::nonNull).toList();
    }

    /**
     * Initializes the repository, registering this repository with all contained code items.
     */
    public synchronized void init() {
        if (this.initialized) {
            return;
        }
        this.repository.values().forEach(it -> it.registerCurrentCodeItemRepository(this));
        this.initialized = true;
    }
}
