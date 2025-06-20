/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.id.types;

import java.io.Serial;
import java.util.Locale;
import java.util.Objects;

import org.eclipse.collections.api.collection.ImmutableCollection;
import org.eclipse.collections.api.factory.Lists;

import edu.kit.kastel.mcse.ardoco.core.api.entity.ModelEntity;
import edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency.ModelInconsistency;

public class MissingTextForModelElementInconsistency implements ModelInconsistency {
    private static final String INCONSISTENCY_TYPE_NAME = "MissingTextForModelElement";
    @Serial
    private static final long serialVersionUID = 2376071657979892661L;

    private final ModelEntity modelEntity;

    public MissingTextForModelElementInconsistency(ModelEntity modelEntity) {
        this.modelEntity = modelEntity;
    }

    @Override
    public String getReason() {
        return String.format(Locale.US, "Model contains an Instance \"%s\" (type: \"%s\")  that seems to be undocumented.", modelEntity.getName(), modelEntity
                .getType()
                .orElseThrow());
    }

    @Override
    public String getType() {
        return INCONSISTENCY_TYPE_NAME;
    }

    @Override
    public String toString() {
        return "MissingTextForModelElementInconsistency [modelInstance=" + modelEntity + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelEntity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MissingTextForModelElementInconsistency other)) {
            return false;
        }
        return Objects.equals(modelEntity, other.modelEntity);
    }

    @Override
    public ImmutableCollection<String[]> toFileOutput() {
        String[] entry = { getType(), modelEntity.getName(), modelEntity.getType().orElseThrow() };
        var list = Lists.mutable.<String[]>empty();
        list.add(entry);
        return list.toImmutable();
    }

    @Override
    public String getModelInstanceName() {
        return modelEntity.getName();
    }

    @Override
    public String getModelInstanceType() {
        return modelEntity.getType().orElseThrow();
    }

    @Override
    public String getModelInstanceUid() {
        return modelEntity.getId();
    }
}
