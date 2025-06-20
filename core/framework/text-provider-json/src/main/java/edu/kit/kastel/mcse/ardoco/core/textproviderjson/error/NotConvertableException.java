/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.textproviderjson.error;

import java.io.Serial;

public class NotConvertableException extends Exception {

    @Serial
    private static final long serialVersionUID = -7449824797573961104L;

    public NotConvertableException(String message) {
        super(message);
    }
}
