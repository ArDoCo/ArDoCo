/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.connectors.generators;

import edu.kit.kastel.mcse.ardoco.core.api.models.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.models.ModelType;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.Model;

public abstract class Extractor {
    protected final Metamodel metamodelToExtract;
    protected String path;

    protected Extractor(String path, Metamodel metamodelToExtract) {
        this.path = path;
        this.metamodelToExtract = metamodelToExtract;
    }

    public String getPath() {
        return this.path;
    }

    public final Model extractModel(String path) {
        this.path = path;
        return this.extractModel();
    }

    public abstract Model extractModel();

    public Metamodel getMetamodel() {
        return this.getModelType().getMetamodel();
    }

    public abstract ModelType getModelType();
}
