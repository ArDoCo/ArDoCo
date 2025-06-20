/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.textproviderjson;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.core.api.text.Phrase;
import edu.kit.kastel.mcse.ardoco.core.api.text.Text;
import edu.kit.kastel.mcse.ardoco.core.textproviderjson.converter.DtoToObjectConverter;
import edu.kit.kastel.mcse.ardoco.core.textproviderjson.textobject.PhraseImpl;

class PhraseImplTest {

    private static final DtoToObjectConverter CONVERTER = new DtoToObjectConverter();
    private static Phrase baselinePhrase;
    private PhraseImpl phraseImplInstance;

    @BeforeAll
    static void initAll() {
        Text baselineText = TestUtil.generateTextWithMultipleSentences();
        baselinePhrase = baselineText.getSentences().get(1).getPhrases().get(0);
    }

    @BeforeEach
    void init() throws Exception {
        Text textImplInstance = CONVERTER.convertText(TestUtil.generateDTOWithMultipleSentences());
        phraseImplInstance = (PhraseImpl) textImplInstance.getSentences().get(1).getPhrases().get(0);
    }

    @Test
    void testGetSentenceNumber() {
        Assertions.assertEquals(baselinePhrase.getSentenceNumber(), phraseImplInstance.getSentenceNumber());
    }

    @Test
    void testGetText() {
        Assertions.assertEquals(baselinePhrase.getText(), phraseImplInstance.getText());
    }

    @Test
    void testGetPhraseType() {
        Assertions.assertEquals(baselinePhrase.getPhraseType(), phraseImplInstance.getPhraseType());
    }

    @Test
    void testGetContainedWords() {
        Assertions.assertEquals(baselinePhrase.getContainedWords().size(), phraseImplInstance.getContainedWords().size());
    }

    @Test
    void testGetSubphrases() {
        Assertions.assertEquals(baselinePhrase.getSubphrases().size(), phraseImplInstance.getSubphrases().size());
    }

    @Test
    void testIsSuperphraseOf() {
        Phrase subphrase = phraseImplInstance.getSubphrases().get(0);
        Assertions.assertAll(//
                () -> Assertions.assertTrue(phraseImplInstance.isSuperphraseOf(subphrase)), () -> Assertions.assertFalse(phraseImplInstance.isSuperphraseOf(
                        phraseImplInstance)), () -> Assertions.assertFalse(subphrase.isSuperphraseOf(phraseImplInstance))//
        );
    }

    @Test
    void testIsSubphraseOf() {
        Phrase subphrase = phraseImplInstance.getSubphrases().get(0);
        Assertions.assertAll(//
                () -> Assertions.assertFalse(phraseImplInstance.isSubphraseOf(subphrase)), () -> Assertions.assertFalse(phraseImplInstance.isSubphraseOf(
                        phraseImplInstance)), () -> Assertions.assertTrue(subphrase.isSubphraseOf(phraseImplInstance))//
        );
    }

    @Test
    void testGetPhraseVector() {
        Assertions.assertEquals(baselinePhrase.getPhraseVector().size(), phraseImplInstance.getPhraseVector().size());
    }

    @Test
    void simpleHashCodeTest() {
        Assertions.assertEquals(phraseImplInstance.hashCode(), phraseImplInstance.hashCode());
    }
}
