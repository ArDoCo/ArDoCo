/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.textextraction;

import static edu.kit.kastel.mcse.ardoco.core.common.AggregationFunctions.AVERAGE;

import java.io.Serial;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.SortedMaps;
import org.eclipse.collections.api.factory.SortedSets;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.sorted.ImmutableSortedMap;
import org.eclipse.collections.api.map.sorted.MutableSortedMap;
import org.eclipse.collections.api.set.sorted.ImmutableSortedSet;
import org.eclipse.collections.api.set.sorted.MutableSortedSet;

import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.MappingKind;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.NounMapping;
import edu.kit.kastel.mcse.ardoco.core.api.text.Phrase;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;
import edu.kit.kastel.mcse.ardoco.core.architecture.NoHashCodeEquals;
import edu.kit.kastel.mcse.ardoco.core.common.AggregationFunctions;
import edu.kit.kastel.mcse.ardoco.core.data.Confidence;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Claimant;

/**
 * The Class NounMapping is a basic realization of {@link NounMapping}.
 */
@Deterministic
@NoHashCodeEquals
public class NounMappingImpl implements NounMapping {

    protected static final AtomicLong CREATION_TIME_COUNTER = new AtomicLong(0);

    @Serial
    private static final long serialVersionUID = -1848153982944207492L;

    private static final AggregationFunctions DEFAULT_AGGREGATOR = AVERAGE;
    private final Long earliestCreationTime;
    private final MutableSortedSet<Word> words;
    private MutableSortedSet<Phrase> phrases;
    private final MutableSortedMap<MappingKind, Confidence> distribution;
    private final MutableList<Word> referenceWords;
    private final MutableList<String> surfaceForms;
    private final String reference;
    private boolean isDefinedAsCompound;

    /**
     * Instantiates a new noun mapping. A new creation time will be generated.
     *
     * @param words          the list of words for this nounmapping
     * @param kind           the kind of mapping
     * @param claimant       the claimant that created this mapping
     * @param probability    the confidence
     * @param referenceWords the reference words
     * @param surfaceForms   the surface forms
     */
    public NounMappingImpl(ImmutableSortedSet<Word> words, MappingKind kind, Claimant claimant, double probability, ImmutableList<Word> referenceWords,
            ImmutableList<String> surfaceForms) {
        this(CREATION_TIME_COUNTER.incrementAndGet(), words, kind, claimant, probability, referenceWords, surfaceForms);
    }

    /**
     * Constructor. A new creation time will be generated.
     *
     * @param words          the words
     * @param distribution   the distribution map (kind to confidence)
     * @param referenceWords the reference words
     * @param surfaceForms   the surface forms
     * @param reference      the String reference
     */

    public NounMappingImpl(ImmutableSortedSet<Word> words, ImmutableSortedMap<MappingKind, Confidence> distribution, ImmutableList<Word> referenceWords,
            ImmutableList<String> surfaceForms, String reference) {
        this(CREATION_TIME_COUNTER.incrementAndGet(), words, distribution, referenceWords, surfaceForms, reference);
    }

    /**
     * Constructor
     *
     * @param earliestCreationTime the earliest creation time
     * @param words                the words
     * @param distribution         the distribution map (kind to confidence)
     * @param referenceWords       the reference words
     * @param surfaceForms         the surface forms
     * @param reference            the String reference
     */

    public NounMappingImpl(Long earliestCreationTime, ImmutableSortedSet<Word> words, ImmutableSortedMap<MappingKind, Confidence> distribution,
            ImmutableList<Word> referenceWords, ImmutableList<String> surfaceForms, String reference) {
        this.earliestCreationTime = earliestCreationTime;
        this.words = words.toSortedSet();
        this.distribution = distribution.toSortedMap();
        this.referenceWords = referenceWords.toList();
        this.surfaceForms = surfaceForms.toList();
        this.reference = reference;
        this.isDefinedAsCompound = false;

    }

    /**
     * Instantiates a new noun mapping.
     *
     * @param earliestCreationTime the earliest creation time
     * @param words                the list of words for this nounmapping
     * @param kind                 the kind of mapping
     * @param claimant             the claimant that created this mapping
     * @param probability          the confidence
     * @param referenceWords       the reference words
     * @param surfaceForms         the surface forms
     */
    public NounMappingImpl(Long earliestCreationTime, ImmutableSortedSet<Word> words, MappingKind kind, Claimant claimant, double probability,
            ImmutableList<Word> referenceWords, ImmutableList<String> surfaceForms) {
        this(earliestCreationTime, words.toSortedSet().toImmutable(), SortedMaps.immutable.empty(), referenceWords, surfaceForms, calculateReference(
                referenceWords));

        Objects.requireNonNull(claimant);
        this.distribution.putIfAbsent(MappingKind.NAME, new Confidence(DEFAULT_AGGREGATOR));
        this.distribution.putIfAbsent(MappingKind.TYPE, new Confidence(DEFAULT_AGGREGATOR));
        this.addKindWithProbability(kind, claimant, probability);
    }

