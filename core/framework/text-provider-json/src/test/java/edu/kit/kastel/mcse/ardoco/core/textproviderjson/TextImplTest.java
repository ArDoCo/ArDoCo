/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.textproviderjson;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.core.api.text.Text;
import edu.kit.kastel.mcse.ardoco.core.textproviderjson.converter.DtoToObjectConverter;
import edu.kit.kastel.mcse.ardoco.core.textproviderjson.textobject.TextImpl;

class TextImplTest {
    private static final DtoToObjectConverter CONVERTER = new DtoToObjectConverter();
    private static Text baselineText;
    private TextImpl textImplInstance;

    @BeforeAll
    static void initAll() {
        baselineText = TestUtil.generateTextWithMultipleSentences();
    }

    @BeforeEach
    void init() throws Exception {
        textImplInstance = (TextImpl) CONVERTER.convertText(TestUtil.generateDTOWithMultipleSentences());
    }

    @Test
    void getNumberOfWordsTest() {
        Assertions.assertEquals(baselineText.getNumberOfWords(), textImplInstance.getNumberOfWords());
    }

    @Test
    void wordsTest() {
        Assertions.assertEquals(baselineText.words().size(), textImplInstance.words().size());
    }

    @Test
    void getWordTest() {
        Assertions.assertEquals(baselineText.getWord(0), textImplInstance.getWord(0));
    }

    @Test
    void getSentencesTest() {
        Assertions.assertEquals(baselineText.getSentences().size(), textImplInstance.getSentences().size());
    }

    @Test
    void simpleHashCodeTest() {
        Assertions.assertEquals(textImplInstance.hashCode(), textImplInstance.hashCode());
    }
}
