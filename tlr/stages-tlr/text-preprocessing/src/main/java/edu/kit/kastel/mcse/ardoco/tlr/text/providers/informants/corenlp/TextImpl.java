/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.text.providers.informants.corenlp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import edu.kit.kastel.mcse.ardoco.core.api.text.Sentence;
import edu.kit.kastel.mcse.ardoco.core.api.text.Text;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.stanford.nlp.pipeline.CoreDocument;

public class TextImpl implements Text {

    @Serial
    private static final long serialVersionUID = 8720058979625513977L;
    private transient CoreDocument coreDocument;
    private ImmutableList<Sentence> sentences = Lists.immutable.empty();
    private ImmutableList<Word> words = Lists.immutable.empty();
    private final SortedMap<Integer, Word> wordsIndex = new TreeMap<>();

    public TextImpl(CoreDocument coreDocument) {
        this.coreDocument = coreDocument;
    }

    @Override
    public ImmutableList<Word> words() {
        if (words.isEmpty()) {
            iterateDocumentForWordsAndSentences();
        }
        return words;
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
        if (sentences.isEmpty()) {
            iterateDocumentForWordsAndSentences();
        }
        return sentences;
    }

    private void iterateDocumentForWordsAndSentences() {
        MutableList<Sentence> sentenceList = Lists.mutable.empty();
        MutableList<Word> wordList = Lists.mutable.empty();

        var coreSentences = coreDocument.sentences();
        int wordIndex = 0;
        for (int i = 0; i < coreSentences.size(); i++) {
            var coreSentence = coreSentences.get(i);
            var sentence = new SentenceImpl(coreSentence, i, this);
            sentenceList.add(sentence);

            for (var token : coreSentence.tokens()) {
                var word = new WordImpl(token, wordIndex, this);
                wordList.add(word);
                wordIndex++;
            }
        }

        sentences = sentenceList.toImmutable();
        words = wordList.toImmutable();
        int index = 0;
        for (Word word : words) {
            wordsIndex.put(index, word);
            index++;
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        words(); //Initialize words
        getSentences(); //Initialize sentences
        out.defaultWriteObject();
        /* It is a lot cheaper to serialize the phrases (up to 70x less storage space and much
        faster), if the coreDocument is ever made accessible, this should be uncommented
        ProtobufAnnotationSerializer serializer = new ProtobufAnnotationSerializer();
        serializer.writeCoreDocument(coreDocument, out);
         */
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        /* It is a lot cheaper to serialize the phrases (up to 70x less storage space and much
        faster), if the coreDocument is ever made accessible, this should be uncommented
        ProtobufAnnotationSerializer serializer = new ProtobufAnnotationSerializer();
        coreDocument = serializer.readCoreDocument(in).first;
         */
    }
}
