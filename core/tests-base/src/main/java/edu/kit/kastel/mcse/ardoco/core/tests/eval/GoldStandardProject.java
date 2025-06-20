/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.core.tests.eval;

import java.io.File;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.prefs.Preferences;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.slf4j.LoggerFactory;

import edu.kit.kastel.mcse.ardoco.core.api.models.ModelFormat;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.ArchitectureComponentModel;
import edu.kit.kastel.mcse.ardoco.core.tests.eval.results.ExpectedResults;

/**
 * Interface for all Project extensions
 */
public interface GoldStandardProject {
    /**
     * {@return the project the instance is based on}
     */
    String getProjectName();

    /**
     * {@return the name of all resources associated with instances relative to the class}
     */
    SortedSet<String> getResourceNames();

    /**
     * {@return the version of the source files of this project}
     */
    default long getSourceFilesVersion() {
        this.getResourceNames().forEach(this::validateResourceChecksum);
        return Preferences.userNodeForPackage(getClass()).getLong("version", -1L);
    }

    /**
     * Calculates an MD5 checksum for the resource at the given path. Will bump the source files version if the checksum doesn't match.
     *
     * @param resourceName the resource relative to the class
     * @return true, if the checksum matches, false otherwise
     * @see #getSourceFilesVersion()
     */
    private boolean validateResourceChecksum(String resourceName) {
        var cls = getClass();
        var logger = LoggerFactory.getLogger(cls);
        try (var resource = cls.getResourceAsStream(resourceName)) {
            if (resource == null) {
                throw new MissingResourceException("No such resource at path " + resourceName, File.class.getSimpleName(), resourceName);
            }
            String md5 = DigestUtils.md5Hex(resource);
            if (!Objects.equals(Preferences.userNodeForPackage(cls).get(resourceName, null), md5)) {
                Preferences.userNodeForPackage(cls).put(resourceName, md5);
                Preferences.userNodeForPackage(cls).putLong("version", System.currentTimeMillis());
                logger.info("Checksum for source file {} doesn't match", resourceName);
                return false;
            }
            logger.info("Checksum for source file {} matches", resourceName);
            return true;
        } catch (IOException e) {
            logger.error("Couldn't calculate checksum for resource at {}", resourceName, e);
            return false;
        }
    }

    /**
     * {@return the project alias}
     */
    default String getAlias() {
        return this.getProjectOrThrow().getAlias();
    }

    /**
     * Returns the File that represents the model for this project.
     *
     * @return the File that represents the model for this project
     */
    default File getModelFile() {
        return this.getProjectOrThrow().getModelFile();
    }

    /**
     * {@return the resource name that represents the model for this project}
     */
    default String getModelResourceName() {
        return this.getProjectOrThrow().getModelResourceName();
    }

    /**
     * Returns the File that represents the model for this project with the given model type.
     *
     * @param modelType the model type
     * @return the File that represents the model for this project
     */
    default File getModelFile(ModelFormat modelType) {
        return this.getProjectOrThrow().getModelFile(modelType);
    }

    /**
     * {@return the resource name that represents the model for this project with the given model type}
     *
     * @param modelType the model type
     */
    default String getModelResourceName(ModelFormat modelType) {
        return this.getProjectOrThrow().getModelResourceName(modelType);
    }

    /**
     * Returns the File that represents the text for this project.
     *
     * @return the File that represents the text for this project
     */
    default File getTextFile() {
        return this.getProjectOrThrow().getTextFile();
    }

    /**
     * {@return the resource name that represents the text for this project}
     */
    default String getTextResourceName() {
        return this.getProjectOrThrow().getTextResourceName();
    }

    /**
     * Return the map of additional configuration options
     *
     * @return the map of additional configuration options
     */
    default SortedMap<String, String> getAdditionalConfigurations() {
        return this.getProjectOrThrow().getAdditionalConfigurations();
    }

    /**
     * Returns a {@link File} that points to the text file containing additional configurations
     *
     * @return the file for additional configurations
     */
    default File getAdditionalConfigurationsFile() {
        return this.getProjectOrThrow().getAdditionalConfigurationsFile();
    }

    /**
     * {@return the resource name that represents the additional configurations for this project}
     */
    default String getAdditionalConfigurationsResourceName() {
        return this.getProjectOrThrow().getAdditionalConfigurationsResourceName();
    }

    /**
     * Returns the {@link GoldStandard} for this project.
     *
     * @return the File that represents the gold standard for this project
     */
    default File getTlrGoldStandardFile() {
        return this.getProjectOrThrow().getTlrGoldStandardFile();
    }

    /**
     * {@return the resource name that represents the TLR {@link GoldStandard} for this project}
     */
    default String getTlrGoldStandardResourceName() {
        return this.getProjectOrThrow().getTlrGoldStandardResourceName();
    }

    /**
     * Returns a string-list of entries as goldstandard for TLR for this project.
     *
     * @return a list with the entries of the goldstandard for TLR
     */
    default ImmutableList<String> getTlrGoldStandard() {
        return this.getProjectOrThrow().getTlrGoldStandard();
    }

    /**
     * Returns the {@link GoldStandard} for this project for the given model connector.
     *
     * @param architectureComponentModel the model
     * @return the {@link GoldStandard} for this project
     */
    default GoldStandard getTlrGoldStandard(ArchitectureComponentModel architectureComponentModel) {
        return this.getProjectOrThrow().getTlrGoldStandard(architectureComponentModel);
    }

    default MutableList<String> getMissingTextForModelElementGoldStandard() {
        return this.getProjectOrThrow().getMissingTextForModelElementGoldStandard();
    }

    /**
     * {@return the {@link GoldStandard} for this project}
     */
    default File getMissingTextForModelElementGoldStandardFile() {
        return this.getProjectOrThrow().getMissingTextForModelElementGoldStandardFile();
    }

    /**
     * {@return the resource name that represents the MME {@link GoldStandard} for this project}
     */
    default String getMissingTextForModelElementGoldStandardResourceName() {
        return this.getProjectOrThrow().getMissingTextForModelElementGoldStandardResourceName();
    }

    /**
     * {@return the expected results for Traceability Link Recovery}
     */
    default ExpectedResults getExpectedTraceLinkResults() {
        return this.getProjectOrThrow().getExpectedTraceLinkResults();
    }

    /**
     * {@return the expected results for Inconsistency Detection}
     */
    default ExpectedResults getExpectedInconsistencyResults() {
        return this.getProjectOrThrow().getExpectedInconsistencyResults();
    }

    /**
     * Private so the project doesn't get passed around directly, defeating the purpose of making it extensible
     *
     * @return the project this instance belongs to
     */
    private Project getProjectOrThrow() {
        return this.getFromName().orElseThrow();
    }

    /**
     * Returns an {@link Optional} containing the project that has a name that equals the given name, ignoring case.
     *
     * @return the Optional containing the project with the given name or is empty if no such is found.
     */
    private Optional<Project> getFromName() {
        for (Project project : Project.values()) {
            if (project.name().equalsIgnoreCase(this.getProjectName())) {
                return Optional.of(project);
            }
        }
        return Optional.empty();
    }
}
