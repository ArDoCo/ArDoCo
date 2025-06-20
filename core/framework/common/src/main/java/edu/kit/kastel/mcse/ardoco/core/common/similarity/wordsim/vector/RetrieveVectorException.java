/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.similarity.wordsim.vector;

import java.io.Serial;

/**
 * An exception that can occur while trying to retrieve a vector.
 */
public class RetrieveVectorException extends Exception {
    @Serial
    private static final long serialVersionUID = 6771335689887319781L;

    public RetrieveVectorException(String message, Throwable cause) {
        super(message, cause);
    }

}
