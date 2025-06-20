/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.textproviderjson.textobject;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import edu.kit.kastel.mcse.ardoco.core.api.text.DependencyTag;

public class DependencyImpl implements Serializable {

    @Serial
    private static final long serialVersionUID = -6941672414051586496L;
    private final DependencyTag dependencyType;
    private final long wordId;

    public DependencyImpl(DependencyTag type, long wordId) {
        this.dependencyType = type;
        this.wordId = wordId;
    }

    public long getWordId() {
        return this.wordId;
    }

    public DependencyTag getDependencyTag() {
        return this.dependencyType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DependencyImpl that)) {
            return false;
        }
        return this.wordId == that.wordId && this.dependencyType == that.dependencyType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.dependencyType, this.wordId);
    }
}
