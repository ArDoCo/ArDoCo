/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.eclipse.collections.api.factory.Lists;

import edu.kit.kastel.mcse.ardoco.core.api.text.Sentence;

/**
 * Represents an inconsistent sentence and all inconsistencies found within it.
 */
public final class InconsistentSentence {
    private final Sentence sentence;
    private final List<Inconsistency> inconsistencies;

    /**
     * @param sentence        the sentence
     * @param inconsistencies the list of inconsistencies
     */
    public InconsistentSentence(Sentence sentence, List<Inconsistency> inconsistencies) {
        this.sentence = sentence;
        this.inconsistencies = inconsistencies;
    }

    /**
     * Creates a new instance with only one inconsistency.
     *
     * @param sentence      the sentence
     * @param inconsistency the inconsistency
     */
    public InconsistentSentence(Sentence sentence, Inconsistency inconsistency) {
        this(sentence, Lists.mutable.of(inconsistency));
    }

    /**
     * Adds an inconsistency to the list of inconsistencies of this sentence.
     *
     * @param inconsistency the inconsistency
     * @return whether the inconsistency was added successfully
     */
    public boolean addInconsistency(Inconsistency inconsistency) {
        return inconsistencies.add(inconsistency);
    }

    /**
     * Creates and returns an info string that contains the sentence number, the text of the sentence, and the reasons of the inconsistencies.
     *
     * @return an info string
     */
    public String getInfoString() {
        StringBuilder reasonsBuilder = new StringBuilder();
        for (var inconsistency : inconsistencies) {
            var reason = inconsistency.getReason();
            reasonsBuilder.append(reason).append("\n");
        }

        String formatString = "S%3d: \"%s\"%n\tInconsistent due to the following reasons:%n%s";
        return String.format(Locale.ENGLISH, formatString, sentence.getSentenceNumberForOutput(), sentence.getText(), reasonsBuilder);
    }

    public Sentence sentence() {
        return sentence;
    }

    public List<Inconsistency> inconsistencies() {
        return inconsistencies;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var that = (InconsistentSentence) obj;
        return Objects.equals(this.sentence, that.sentence) && Objects.equals(this.inconsistencies, that.inconsistencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sentence, inconsistencies);
    }

    @Override
    public String toString() {
        return "InconsistentSentence[" + "sentence=" + sentence + ", " + "inconsistencies=" + inconsistencies + ']';
    }

}
