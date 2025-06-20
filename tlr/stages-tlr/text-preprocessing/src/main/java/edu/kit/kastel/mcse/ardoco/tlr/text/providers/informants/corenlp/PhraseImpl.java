/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.text.providers.informants.corenlp;

import java.io.Serial;
import java.util.Comparator;
import java.util.Objects;

import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.SortedMaps;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.map.sorted.ImmutableSortedMap;
import org.eclipse.collections.api.map.sorted.MutableSortedMap;

import edu.kit.kastel.mcse.ardoco.core.api.text.Phrase;
import edu.kit.kastel.mcse.ardoco.core.api.text.PhraseType;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;
import edu.stanford.nlp.trees.Tree;

@Deterministic
public class PhraseImpl implements Phrase {

    @Serial
    private static final long serialVersionUID = 4504339547271582240L;
    private final Tree tree;
    private final MutableList<Word> words;

    private final SentenceImpl parent;

    private String text = null;

    public PhraseImpl(Tree tree, ImmutableList<Word> words, SentenceImpl parent) {
        this.tree = tree;
        this.words = words.toList();
        this.parent = parent;
    }

    @Override
    public int getSentenceNumber() {
        return words.getFirst().getSentenceNumber();
    }

    @Override
    public String getText() {
        if (text == null) {
            text = tree.spanString();
        }
        return text;
    }

    @Override
    public PhraseType getPhraseType() {
        String type = tree.label().toString();
        return PhraseType.get(type);
    }

    @Override
    public ImmutableList<Word> getContainedWords() {
        return words.toImmutable();
    }

    @Override
    public ImmutableList<Phrase> getSubphrases() {
        MutableList<Phrase> subPhrases = Lists.mutable.empty();
        for (var subTree : tree) {
            if (subTree.isPhrasal() && tree.dominates(subTree)) {
                ImmutableList<Word> wordsForPhrase = Lists.immutable.withAll(parent.getWordsForPhrase(subTree));
                PhraseImpl currPhrase = new PhraseImpl(subTree, wordsForPhrase, parent);
                subPhrases.add(currPhrase);
            }
        }
        return subPhrases.toImmutable();
    }

    @Override
    public boolean isSuperphraseOf(Phrase other) {
        if (other instanceof PhraseImpl otherPhrase) {
            return tree.dominates(otherPhrase.tree);
        } else {
            var currText = getText();
            var otherText = other.getText();
            return currText.contains(otherText) && currText.length() != otherText.length();
        }
    }

    @Override
    public boolean isSubphraseOf(Phrase other) {
        if (other instanceof PhraseImpl otherPhrase) {
            return otherPhrase.tree.dominates(this.tree);
        } else {
            var currText = getText();
            var otherText = other.getText();
            return otherText.contains(currText) && currText.length() != otherText.length();
        }
    }

    @Override
    public ImmutableSortedMap<Word, Integer> getPhraseVector() {
        MutableSortedMap<Word, Integer> phraseVector = SortedMaps.mutable.empty();

        MutableMap<String, RichIterable<Word>> grouped = getContainedWords().groupBy(Word::getText).toMap();
        grouped.forEach((key, value) -> phraseVector.put(value.getAny(), value.size()));

        return phraseVector.toImmutable();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getSentenceNumber(), this.getText(), this.getPhraseType(), this.getContainedWords().get(0).getPosition());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Phrase other))
            return false;
        return this.getSentenceNumber() == other.getSentenceNumber() && Objects.equals(this.getText(), other.getText()) && Objects.equals(this.getPhraseType(),
                other.getPhraseType()) && this.getContainedWords().get(0).getPosition() == other.getContainedWords().get(0).getPosition();
    }

    @Override
    public String toString() {
        return "Phrase{" + "text='" + getText() + '\'' + '}';
    }

    @Override
    public int compareTo(Phrase o) {
        if (this == o)
            return 0;
        return Comparator.comparing(Phrase::getSentenceNumber)
                .thenComparing(Phrase::getText)
                .thenComparing(Phrase::getPhraseType)
                .thenComparingInt(p -> p.getContainedWords().get(0).getPosition())
                .compare(this, o);
    }
}
