/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.textproviderjson.textobject;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;

import edu.kit.kastel.mcse.ardoco.core.api.text.DependencyTag;
import edu.kit.kastel.mcse.ardoco.core.api.text.POSTag;
import edu.kit.kastel.mcse.ardoco.core.api.text.Phrase;
import edu.kit.kastel.mcse.ardoco.core.api.text.Sentence;
import edu.kit.kastel.mcse.ardoco.core.api.text.Text;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;

public class WordImpl implements Word {

    @Serial
    private static final long serialVersionUID = 411612592257031380L;
    private final Text parent;
    private final int indexInText;
    private Word preWord;
    private Word nextWord;

    private final int sentenceNo;
    private Phrase phrase;
    private final String text;
    private final POSTag posTag;
    private final String lemma;

    private final List<DependencyImpl> ingoingDependencies;
    private final List<DependencyImpl> outgoingDependencies;

    public WordImpl(Text parent, int index, int sentenceNo, String text, POSTag posTag, String lemma, List<DependencyImpl> inDep, List<DependencyImpl> outDep) {
        this.parent = parent;
        this.indexInText = index;
        this.preWord = null;
        this.nextWord = null;
        this.sentenceNo = sentenceNo;
        this.phrase = null;
        this.text = text;
        this.posTag = posTag;
        this.lemma = lemma;
        this.outgoingDependencies = outDep;
        this.ingoingDependencies = inDep;
    }

    @Override
    public int getSentenceNumber() {
        return sentenceNo;
    }

    @Override
    public Sentence getSentence() {
        return this.parent.getSentences().get(this.sentenceNo);
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public POSTag getPosTag() {
        return this.posTag;
    }

    @Override
    public Word getPreWord() {
        int preWordIndex = indexInText - 1;
        if (preWord == null && preWordIndex > 0) {
            preWord = parent.getWord(preWordIndex);
        }
        return preWord;
    }

    @Override
    public Word getNextWord() {
        int nextWordIndex = indexInText + 1;
        if (nextWord == null && nextWordIndex < parent.getNumberOfWords()) {
            nextWord = parent.getWord(nextWordIndex);
        }
        return nextWord;
    }

    @Override
    public int getPosition() {
        return this.indexInText;
    }

    @Override
    public String getLemma() {
        return this.lemma;
    }

    @Override
    public ImmutableList<Word> getOutgoingDependencyWordsWithType(DependencyTag dependencyTag) {
        List<DependencyImpl> dependenciesOfType = this.outgoingDependencies.stream().filter(x -> x.getDependencyTag() == dependencyTag).toList();
        List<Word> words = dependenciesOfType.stream().map(x -> this.parent.getWord((int) x.getWordId())).toList();
        return Lists.immutable.ofAll(words);
    }

    @Override
    public ImmutableList<Word> getIncomingDependencyWordsWithType(DependencyTag dependencyTag) {
        List<DependencyImpl> dependenciesOfType = this.ingoingDependencies.stream().filter(x -> x.getDependencyTag() == dependencyTag).toList();
        List<Word> words = dependenciesOfType.stream().map(x -> this.parent.getWord((int) x.getWordId())).toList();
        return Lists.immutable.ofAll(words);
    }

    @Override
    public Phrase getPhrase() {
        if (this.phrase == null) {
            this.phrase = loadPhrase();
        }
        return this.phrase;
    }

    private Phrase loadPhrase() {
        var currentPhrase = getSentence().getPhrases().toList().stream().filter(p -> p.getContainedWords().contains(this)).findFirst().orElseThrow();
        var subPhrases = List.of(currentPhrase);
        while (!subPhrases.isEmpty()) {
            currentPhrase = subPhrases.getFirst();
            subPhrases = currentPhrase.getSubphrases().toList().stream().filter(p -> p.getContainedWords().contains(this)).toList();
        }
        return currentPhrase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof WordImpl word))
            return false;
        return indexInText == word.indexInText && sentenceNo == word.sentenceNo && Objects.equals(text, word.text) && posTag == word.posTag && Objects.equals(
                lemma, word.lemma) && Objects.equals(ingoingDependencies, word.ingoingDependencies) && Objects.equals(outgoingDependencies,
                        word.outgoingDependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(indexInText, sentenceNo, text, posTag, lemma, ingoingDependencies, outgoingDependencies);
    }
}
