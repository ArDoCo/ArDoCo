/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.textextraction;

import java.io.Serial;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.SortedMaps;
import org.eclipse.collections.api.factory.SortedSets;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.sorted.ImmutableSortedMap;
import org.eclipse.collections.api.map.sorted.MutableSortedMap;
import org.eclipse.collections.api.set.sorted.ImmutableSortedSet;
import org.eclipse.collections.api.set.sorted.MutableSortedSet;

import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.NounMapping;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.PhraseMapping;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.TextState;
import edu.kit.kastel.mcse.ardoco.core.api.text.Phrase;
import edu.kit.kastel.mcse.ardoco.core.api.text.PhraseType;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;
import edu.kit.kastel.mcse.ardoco.core.architecture.NoHashCodeEquals;

@Deterministic
@NoHashCodeEquals
public final class PhraseMappingImpl implements PhraseMapping {
    @Serial
    private static final long serialVersionUID = -1379930263383418382L;
    /**
     * Phrases encapsulated in the mapping.
     */
    private final MutableSortedSet<Phrase> phrases;

    public PhraseMappingImpl(ImmutableSortedSet<Phrase> phrases) {
        this.phrases = SortedSets.mutable.withAll(phrases);
    }

    @Override
    public ImmutableList<NounMapping> getNounMappings(TextState textState) {
        var allContainedWords = SortedSets.mutable.withAll(this.phrases.stream().flatMap(phrase -> phrase.getContainedWords().stream()).toList());
        return textState.getNounMappings().select(nm -> SortedSets.mutable.withAll(nm.getWords()).equals(allContainedWords));
    }

    @Override
    public ImmutableSortedSet<Phrase> getPhrases() {
        return this.phrases.toImmutable();
    }

    @Override
    public void removePhrase(Phrase phrase) {
        this.phrases.remove(phrase);
        assert !this.phrases.isEmpty(); // PhraseMappings cannot be empty!
    }

    @Override
    public PhraseType getPhraseType() {
        if (this.phrases.isEmpty()) {
            throw new IllegalStateException("A phrase mapping should always contain some phrases!");
        }
        return this.phrases.getFirst().getPhraseType();
    }

    @Override
    public ImmutableSortedMap<Word, Integer> getPhraseVector() {
        MutableList<Word> words = Lists.mutable.empty();

        for (Phrase phrase : this.phrases) {
            words.addAllIterable(phrase.getContainedWords());
        }

        MutableSortedMap<Word, Integer> phraseVector = SortedMaps.mutable.empty();
        var grouped = words.groupBy(Word::getText).toMap();
        grouped.forEach((key, value) -> phraseVector.put(value.getAny(), value.size()));

        return phraseVector.toImmutable();
    }

    @Override
    public String toString() {
        return "PhraseMapping{" + "phrases=" + this.phrases + '}';
    }

}
