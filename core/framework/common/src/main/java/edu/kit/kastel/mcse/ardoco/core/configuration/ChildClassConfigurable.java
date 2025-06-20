/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.configuration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a field as configurable based on the child class. The configuration key is based on the actual class, not the declaring class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface ChildClassConfigurable {
}
