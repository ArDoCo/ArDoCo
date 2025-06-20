/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.id.types;

import java.io.Serial;
import java.util.Locale;
import java.util.Objects;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import edu.kit.kastel.mcse.ardoco.core.api.entity.Entity;
import edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency.TextInconsistency;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;

public class NameInconsistency implements TextInconsistency {

    private static final String INCONSISTENCY_TYPE_NAME = "NameInconsistency";
    @Serial
    private static final long serialVersionUID = 7248818574943312593L;

    private final Entity entity;
    private final Word word;
    private final int sentenceNo;

    public NameInconsistency(Entity entity, Word word) {
        this.entity = entity;
        this.word = word;
        sentenceNo = word.getSentenceNumber() + 1;
    }

    @Override
    public String getReason() {
        String textOccurrence = word.getText();
        String modelOccurrence = entity.getName();
        String uid = entity.getId();
        return String.format(Locale.US, "Inconsistent naming in trace link between textual occurence \"%s\" and model element \"%s\" (%s)", textOccurrence,
                modelOccurrence, uid);
    }

    @Override
    public int getSentenceNumber() {
        return sentenceNo;
    }

    @Override
    public ImmutableList<String[]> toFileOutput() {
        MutableList<String[]> returnList = Lists.mutable.empty();
        var modelUid = entity.getId();
        returnList.add(new String[] { getType(), Integer.toString(sentenceNo), word.getText(), entity.getName(), modelUid });
        return returnList.toImmutable();
    }

    @Override
    public String getType() {
        return INCONSISTENCY_TYPE_NAME;
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, word);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NameInconsistency other)) {
            return false;
        }
        return Objects.equals(entity, other.entity) && Objects.equals(word, other.word);
    }

}
