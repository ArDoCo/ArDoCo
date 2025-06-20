/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code;

import java.io.Serial;
import java.util.SortedSet;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Represents a code assembly in the code model.
 * Specialized type of {@link CodeModule}.
 */
@JsonTypeName("CodeAssembly")
public final class CodeAssembly extends CodeModule {

    @Serial
    private static final long serialVersionUID = 3082912967900986071L;

    /**
     * Default constructor for Jackson.
     */
    @SuppressWarnings("unused")
    private CodeAssembly() {
        // Jackson
    }

    /**
     * Constructs a new CodeAssembly with the given repository, name, and content.
     *
     * @param codeItemRepository the code item repository
     * @param name               the name of the assembly
     * @param content            the content of the assembly
     */
    public CodeAssembly(CodeItemRepository codeItemRepository, String name, SortedSet<? extends CodeItem> content) {
        super(codeItemRepository, name, content);
    }
}
