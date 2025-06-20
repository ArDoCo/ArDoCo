/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.Model;
import edu.kit.kastel.mcse.ardoco.core.api.stage.recommendationgenerator.RecommendationState;
import edu.kit.kastel.mcse.ardoco.core.api.stage.recommendationgenerator.RecommendedInstance;
import edu.kit.kastel.mcse.ardoco.core.api.stage.textextraction.NounMapping;
import edu.kit.kastel.mcse.ardoco.core.api.text.DependencyTag;
import edu.kit.kastel.mcse.ardoco.core.api.text.Word;
import edu.kit.kastel.mcse.ardoco.core.common.similarity.SimilarityUtils;
import edu.kit.kastel.mcse.ardoco.core.pipeline.agent.Claimant;

/**
 * General helper class for common utility methods used throughout the framework.
 */
public final class CommonUtilities {
    /**
     * Formatter for date and time output (pattern: yyyy-MM-dd HH:mm).
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Logger logger = LoggerFactory.getLogger(CommonUtilities.class);

    private CommonUtilities() {
        throw new IllegalAccessError();
    }

    /**
     * Compare two double values.
     *
     * @param d1 value 1
     * @param d2 value 2
     * @return true iff similar enough to be equal
     */
    public static boolean valueEqual(double d1, double d2) {
        return Math.abs(d1 - d2) < 1E-8;
    }

    /**
     * Calculate the arithmetic mean (average) between two given values.
     *
     * @param first  the first value
     * @param second the second value
     * @return the arithmetic mean (average) of the two given values
     */
    public static double arithmeticMean(double first, double second) {
        return arithmeticMean(List.of(first, second));
    }

