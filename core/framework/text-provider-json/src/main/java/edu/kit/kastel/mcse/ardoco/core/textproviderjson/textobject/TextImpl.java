/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.textproviderjson.textobject;

import java.io.Serial;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import edu.kit.kastel.mcse.ardoco.core.api.text.Sentence;
import edu.kit.kastel.mcse.ardoco.core.api.text.Text;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;

public class TextImpl implements Text {
    @Serial
    private static final long serialVersionUID = 9154125201453994969L;
    private MutableList<Sentence> sentences;

    private MutableList<Word> words;
    private final SortedMap<Integer, Word> wordsIndex = new TreeMap<>();

    private int length = -1;

    public TextImpl() {
        sentences = Lists.mutable.empty();
        words = Lists.mutable.empty();
    }

    public void setSentences(ImmutableList<Sentence> sentences) {
        this.sentences = sentences.toList();
    }

    @Override
    public synchronized int getNumberOfWords() {
        if (this.length < 0) {
            int calculatedLength = 0;
            for (Sentence sentence : sentences) {
                calculatedLength += sentence.getWords().size();
            }
            this.length = calculatedLength;
        }
        return length;
    }

    @Override
    public ImmutableList<Word> words() {
        if (words.isEmpty()) {
            words = collectWords().toList();
            int index = 0;
            for (Word word : words) {
                wordsIndex.put(index, word);
                index++;
            }
        }
        return words.toImmutable();
    }

    @Override
    public synchronized Word getWord(int index) {
        if (wordsIndex.isEmpty()) {
            words();
        }
        return wordsIndex.get(index);
    }

    @Override
    public ImmutableList<Sentence> getSentences() {
        return sentences.toImmutable();
    }

    private ImmutableList<Word> collectWords() {
        MutableList<Word> collectedWords = Lists.mutable.empty();
        for (Sentence sentence : sentences) {
            collectedWords.addAll(sentence.getWords().toList());
        }
        return collectedWords.toImmutable();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TextImpl text))
            return false;
        return Objects.equals(sentences, text.sentences) && Objects.equals(words, text.words);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sentences, words);
    }
}