    @Override
    public final ImmutableSortedSet<Word> getWords() {
        return this.words.toImmutable();
    }

    @Override
    public String getReference() {
        return this.reference;
    }

    private static String calculateReference(ImmutableList<Word> words) {
        return words.collect(Word::getText).makeString(" ");
    }

    @Override
    public final ImmutableList<Word> getReferenceWords() {
        return this.referenceWords.toImmutable();
    }

    @Override
    public final ImmutableList<Integer> getMappingSentenceNo() {
        MutableList<Integer> positions = Lists.mutable.empty();
        for (Word word : this.words) {
            positions.add(word.getSentenceNumber() + 1);
        }
        return positions.toSortedList().toImmutable();
    }

    @Override
    public ImmutableSortedSet<Phrase> getPhrases() {
        if (this.phrases == null) {
            this.phrases = SortedSets.mutable.empty();
            for (Word word : this.words) {
                if (this.phrases.contains(word.getPhrase())) {
                    continue;
                }
                this.phrases.add(word.getPhrase());
            }
        }
        return this.phrases.toImmutable();
    }

    @Override
    public void addKindWithProbability(MappingKind kind, Claimant claimant, double probability) {
        var currentProbability = this.distribution.get(kind);
        Objects.requireNonNull(claimant);
        currentProbability.addAgentConfidence(claimant, probability);
    }

    @Override
    public ImmutableSortedMap<MappingKind, Confidence> getDistribution() {
        return this.distribution.toImmutable();
    }

    @Override
    public double getProbability() {
        return this.distribution.get(this.getKind()).getConfidence();
    }

    @Override
    public MappingKind getKind() {
        var probName = this.distribution.get(MappingKind.NAME).getConfidence();
        var probType = this.distribution.get(MappingKind.TYPE).getConfidence();
        if (probName >= probType) {
            return MappingKind.NAME;
        }
        return MappingKind.TYPE;
    }

    @Override
    public boolean isCompound() {
        return this.isDefinedAsCompound;
    }

    @Override
    public String toString() {
        return "NounMapping [" + "distribution=" + this.distribution.keyValuesView().collect(entry -> entry.getOne() + ":" + entry.getTwo()).makeString(",") + //
                ", reference=" + this.getReference() + //
                ", node=" + String.join(", ", this.surfaceForms) + //
                ", position=" + String.join(", ", this.getWords().collect(word -> String.valueOf(word.getPosition()))) + //
                ", probability=" + this.getProbability() + ", isCompound=" + this.isCompound() + "]";
    }

    @Override
    public double getProbabilityForKind(MappingKind mappingKind) {
        return this.distribution.get(mappingKind).getConfidence();
    }

    @Override
    public ImmutableList<String> getSurfaceForms() {
        return this.surfaceForms.toImmutable();
    }

    @Override
    public ImmutableList<Claimant> getClaimants() {
        Set<Claimant> identitySet = new LinkedHashSet<>();
        for (var claimant : this.distribution.valuesView().flatCollect(Confidence::getClaimants)) {
            identitySet.add(claimant);
        }
        return Lists.immutable.withAll(identitySet);
    }

    public static Long earliestCreationTime(NounMapping... nounMappings) {
        Long earliest = Long.MAX_VALUE;
        for (var mapping : nounMappings) {
            if (mapping instanceof NounMappingImpl impl && impl.earliestCreationTime() < earliest) {
                earliest = impl.earliestCreationTime();
            }
        }
        return earliest == Long.MAX_VALUE ? null : earliest;
    }

    public Long earliestCreationTime() {
        return this.earliestCreationTime;
    }

    public ImmutableSortedSet<Word> words() {
        return this.words.toImmutable();
    }

    public MutableSortedMap<MappingKind, Confidence> distribution() {
        return this.distribution;
    }

    public ImmutableList<Word> referenceWords() {
        return this.referenceWords.toImmutable();
    }

    public ImmutableList<String> surfaceForms() {
        return this.surfaceForms.toImmutable();
    }

    public String reference() {
        return this.reference;
    }

    public void setIsDefinedAsCompound(boolean isDefinedAsCompound) {
        this.isDefinedAsCompound = isDefinedAsCompound;
    }
}
