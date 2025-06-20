/* Licensed under MIT 2022-2025. */
package edu.kit.kastel.mcse.ardoco.core.configuration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a field as configurable. Should be used with {@link IConfigurable}. The field should not be final or static.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface Configurable {
    /**
     * Optional key for the configuration property. If not set, a default key is generated.
     *
     * @return the configuration key
     */
    String key() default "";
}
