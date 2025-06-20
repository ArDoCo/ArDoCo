/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.core.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides access to configuration resources from files or classpath resources.
 * Allows retrieval of properties as different types (String, boolean, int, double, list).
 */
public final class ResourceAccessor {

    private final Properties prop = new Properties();
    private static final Logger logger = LoggerFactory.getLogger(ResourceAccessor.class);

    /**
     * Instantiates a new resource accessor.
     *
     * @param filepath    the filepath to the resource
     * @param isClasspath indicator whether the file path is a classpath path
     */
    public ResourceAccessor(String filepath, boolean isClasspath) {
        if (isClasspath) {
            try (var inputStream = this.getClass().getResourceAsStream(filepath)) {
                this.prop.load(inputStream);
            } catch (IOException e) {
                logger.debug(e.getMessage(), e.getCause());
            }
        } else {
            try (var inputStream = new FileInputStream(filepath)) {
                this.prop.load(inputStream);
            } catch (IOException e) {
                logger.debug(e.getMessage(), e.getCause());
            }
        }
    }

    /**
     * Returns the specified property of the config file as a string.
     *
     * @param key name of the specified property
     * @return value of the property as a string, or null if not found
     */
    public String getProperty(String key) {
        return this.prop.getProperty(key);
    }

    /**
     * Returns the specified property of the config file as a boolean if it is set.
     *
     * @param key name of the specified property
     * @return value of the property as a boolean. True, if the value for the key is "true", "yes", or "1" (ignoring case).
     */
    public boolean isPropertyEnabled(String key) {
        var propValue = this.prop.getProperty(key).strip();
        return Boolean.parseBoolean(propValue) || propValue.equalsIgnoreCase("yes") || propValue.equalsIgnoreCase("1");
    }

    /**
     * Returns the specified property of the config file as a double.
     *
     * @param key name of the specified property
     * @return value of the property as a double, or -1 if not parsable
     */
    public double getPropertyAsDouble(String key) {
        try {
            return Double.parseDouble(this.prop.getProperty(key));
        } catch (NumberFormatException n) {
            logger.debug(n.getMessage(), n.getCause());
            return -1;
        }
    }

    /**
     * Returns the specified property of the config file as an int.
     *
     * @param key name of the specified property
     * @return value of the property as an int, or -1 if not parsable
     */
    public int getPropertyAsInt(String key) {
        try {
            return Integer.parseInt(this.prop.getProperty(key));
        } catch (NumberFormatException n) {
            logger.debug(n.getMessage(), n.getCause());
            return -1;
        }
    }

    /**
     * Returns the specified property of the config file as a list of strings.
     *
     * @param key name of the specified property
     * @return value of the property as a list of strings
     * @throws IllegalArgumentException if the key is not found in the config
     */
    public ImmutableList<String> getPropertyAsList(String key) {
        MutableList<String> values = Lists.mutable.empty();
        String value = this.prop.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Key: " + key + " not found in config");
        }
        if (value.isBlank()) {
            return values.toImmutable();
        }
        values.addAll(Lists.immutable.with(value.split(" ")).castToCollection());
        values.removeIf(String::isBlank);
        return values.toImmutable();
    }
}
