/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code.CodeItemRepository;

/**
 * Data transfer object for the code model.
 * Contains a {@link CodeItemRepository} and a list of content identifiers.
 *
 * @param codeItemRepository the repository of code items
 * @param content            the list of content identifiers
 */
public record CodeModelDTO(@JsonProperty CodeItemRepository codeItemRepository, @JsonProperty List<String> content) {
    /**
     * Returns the code item repository, initializing it if necessary.
     *
     * @return the code item repository
     */
    @Override
    public CodeItemRepository codeItemRepository() {
        codeItemRepository.init();
        return codeItemRepository;
    }
}
