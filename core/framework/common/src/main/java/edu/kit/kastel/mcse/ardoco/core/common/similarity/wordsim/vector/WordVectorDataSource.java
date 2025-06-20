/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.vector;

import java.util.Optional;

/**
 * A data source that provides vector representations for words.
 */
public interface WordVectorDataSource {

    /**
     * Attempts to retrieve the vector representation for the given word.
     *
     * @param word the word
     * @return the vector representation, or {@link Optional#empty()} if not found
     * @throws RetrieveVectorException if an error occurs while retrieving the vector
     */
    Optional<float[]> getWordVector(String word) throws RetrieveVectorException;

}
