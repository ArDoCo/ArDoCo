/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.data.impl;

import java.util.List;

import edu.kit.kastel.mcse.ardoco.core.data.PipelineStepData;

/**
 * Example {@link PipelineStepData}
 */
public class ProcessedTextData implements PipelineStepData {
    private static final long serialVersionUID = -6806096212069462237L;
    private List<String> importantTokens;

    public ProcessedTextData() {
        this.importantTokens = null;
    }

    public List<String> getImportantTokens() {
        return this.importantTokens;
    }

    public void setImportantTokens(List<String> importantTokens) {
        this.importantTokens = importantTokens;
    }
}
