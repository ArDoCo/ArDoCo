/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.textproviderjson.converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.collections.api.list.ImmutableList;

import edu.kit.kastel.mcse.ardoco.core.api.text.DependencyTag;
import edu.kit.kastel.mcse.ardoco.core.api.text.POSTag;
import edu.kit.kastel.mcse.ardoco.core.api.text.Phrase;
import edu.kit.kastel.mcse.ardoco.core.api.text.Sentence;
import edu.kit.kastel.mcse.ardoco.core.api.text.Text;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.kit.kastel.mcse.ardoco.core.textproviderjson.dto.IncomingDependencyDto;
import edu.kit.kastel.mcse.ardoco.core.textproviderjson.dto.OutgoingDependencyDto;
import edu.kit.kastel.mcse.ardoco.core.textproviderjson.dto.SentenceDto;
import edu.kit.kastel.mcse.ardoco.core.textproviderjson.dto.TextDto;
import edu.kit.kastel.mcse.ardoco.core.textproviderjson.dto.WordDto;
import edu.kit.kastel.mcse.ardoco.core.textproviderjson.error.NotConvertableException;
import edu.kit.kastel.mcse.ardoco.core.textproviderjson.textobject.DependencyImpl;

public class ObjectToDtoConverter {

    private static final String TREE_SEPARATOR = " ";
    private static final char TREE_OPEN_BRACKET = '(';
    private static final char TREE_CLOSE_BRACKET = ')';

    /**
     * converts an ArDoCo text into a text DTO
     *
     * @param text the ArDoCo text
     * @return the text DTO
     */
    public TextDto convertTextToDTO(Text text) throws NotConvertableException {
        if (text == null) {
            throw new NotConvertableException("Text is null");
        }
        TextDto textDTO = new TextDto();
        List<SentenceDto> sentences = generateSentenceDTOs(text.getSentences());
        textDTO.setSentences(sentences);
        return textDTO;
    }

    private List<SentenceDto> generateSentenceDTOs(ImmutableList<Sentence> sentences) throws NotConvertableException {
        List<SentenceDto> sentenceDtos = new ArrayList<>();
        for (Sentence sentence : sentences) {
            sentenceDtos.add(convertToSentenceDTO(sentence));
        }
        return sentenceDtos;
    }

    private SentenceDto convertToSentenceDTO(Sentence sentence) throws NotConvertableException {
        SentenceDto sentenceDTO = new SentenceDto();
        sentenceDTO.setSentenceNo(sentence.getSentenceNumber() + (long) 1);
        sentenceDTO.setText(sentence.getText());
        List<WordDto> words = generateWordDTOs(sentence.getWords());
        sentenceDTO.setWords(words);
        String tree = convertToConstituencyTrees(sentence);
        sentenceDTO.setConstituencyTree(tree);
        return sentenceDTO;
    }

    private List<WordDto> generateWordDTOs(ImmutableList<Word> words) throws NotConvertableException {
        List<WordDto> wordDtos = new ArrayList<>();
        for (Word word : words) {
            wordDtos.add(convertToWordDTO(word));
        }
        return wordDtos;
    }

    private WordDto convertToWordDTO(Word word) throws NotConvertableException {
        WordDto wordDTO = new WordDto();
        wordDTO.setId(word.getPosition() + (long) 1);
        wordDTO.setText(word.getText());
        wordDTO.setLemma(word.getLemma());
        try {
            wordDTO.setPosTag(POSTag.forValue(word.getPosTag().toString()));
        } catch (IOException e) {
            throw new NotConvertableException(String.format("IOException when converting word with id %d to WordDto: PosTag not found.", wordDTO.getId()));
        }
        wordDTO.setSentenceNo(word.getSentenceNo() + (long) 1);
        List<DependencyImpl> inDep = new ArrayList<>();
        List<DependencyImpl> outDep = new ArrayList<>();
        for (DependencyTag depType : DependencyTag.values()) {
            ImmutableList<Word> inDepWords = word.getIncomingDependencyWordsWithType(depType);
            inDep.addAll(inDepWords.toList().stream().map(currentWord -> new DependencyImpl(depType, currentWord.getPosition())).toList());
            ImmutableList<Word> outDepWords = word.getOutgoingDependencyWordsWithType(depType);
            outDep.addAll(outDepWords.toList().stream().map(currentWord -> new DependencyImpl(depType, currentWord.getPosition())).toList());
        }
        List<IncomingDependencyDto> inDepDTO = generateDepInDTOs(inDep);
        List<OutgoingDependencyDto> outDepDTO = generateDepOutDTOs(outDep);
        wordDTO.setIncomingDependencies(inDepDTO);
        wordDTO.setOutgoingDependencies(outDepDTO);
        return wordDTO;
    }

