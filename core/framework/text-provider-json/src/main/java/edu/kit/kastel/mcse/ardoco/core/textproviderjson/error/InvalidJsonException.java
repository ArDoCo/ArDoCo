/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.textproviderjson.error;

import java.io.Serial;

public class InvalidJsonException extends Exception {

    @Serial
    private static final long serialVersionUID = -3836461863298092356L;

    public InvalidJsonException(String message) {
        super(message);
    }
}
