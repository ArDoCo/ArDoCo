/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.execution;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;
import edu.kit.kastel.mcse.ardoco.core.configuration.AbstractConfigurable;
import edu.kit.kastel.mcse.ardoco.core.configuration.Configurable;
import edu.kit.kastel.mcse.ardoco.core.configuration.ConfigurationInstantiatorUtils;

@Deterministic
public class ConfigurationHelper {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationHelper.class);

    private ConfigurationHelper() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    /**
     * Loads a file containing additional configurations and returns a map of configuration options.
     *
     * @param additionalConfigsFile the file containing the additional configurations
     * @return a map with the additional configurations
     */
    public static SortedMap<String, String> loadAdditionalConfigs(File additionalConfigsFile) {
        SortedMap<String, String> additionalConfigs = new TreeMap<>();
        if (additionalConfigsFile != null && (!additionalConfigsFile.exists() || !additionalConfigsFile.isFile())) {
            throw new IllegalArgumentException("File " + additionalConfigsFile.getAbsolutePath() + " is not a valid configuration file!");
        }
        if (additionalConfigsFile == null) {
            return additionalConfigs;
        }

        try (var scanner = new Scanner(additionalConfigsFile, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                var line = scanner.nextLine();
                if (line == null || line.isBlank()) {
                    continue;
                }
                var values = line.split(AbstractConfigurable.KEY_VALUE_CONNECTOR, 2);
                if (values.length != 2) {
                    logger.error(
                            "Found config line \"{}\". Layout has to be: 'KEY" + AbstractConfigurable.KEY_VALUE_CONNECTOR + "VALUE', e.g., 'SimpleClassName" + AbstractConfigurable.CLASS_ATTRIBUTE_CONNECTOR + "AttributeName" + AbstractConfigurable.KEY_VALUE_CONNECTOR + "42",
                            line);
                } else {
                    additionalConfigs.put(values[0], values[1]);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return additionalConfigs;
    }

    /**
     * Returns a map containing all default configuration options for all configurable classes in the ArDoCo framework.
     *
     * @return a map with all default configuration options
     */
    public static Map<String, String> getDefaultConfigurationOptions() {
        Map<String, String> configs = new TreeMap<>();
        var reflectAccess = new Reflections("edu.kit.kastel.mcse.ardoco");
        var classesThatMayBeConfigured = reflectAccess.getSubTypesOf(AbstractConfigurable.class)
                .stream()
                .filter(c -> c.getPackageName().startsWith("edu.kit.kastel.mcse.ardoco"))
                .filter(c -> !Modifier.isAbstract(c.getModifiers()))
                .filter(c -> !c.getPackageName().contains("tests"))
                .toList();
        for (var clazz : classesThatMayBeConfigured) {
            try {
                processConfigurationOfClass(configs, clazz);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
        return configs;
    }

    /**
     * Processes the configuration of a specific class by creating an instance and extracting all configurable fields.
     *
     * @param configs the map to store configuration options in
     * @param clazz   the class to process
     * @throws InvocationTargetException if the constructor cannot be invoked
     * @throws InstantiationException    if the class cannot be instantiated
     * @throws IllegalAccessException    if access to fields is denied
     */
    protected static void processConfigurationOfClass(Map<String, String> configs, Class<? extends AbstractConfigurable> clazz)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        var object = ConfigurationInstantiatorUtils.createObject(clazz);
        List<Field> fields = new ArrayList<>();
        findImportantFields(object.getClass(), fields);
        fillConfigs(object, fields, configs);
    }

    private static void fillConfigs(AbstractConfigurable object, List<Field> fields, Map<String, String> configs) throws IllegalAccessException {
        for (Field f : fields) {
            f.setAccessible(true);
            var key = AbstractConfigurable.getKeyOfField(object, f.getDeclaringClass(), f);
            var rawValue = f.get(object);
            var value = getValue(rawValue);
            if (configs.containsKey(key)) {
                throw new IllegalArgumentException("Found duplicate entry in map: " + key);
            }
            configs.put(key, value);
        }
    }

    private static String getValue(Object rawValue) {
        if (rawValue instanceof Integer i) {
            return Integer.toString(i);
        }
        if (rawValue instanceof Double d) {
            return String.format(Locale.ENGLISH, "%f", d);
        }
        if (rawValue instanceof Boolean b) {
            return String.valueOf(b);
        }
        if (rawValue instanceof List<?> s && s.stream().allMatch(String.class::isInstance)) {
            return s.stream().map(Object::toString).collect(Collectors.joining(AbstractConfigurable.LIST_SEPARATOR));
        }
        if (rawValue instanceof Enum<?> e) {
            return e.name();
        }

        throw new IllegalArgumentException("RawValue has no type that may be transformed to an Configuration" + rawValue + "[" + rawValue.getClass() + "]");

    }

    private static void findImportantFields(Class<?> clazz, List<Field> fields) {
        if (clazz == Object.class || clazz == AbstractConfigurable.class) {
            return;
        }

        for (var field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Configurable.class)) {
                fields.add(field);
            }
        }
        findImportantFields(clazz.getSuperclass(), fields);
    }
}
