/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.text.providers.base;

import org.eclipse.collections.api.list.ImmutableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.core.api.text.NlpInformant;
import edu.kit.kastel.mcse.ardoco.core.api.text.Phrase;
import edu.kit.kastel.mcse.ardoco.core.api.text.PhraseType;
import edu.kit.kastel.mcse.ardoco.core.api.text.Sentence;

public abstract class PhraseTest {
    public static final int SENTENCE_NO = 41;
    private Phrase npPhrase;
    private Phrase vpPhrase;
    private Phrase sentencePhrase;
    private Sentence sentence;

    @BeforeEach
    void beforeEach() {
        var provider = getProvider();
        var text = provider.getAnnotatedText();
        sentence = text.getSentences().get(SENTENCE_NO);
        ImmutableList<Phrase> phrases = sentence.getPhrases();
        sentencePhrase = phrases.get(1);
        vpPhrase = phrases.get(3);
        npPhrase = phrases.get(4);
    }

    protected abstract NlpInformant getProvider();

    @Test
    void getPhraseTypeTest() {
        Assertions.assertAll( //
                () -> Assertions.assertEquals(PhraseType.S, sentencePhrase.getPhraseType()), //
                () -> Assertions.assertEquals(PhraseType.VP, vpPhrase.getPhraseType()), //
                () -> Assertions.assertEquals(PhraseType.NP, npPhrase.getPhraseType()) //
        );
    }

    @Test
    void getSentenceNumberTest() {
        Assertions.assertAll(//
                () -> Assertions.assertEquals(SENTENCE_NO, npPhrase.getSentenceNumber()), //
                () -> Assertions.assertEquals(SENTENCE_NO, vpPhrase.getSentenceNumber()), //
                () -> Assertions.assertEquals(SENTENCE_NO, sentencePhrase.getSentenceNumber()));
    }

    @Test
    void getSentenceTest() {
        Assertions.assertEquals(sentence.getSentenceNumber(), npPhrase.getSentenceNumber());
    }

    @Test
    void getTextTest() {
        var sentencePhraseText = sentencePhrase.getText();
        var npPhraseText = npPhrase.getText();
        var vpPhraseText = vpPhrase.getText();
        Assertions.assertAll( //
                () -> Assertions.assertEquals("The TeaStore is a test application.", sentencePhraseText), //
                () -> Assertions.assertEquals("a test application", npPhraseText), //
                () -> Assertions.assertEquals("is a test application", vpPhraseText)); //
    }

    @Test
    void getContainedWords() {
        var words = npPhrase.getContainedWords();
        Assertions.assertAll(//
                () -> Assertions.assertEquals(3, words.size()), //
                () -> Assertions.assertEquals(738, words.get(0).getPosition()), //
                () -> Assertions.assertEquals(740, words.get(words.size() - 1).getPosition()), //
                () -> Assertions.assertEquals("test", words.get(1).getText()), //
                () -> Assertions.assertEquals(sentence.getWords().get(3), words.get(0)));
    }

    @Test
    void isSuperphraseOfTest() {
        Assertions.assertAll(//
                () -> Assertions.assertTrue(sentencePhrase.isSuperphraseOf(vpPhrase)), //
                () -> Assertions.assertTrue(sentencePhrase.isSuperphraseOf(npPhrase)), //
                () -> Assertions.assertTrue(vpPhrase.isSuperphraseOf(npPhrase)), //
                () -> Assertions.assertFalse(npPhrase.isSuperphraseOf(vpPhrase)), //
                () -> Assertions.assertFalse(npPhrase.isSuperphraseOf(sentencePhrase)), //
                () -> Assertions.assertFalse(vpPhrase.isSuperphraseOf(sentencePhrase)), //
                () -> Assertions.assertFalse(npPhrase.isSuperphraseOf(npPhrase)), //
                () -> Assertions.assertFalse(vpPhrase.isSuperphraseOf(vpPhrase)), //
                () -> Assertions.assertFalse(sentencePhrase.isSuperphraseOf(sentencePhrase)));

    }

    @Test
    void isSubphraseOfTest() {
        Assertions.assertAll(//
                () -> Assertions.assertTrue(vpPhrase.isSubphraseOf(sentencePhrase)), //
                () -> Assertions.assertTrue(npPhrase.isSubphraseOf(sentencePhrase)), //
                () -> Assertions.assertTrue(npPhrase.isSubphraseOf(vpPhrase)), //
                () -> Assertions.assertFalse(vpPhrase.isSubphraseOf(npPhrase)), //
                () -> Assertions.assertFalse(sentencePhrase.isSubphraseOf(npPhrase)), //
                () -> Assertions.assertFalse(sentencePhrase.isSubphraseOf(vpPhrase)), //
                () -> Assertions.assertFalse(npPhrase.isSubphraseOf(npPhrase)), //
                () -> Assertions.assertFalse(vpPhrase.isSubphraseOf(vpPhrase)), //
                () -> Assertions.assertFalse(sentencePhrase.isSubphraseOf(sentencePhrase)));
    }

    @Test
    void getSubphrasesTest() {
        var sentenceSubPhrases = sentencePhrase.getSubphrases();
        var vpSubPhrases = vpPhrase.getSubphrases();
        var npSubPhrases = npPhrase.getSubphrases();

        Assertions.assertAll(//
                () -> Assertions.assertEquals(3, sentenceSubPhrases.size()), //
                () -> Assertions.assertEquals(1, vpSubPhrases.size()), //
                () -> Assertions.assertEquals(0, npSubPhrases.size()));
    }
}
