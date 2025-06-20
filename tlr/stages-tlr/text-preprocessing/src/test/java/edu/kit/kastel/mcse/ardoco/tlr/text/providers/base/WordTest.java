/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.text.providers.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.core.api.text.DependencyTag;
import edu.kit.kastel.mcse.ardoco.core.api.text.NlpInformant;
import edu.kit.kastel.mcse.ardoco.core.api.text.POSTag;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;

public abstract class WordTest {
    public static final int WORD_INDEX = 739;
    private static Word word;

    @BeforeEach
    void beforeEach() {
        var provider = getProvider();
        var text = provider.getAnnotatedText();
        word = text.words().get(WORD_INDEX);
    }

    protected abstract NlpInformant getProvider();

    @Test
    void getTextTest() {
        Assertions.assertEquals("test", word.getText());
    }

    @Test
    void getPositionTest() {
        Assertions.assertEquals(WORD_INDEX, word.getPosition());
    }

    @Test
    void getPOSTagTest() {
        Assertions.assertEquals(POSTag.NOUN, word.getPosTag());
    }

    @Test
    void getLemmaTest() {
        Assertions.assertEquals("test", word.getLemma());
    }

    @Test
    void getSentenceNumberTest() {
        Assertions.assertEquals(41, word.getSentenceNumber());
    }

    @Test
    void getIncomingDependencyWordsWithTypeTest() {
        var dependencies = word.getIncomingDependencyWordsWithType(DependencyTag.COMPOUND);
        Assertions.assertAll(//
                () -> Assertions.assertEquals(1, dependencies.size()), //
                () -> Assertions.assertEquals("application", dependencies.get(0).getText()));
    }

    @Test
    void getOutgoingDependencyWordsWithTypeTest() {
        var dependencies = word.getNextWord().getOutgoingDependencyWordsWithType(DependencyTag.COMPOUND);
        Assertions.assertAll(//
                () -> Assertions.assertEquals(1, dependencies.size()), //
                () -> Assertions.assertEquals("test", dependencies.get(0).getText()));
    }

}