    private String convertToConstituencyTrees(Sentence sentence) {
        List<Phrase> rootPhrases = ConverterUtil.getChildPhrases(sentence);
        StringBuilder constituencyTree = new StringBuilder();
        for (Phrase rootPhrase : rootPhrases) {
            constituencyTree.append(convertToSubtree(rootPhrase));
        }
        return constituencyTree.toString();
    }

    private String convertToSubtree(Phrase phrase) {
        List<Word> words = phrase.getContainedWords().toList().stream().filter(x -> x.getPhrase().equals(phrase)).collect(Collectors.toList());
        StringBuilder constituencyTree = new StringBuilder().append(TREE_OPEN_BRACKET);
        constituencyTree.append(phrase.getPhraseType().toString());
        List<Phrase> subphrases = new ArrayList<>(ConverterUtil.getChildPhrases(phrase));
        // since we don't know the order of words and subphrases we have to reconstruct the order by comparing the word index
        while (!words.isEmpty() || !subphrases.isEmpty()) {
            if (subphrases.isEmpty()) {
                // word next
                Word word = words.removeFirst();
                constituencyTree.append(TREE_SEPARATOR).append(convertWordToTree(word));
            } else if (words.isEmpty()) {
                // phrase next
                Phrase subphrase = subphrases.removeFirst();
                constituencyTree.append(TREE_SEPARATOR).append(convertToSubtree(subphrase));
            } else {
                int wordIndex = words.getFirst().getPosition();
                List<Integer> phraseWordIndices = subphrases.getFirst().getContainedWords().toList().stream().map(Word::getPosition).toList();
                if (wordIndex < Collections.min(phraseWordIndices)) {
                    // word next
                    Word word = words.removeFirst();
                    constituencyTree.append(TREE_SEPARATOR).append(convertWordToTree(word));
                } else {
                    // phrase next
                    Phrase subphrase = subphrases.removeFirst();
                    constituencyTree.append(TREE_SEPARATOR).append(convertToSubtree(subphrase));
                }
            }
        }
        constituencyTree.append(TREE_CLOSE_BRACKET);
        return constituencyTree.toString();
    }

    private String convertWordToTree(Word word) {
        return TREE_OPEN_BRACKET + word.getPosTag().toString() + TREE_SEPARATOR + word.getText() + TREE_CLOSE_BRACKET;
    }

    private List<IncomingDependencyDto> generateDepInDTOs(List<DependencyImpl> dependencies) {
        return new ArrayList<>(dependencies.stream().map(this::convertToDepInDTO).toList());
    }

    private List<OutgoingDependencyDto> generateDepOutDTOs(List<DependencyImpl> dependencies) {
        return new ArrayList<>(dependencies.stream().map(this::convertToDepOutDTO).toList());
    }

    private IncomingDependencyDto convertToDepInDTO(DependencyImpl dependency) {
        IncomingDependencyDto dependencyDTO = new IncomingDependencyDto();
        dependencyDTO.setDependencyTag(dependency.getDependencyTag());
        dependencyDTO.setSourceWordId(dependency.getWordId() + 1L);
        return dependencyDTO;
    }

    private OutgoingDependencyDto convertToDepOutDTO(DependencyImpl dependency) {
        OutgoingDependencyDto dependencyDTO = new OutgoingDependencyDto();
        dependencyDTO.setDependencyTag(dependency.getDependencyTag());
        dependencyDTO.setTargetWordId(dependency.getWordId() + 1L);
        return dependencyDTO;
    }
}
