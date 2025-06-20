/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.id.types;

import java.io.Serial;
import java.util.Objects;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.core.api.stage.inconsistency.Inconsistency;
import edu.kit.kastel.mcse.ardoco.core.api.text.DependencyTag;
import edu.kit.kastel.mcse.ardoco.core.api.text.POSTag;
import edu.kit.kastel.mcse.ardoco.core.api.text.Phrase;
import edu.kit.kastel.mcse.ardoco.core.api.text.Sentence;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;

/**
 *
 */
public abstract class AbstractInconsistencyTypeTest {

    protected abstract Inconsistency getInconsistency();

    protected abstract String getTypeString();

    protected abstract String getReasonString();

    protected abstract Inconsistency getUnequalInconsistency();

    protected abstract Inconsistency getEqualInconsistency();

    protected abstract String[] getFileOutputEntry();

    @Test
    void getTypeTest() {
        Assertions.assertEquals(getTypeString(), getInconsistency().getType());
    }

    @Disabled("Disabled for now as the (expected) values might change regularly")
    @Test
    void toFileOutputTest() {
        var fileOutput = getInconsistency().toFileOutput();
        var entry = fileOutput.getAny();
        var expectedEntry = getFileOutputEntry();
        Assertions.assertAll(//
                () -> Assertions.assertEquals(1, fileOutput.size()), //
                () -> Assertions.assertEquals(expectedEntry.length, entry.length), //
                () -> {
                    for (var i = 0; i < expectedEntry.length; i++) {
                        Assertions.assertEquals(expectedEntry[i], entry[i]);
                    }
                });
    }

    @Disabled("Disabled for now as the (expected) values might change regularly")
    @Test
    void getReasonTest() {
        var expectedReason = getReasonString();
        var actualReason = getInconsistency().getReason();
        Assertions.assertEquals(expectedReason, actualReason);
    }

    @Test
    void equalsTest() {
        var otherEqualInconsistency = getEqualInconsistency();
        var otherUnequalInconsistency = getUnequalInconsistency();
        var equality = getInconsistency().equals(otherEqualInconsistency);
        var inequality = getInconsistency().equals(otherUnequalInconsistency);
        Assertions.assertAll(//
                () -> Assertions.assertTrue(equality), //
                () -> Assertions.assertFalse(inequality));
    }

    protected static class DummyWord implements Word {

        @Serial
        private static final long serialVersionUID = -6565646312942900337L;

        @Override
        public int getSentenceNumber() {
            return 0;
        }

        @Override
        public Sentence getSentence() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Phrase getPhrase() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getText() {
            return "text";
        }

        @Override
        public POSTag getPosTag() {
            return POSTag.NOUN;
        }

        @Override
        public Word getPreWord() {
            return null;
        }

        @Override
        public Word getNextWord() {
            return null;
        }

        @Override
        public int getPosition() {
            return 0;
        }

        @Override
        public String getLemma() {
            return "text";
        }

        @Override
        public ImmutableList<Word> getOutgoingDependencyWordsWithType(DependencyTag dependencyTag) {
            return Lists.immutable.empty();
        }

        @Override
        public ImmutableList<Word> getIncomingDependencyWordsWithType(DependencyTag dependencyTag) {
            return Lists.immutable.empty();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getPosition(), getSentenceNumber(), getText());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof DummyWord other)) {
                return false;
            }

            return getPosition() == other.getPosition() && getSentenceNumber() == other.getSentenceNumber() && Objects.equals(getText(), other.getText());
        }

        @Override
        public int compareTo(Word o) {
            if (this.equals(o))
                return 0;

            int compareSentences = Integer.compare(this.getSentenceNumber(), o.getSentenceNumber());
            if (compareSentences != 0) {
                return compareSentences;
            }
            return Integer.compare(this.getPosition(), o.getPosition());
        }
    }

}
