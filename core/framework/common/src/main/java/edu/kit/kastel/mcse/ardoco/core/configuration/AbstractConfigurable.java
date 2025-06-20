/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;

/**
 * Abstract base class for configurable components. Provides configuration application logic and utility methods for subclasses.
 */
@Deterministic
public abstract class AbstractConfigurable implements IConfigurable {
    /**
     * Connector for class attribute keys ("::").
     */
    public static final String CLASS_ATTRIBUTE_CONNECTOR = "::";
    /**
     * Connector for key-value pairs ("=").
     */
    public static final String KEY_VALUE_CONNECTOR = "=";
    /**
     * Separator for list values (",").
     */
    public static final String LIST_SEPARATOR = ",";

    @SuppressWarnings("java:S2065") // The logger is used in the subclasses that are serializable
    private transient Logger logger;

    private SortedMap<String, String> lastAppliedConfiguration = new TreeMap<>();

    @Override
    public final void applyConfiguration(SortedMap<String, String> additionalConfiguration) {
        this.applyConfiguration(additionalConfiguration, this, this.getClass());
        this.delegateApplyConfigurationToInternalObjects(additionalConfiguration);
        this.lastAppliedConfiguration = new TreeMap<>(additionalConfiguration);
    }

    @Override
    public SortedMap<String, String> getLastAppliedConfiguration() {
        return Collections.unmodifiableSortedMap(this.lastAppliedConfiguration);
    }

    protected abstract void delegateApplyConfigurationToInternalObjects(SortedMap<String, String> additionalConfiguration);

    private void applyConfiguration(SortedMap<String, String> additionalConfiguration, AbstractConfigurable configurable, Class<?> currentClassInHierarchy) {
        if (currentClassInHierarchy == Object.class || currentClassInHierarchy == AbstractConfigurable.class) {
            return;
        }

        if (currentClassInHierarchy.getAnnotation(NoConfiguration.class) != null) {
            this.getLogger().debug("Skipping configuration for class {}", currentClassInHierarchy.getSimpleName());
            return;
        }

        var fields = currentClassInHierarchy.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Configurable.class)) {
                continue;
            }
            String key = getKeyOfField(configurable, currentClassInHierarchy, field);
            if (additionalConfiguration.containsKey(key)) {
                this.setValue(field, additionalConfiguration.get(key));
            }
        }

        this.applyConfiguration(additionalConfiguration, configurable, currentClassInHierarchy.getSuperclass());
    }

    /**
     * Returns the key (for the configuration file) of a field. If the field is marked as ChildClassConfigurable, the key is based on the class of the
     * configurable object. Otherwise, the key is based on the class where the field is defined.
     *
     * @param configurable            the configurable object
     * @param currentClassInHierarchy the class where the field is defined
     * @param field                   the field
     * @return the key of the field
     */
    public static String getKeyOfField(AbstractConfigurable configurable, Class<?> currentClassInHierarchy, Field field) {
        Configurable configurableAnnotation = field.getAnnotation(Configurable.class);
        ChildClassConfigurable childClassConfigurableAnnotation = field.getAnnotation(ChildClassConfigurable.class);

        if (childClassConfigurableAnnotation != null && !configurableAnnotation.key().isBlank()) {
            throw new IllegalStateException("You cannot define a key for a field that is marked as ChildClassConfigurable.");
        }

        String classOfDefinition = childClassConfigurableAnnotation == null ? currentClassInHierarchy.getSimpleName() : configurable.getClass().getSimpleName();

        return configurableAnnotation.key().isBlank() ? (classOfDefinition + CLASS_ATTRIBUTE_CONNECTOR + field.getName()) : configurableAnnotation.key();
    }

    private void setValue(Field field, String value) {
        var clazz = field.getType();
        var parsedValue = this.parse(field, clazz, value);
        if (parsedValue == null) {
            return;
        }

        try {
            field.setAccessible(true);
            field.set(this, parsedValue);
        } catch (Exception e) {
            this.getLogger().error(e.getMessage(), e);
        }
    }

    private Object parse(Field field, Class<?> fieldsClass, String value) {
        if (fieldsClass == Integer.class || fieldsClass == Integer.TYPE) {
            return Integer.parseInt(value);
        }
        if (fieldsClass == Double.class || fieldsClass == Double.TYPE) {
            return Double.parseDouble(value);
        }
        if (fieldsClass == Boolean.class || fieldsClass == Boolean.TYPE) {
            return Boolean.parseBoolean(value);
        }
        if (fieldsClass.isEnum()) {
            var result = Arrays.stream(fieldsClass.getEnumConstants()).filter(c -> String.valueOf(c).equals(value)).findFirst();
            return result.orElseThrow(() -> new IllegalArgumentException("Unknown Enum Constant " + value));
        }

        if (List.class.isAssignableFrom(fieldsClass) && field.getGenericType() instanceof ParameterizedType parameterizedType) {
            var generics = parameterizedType.getActualTypeArguments();

            if (generics.length == 1 && generics[0] == String.class) {
                return new ArrayList<>(Arrays.stream(value.split(LIST_SEPARATOR)).toList());
            }
        }

        throw new IllegalArgumentException("Could not find a parse method for fields of type: " + fieldsClass);
    }

    /**
     * Returns the logger for this configurable instance.
     *
     * @return the logger
     */
    protected final Logger getLogger() {
        if (this.logger == null) {
            this.logger = LoggerFactory.getLogger(this.getClass());
        }
        return this.logger;
    }
}
