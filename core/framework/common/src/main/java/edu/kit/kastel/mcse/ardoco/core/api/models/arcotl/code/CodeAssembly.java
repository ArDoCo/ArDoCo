/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.code;

import java.util.SortedSet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("CodeAssembly")
public final class CodeAssembly extends CodeModule {

    private static final long serialVersionUID = 3082912967900986071L;
    @JsonProperty
    private String language;

    @SuppressWarnings("unused")
    private CodeAssembly() {
        // Jackson
    }

    public CodeAssembly(CodeItemRepository codeItemRepository, String name, SortedSet<? extends CodeItem> content) {
        super(codeItemRepository, name, content);
    }

    public CodeAssembly(CodeItemRepository codeItemRepository, String name, SortedSet<? extends CodeItem> content, String language) {
        super(codeItemRepository, name, content);
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}
