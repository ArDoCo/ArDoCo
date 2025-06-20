/* Licensed under MIT 2025. */
package edu.kit.kastel.mcse.ardoco.core.api.stage.connectiongenerator;

import java.io.Serial;
import java.util.Objects;

import edu.kit.kastel.mcse.ardoco.core.api.entity.ModelEntity;
import edu.kit.kastel.mcse.ardoco.core.api.text.Sentence;
import edu.kit.kastel.mcse.ardoco.core.api.text.SentenceEntity;
import edu.kit.kastel.mcse.ardoco.core.api.tracelink.TraceLink;

/**
 * Trace link between a sentence and a model entity.
 */
public final class SadModelTraceLink extends TraceLink<SentenceEntity, ModelEntity> {

    @Serial
    private static final long serialVersionUID = 8946290400185965394L;
    private final Sentence sentence;
    private final ModelEntity modelEntity;

    /**
     * Create a trace link based on a {@link Sentence} and a concrete {@link ModelEntity}.
     *
     * @param sentence    the sentence
     * @param modelEntity the model entity
     */
    public SadModelTraceLink(Sentence sentence, ModelEntity modelEntity) {
        super(new SentenceEntity(sentence), modelEntity);
        this.sentence = sentence;
        this.modelEntity = modelEntity;
    }

    /**
     * Create a trace link based on a {@link SentenceEntity} and a concrete {@link ModelEntity}.
     *
     * @param sentenceEntity the sentence entity
     * @param modelEntity    the model entity
     */
    public SadModelTraceLink(SentenceEntity sentenceEntity, ModelEntity modelEntity) {
        super(sentenceEntity, modelEntity);
        this.sentence = sentenceEntity.getSentence();
        this.modelEntity = modelEntity;
    }

    /**
     * Get the sentence number of the word that the trace link is based on.
     *
     * @return sentence number
     */
    public int getSentenceNumber() {
        return this.sentence.getSentenceNumber();
    }

    /**
     * Returns the sentence of the word that the trace link is based on.
     *
     * @return the sentence
     */
    public Sentence getSentence() {
        return this.sentence;
    }

    /**
     * Get the UID of the model element that the trace link is based on.
     *
     * @return UID of the model element
     */
    public String getEntityId() {
        return this.modelEntity.getId();
    }

    /**
     * Checks equality with another object.
     *
     * @param obj the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        // TODO Check whether we really need to override equals and hashCode here.
        if (obj instanceof SadModelTraceLink other) {
            var otherId = other.getEntityId();
            var otherSentenceNo = other.getSentenceNumber();
            return this.getEntityId().equals(otherId) && this.getSentenceNumber() == otherSentenceNo;
        }
        return false;
    }

    /**
     * Returns the hash code for this trace link.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.getEntityId(), this.getSentenceNumber());
    }

}
