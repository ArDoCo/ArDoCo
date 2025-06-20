/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.textproviderjson.textobject;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import edu.kit.kastel.mcse.ardoco.core.api.text.Phrase;
import edu.kit.kastel.mcse.ardoco.core.api.text.Sentence;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;

public class SentenceImpl implements Sentence {

    @Serial
    private static final long serialVersionUID = 949740708112053162L;
    private final MutableList<Word> words;
    private MutableList<Phrase> phrases = Lists.mutable.empty();

    private final int sentenceNumber;

    private final String text;

    public SentenceImpl(int sentenceNumber, String text, ImmutableList<Word> words) {
        this.sentenceNumber = sentenceNumber;
        this.text = text;
        this.words = words == null ? Lists.mutable.empty() : words.toList();
    }

    public void setPhrases(MutableList<Phrase> phrases) {
        this.phrases = phrases;
    }

    @Override
    public int getSentenceNumber() {
        return sentenceNumber;
    }

    @Override
    public ImmutableList<Word> getWords() {
        return words.toImmutable();
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public ImmutableList<Phrase> getPhrases() {
        List<Phrase> allPhrases = new ArrayList<>(this.phrases.toList());
        for (Phrase phrase : this.phrases.toList()) {
            allPhrases.addAll(phrase.getSubphrases().toList());
        }
        return Lists.immutable.ofAll(allPhrases);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SentenceImpl sentence))
            return false;
        return sentenceNumber == sentence.sentenceNumber && Objects.equals(words, sentence.words) && Objects.equals(phrases, sentence.phrases) && Objects
                .equals(text, sentence.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(words, phrases, sentenceNumber, text);
    }

    @Override
    public void addPhrase(Phrase phrase) {
        phrases.add(phrase);
    }
}
