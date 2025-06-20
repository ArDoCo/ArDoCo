/* Licensed under MIT 2024-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.text;

import java.io.Serial;
import java.util.Objects;

import edu.kit.kastel.mcse.ardoco.core.api.entity.TextEntity;

/**
 * Represents a sentence entity wrapping a {@link Sentence}.
 */
public final class SentenceEntity extends TextEntity {

    @Serial
    private static final long serialVersionUID = 7370112901785822767L;

    private final Sentence sentence;

    /**
     * Creates a new sentence entity.
     *
     * @param sentence the sentence
     */
    public SentenceEntity(Sentence sentence) {
        super(sentence.getText(), String.valueOf(sentence.getSentenceNumber()));
        this.sentence = sentence;
    }

    /**
     * Returns the wrapped sentence.
     *
     * @return the sentence
     */
    public Sentence getSentence() {
        return this.sentence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        SentenceEntity that = (SentenceEntity) o;
        return Objects.equals(this.sentence, that.sentence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.sentence);
    }
}