    /**
     * Calculate the arithmetic mean (average) between the given values.
     *
     * @param values List of doubles that should be used to calculate their mean
     * @return the arithmetic mean (average) of the given values
     */
    public static double arithmeticMean(List<Double> values) {
        return values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    /**
     * Calculate the harmonic mean between two given values.
     *
     * @param first  the first value
     * @param second the second value
     * @return the harmonic mean of the two given values
     */
    public static double harmonicMean(double first, double second) {
        return 2 * first * second / (first + second);
    }

    /**
     * Calculate the harmonic mean between all given values.
     *
     * @param values List of doubles that should be used to calculate their mean
     * @return the harmonic mean of the given values
     */
    public static double harmonicMean(List<Double> values) {
        var quotient = values.stream().mapToDouble(d -> 1.0 / d).sum();
        return values.size() / quotient;
    }

    /**
     * Calculates the root mean square between two given values.
     *
     * @param first  the first value
     * @param second the second value
     * @return the root mean square of the given values
     */
    public static double rootMeanSquare(double first, double second) {
        return Math.sqrt((Math.pow(first, 2) + Math.pow(second, 2)) / 2);
    }

    /**
     * Calculates the root mean square between the given values.
     *
     * @param values List of doubles that should be used to calculate their mean
     * @return the root mean square of the given values
     */
    public static double rootMeanSquare(List<Double> values) {
        var squaredValuesSum = values.stream().mapToDouble(d -> Math.pow(d, 2)).sum();
        return Math.sqrt(squaredValuesSum / values.size());
    }

    /**
     * Calculates the cubic mean between two given values.
     *
     * @param first  the first value
     * @param second the second value
     * @return the cubic mean of the given values
     */
    public static double cubicMean(double first, double second) {
        return Math.cbrt((Math.pow(first, 3) + Math.pow(second, 3)) / 2);
    }

    /**
     * Calculates the cubic mean between the given values.
     *
     * @param values List of doubles that should be used to calculate their mean
     * @return the cubic mean of the given values
     */
    public static double cubicMean(List<Double> values) {
        var cubedValuesSum = values.stream().mapToDouble(d -> Math.pow(d, 3)).sum();
        return Math.cbrt(cubedValuesSum / values.size());
    }

    /**
     * Calculates the power mean (or generalized mean) between the given values.
     *
     * @param first  the first value
     * @param second the second value
     * @param power  the power to use
     * @return the power mean (or generalized mean) of the given values
     */
    public static double powerMean(double first, double second, double power) {
        return Math.pow((Math.pow(first, power) + Math.pow(second, power)) / 2, 1 / power);
    }

    /**
     * Calculates the power mean (or generalized mean) between the given values.
     *
     * @param values List of doubles that should be used to calculate their mean
     * @param power  the power to use
     * @return the power mean (or generalized mean) of the given values
     */
    public static double powerMean(List<Double> values, double power) {
        var poweredValuesSum = values.stream().mapToDouble(d -> Math.pow(d, power)).sum();
        return Math.pow(poweredValuesSum / values.size(), 1 / power);
    }

    /**
     * Replaces all defined separators in a given string with a whitespace and returns the resulting string.
     *
     * @param reference given string
     * @return reference with whitespaces instead of separators
     */
    public static ImmutableList<String> splitAtSeparators(String reference) {
        var ref = reference;
        for (String sep : CommonTextToolsConfig.SEPARATORS_TO_SPLIT) {
            ref = ref.replaceAll(sep, " ");
        }
        return Lists.immutable.withAll(Lists.immutable.with(ref.split(" ")));
    }

    /**
     * Checks if a string contains any separators.
     *
     * @param reference string to check
     * @return true, if a separator is contained or false, if not
     */
    public static boolean containsSeparator(String reference) {
        for (String sep : CommonTextToolsConfig.SEPARATORS_TO_CONTAIN) {
            if (reference.contains(sep) && !reference.contentEquals(sep)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates {@link RecommendedInstance}s for the given {@link NounMapping}s using the given information about similar types and probability and type
     * mappings. Adds the created {@link RecommendedInstance}s to the given {@link RecommendationState}.
     *
     * @param similarTypes        The list of similar types
     * @param nameMappings        the noun mappings
     * @param typeMappings        the type mappings
     * @param recommendationState the state the new RecommendedInstances should be added to
     * @param claimant            the claimant that argued in favor of adding the RI
     * @param probability         the probability that should be annotated
     */
    public static void addRecommendedInstancesFromNounMappings(ImmutableList<String> similarTypes, ImmutableList<NounMapping> nameMappings,
            ImmutableList<NounMapping> typeMappings, RecommendationState recommendationState, Claimant claimant, double probability) {
        for (var nameMapping : nameMappings) {
            var name = nameMapping.getReference();
            for (var type : similarTypes) {
                recommendationState.addRecommendedInstance(name, type, claimant, probability, nameMappings, typeMappings);
            }
        }
    }

    /**
     * Retrieves a list of similar types in the given model state given the word.
     *
     * @param word  the word that might have type names in the model state
     * @param model the model containing information about types
     * @return List of type names in the model state that are similar to the given word
     */
    public static ImmutableList<String> getSimilarTypes(Word word, Model model) {
        var identifiers = getSplittedTypeIdentifiers(model);
        return Lists.immutable.fromStream(identifiers.stream().filter(typeId -> SimilarityUtils.getInstance().areWordsSimilar(typeId, word.getText())));
    }

    /**
     * Returns a set of identifiers for the types in the model.
     *
     * @param model the model containing type information
     * @return Set of identifiers for existing types
     */
    public static SortedSet<String> getSplittedTypeIdentifiers(Model model) {
        SortedSet<String> identifiers = model.getTypeIdentifiers()
                .stream()
                .map(CommonUtilities::splitSnakeAndKebabCase)
                .map(CommonUtilities::splitCamelCase)
                .map(type -> type.split(" "))
                .flatMap(Arrays::stream)
                .collect(Collectors.toCollection(TreeSet::new));
        identifiers.addAll(model.getTypeIdentifiers());
        return identifiers;
    }

    /**
     * Splits a given String at Snake and Kebab cases. For example, "test-string" and "test_string" become "test string".
     *
     * @param name the given name
     * @return the name split at snake and kebab case
     */
    public static String splitSnakeAndKebabCase(String name) {
        var joiner = new StringJoiner(" ");
        for (String namePart : name.split("[-_]")) {
            joiner.add(namePart);
        }
        return joiner.toString().replaceAll("\\s+", " ");
    }

    /**
     * Splits a given String at camel cases. For example, "testString" becomes "test String".
     *
     * @param name the given name
     * @return the name split at camel case
     */
    public static String splitCamelCase(String name) {
        var joiner = new StringJoiner(" ");
        for (String namePart : name.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
            joiner.add(namePart);
        }
        return joiner.toString().replaceAll("\\s+", " ");
    }

    /**
     * Splits a given String using {@link #splitCamelCase(String)} and {@link #splitSnakeAndKebabCase(String)}.
     *
     * @param name the given name
     * @return the split name
     */
    public static String splitCases(String name) {
        return splitCamelCase(splitSnakeAndKebabCase(name));
    }

    /**
     * Checks if the given name is a CamelCased-word.
     *
     * @param name the name to check
     * @return true if the given name is CamelCased
     */
    public static boolean nameIsCamelCased(String name) {
        var unCamelCased = CommonUtilities.splitCamelCase(name);
        return name.length() < unCamelCased.length();
    }

    /**
     * Checks if the given name is a snake_cased-word.
     *
     * @param name the name to check
     * @return true if the given name is snake_cased
     */
    public static boolean nameIsSnakeCased(String name) {
        var split = name.split("_");
        return split.length > 1;
    }

    /**
     * Checks if the given name is a kebab-cased-word.
     *
     * @param name the name to check
     * @return true if the given name is kebab-cased
     */
    public static boolean nameIsKebabCased(String name) {
        var split = name.split("-");
        return split.length > 1;
    }

    /**
     * Creates a reference given a list of words (compoundWords).
     *
     * @param compoundWords the given compoundWords
     * @return a reference that consists of the words in the given compoundWords
     */
    public static String createReferenceForCompound(ImmutableList<Word> compoundWords) {
        var sortedPhrase = compoundWords.toSortedListBy(Word::getPosition);
        var referenceJoiner = new StringJoiner(" ");
        for (var w : sortedPhrase) {
            referenceJoiner.add(w.getText());
        }
        return referenceJoiner.toString();
    }

    /**
     * Returns the compound words for the given word, if any.
     *
     * @param word the word to analyze
     * @return the list of compound words, or empty if none
     */
    public static ImmutableList<Word> getCompoundWords(Word word) {
        var deps = Lists.mutable.of(word);
        deps.addAll(word.getOutgoingDependencyWordsWithType(DependencyTag.COMPOUND).toList());
        var sortedWords = deps.toSortedListBy(Word::getPosition);
        if (deps.size() < 2) {
            return Lists.immutable.empty();
        }
        return Lists.immutable.ofAll(sortedWords);
    }

    /**
     * Check if the word is CamelCased. Additionally, the word needs to have a length > 4, otherwise it is probably only an abbreviation.
     *
     * @param word Word to check
     * @return true if the word is CamelCased and has a length greater than 4
     */
    public static boolean isCamelCasedWord(String word) {
        if (word.toUpperCase().equals(word)) {
            return false;
        }
        return word.length() > 4 && nameIsCamelCased(word);
    }

    /**
     * Checks a given list of {@link Word words} to find out if there are words that the given recommendedInstance has in its {@link NounMapping NounMappings}.
     *
     * @param wordList            the word list to check
     * @param recommendedInstance the RecommendedInstance in question
     * @return true if at least one word is also covered by the RecommendedInstance, else false
     */
    public static boolean wordListContainsAnyWordFromRecommendedInstance(ImmutableList<Word> wordList, RecommendedInstance recommendedInstance) {
        var recommendedInstanceWords = recommendedInstance.getNameMappings().flatCollect(NounMapping::getWords);
        for (var recommendedInstanceWord : recommendedInstanceWords) {
            if (wordList.contains(recommendedInstanceWord)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return the current time as String. Can be used for output to show the date of processing.
     *
     * @return the current time as String
     */
    public static String getCurrentTimeAsString() {
        return DATE_FORMATTER.format(LocalDateTime.now(ZoneId.systemDefault()));
    }

    /**
     * Reads text from the {@link InputStream} into a String.
     *
     * @param text the input stream
     * @return the text as String
     */
    public static String readInputText(InputStream text) {
        var scanner = new Scanner(text, StandardCharsets.UTF_8);
        scanner.useDelimiter("\\A");
        String inputText = scanner.next();
        scanner.close();
        return inputText;
    }

    /**
     * Reads the contents of a File into a String.
     *
     * @param textFile the file to be read
     * @return the content of the File as String
     */
    public static String readInputText(File textFile) {
        try {
            return readInputText(new FileInputStream(textFile));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e.getCause());
            return "";
        }
    }

}
