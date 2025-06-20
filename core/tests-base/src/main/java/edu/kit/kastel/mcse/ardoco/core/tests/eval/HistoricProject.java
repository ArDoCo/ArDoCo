/* Licensed under MIT 2021-2025. */
package edu.kit.kastel.mcse.ardoco.core.tests.eval;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.kastel.mcse.ardoco.core.api.models.ModelFormat;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.ArchitectureComponentModel;
import edu.kit.kastel.mcse.ardoco.core.execution.ConfigurationHelper;
import edu.kit.kastel.mcse.ardoco.core.tests.eval.results.ExpectedResults;

/**
 * This enum captures the different case studies that are used for evaluation in the integration tests (only historic versions).
 */
public enum HistoricProject implements GoldStandardProject {
    TEASTORE_HISTORICAL( //
            "TS-H", //
            "/benchmark/teastore/model_2020/pcm/teastore.repository", //
            "/benchmark/teastore/text_2018/teastore_2018_AB.txt", //
            "/benchmark/teastore/goldstandards/goldstandard_sad_2018-sam_2020_AB.csv", //
            "/configurations/ts/filterlists_all.txt", // options: filterlists_none.txt, filterlists_onlyCommon.txt, filterlists_all.txt
            "/benchmark/teastore/goldstandards/goldstandard_sad_2018-sam_2020_AB_UME.csv", //
            new ExpectedResults(.999, .740, .850, .984, .853, .999), //
            new ExpectedResults(.082, .983, .153, .332, .147, .291) //
    ), //
    TEAMMATES_HISTORICAL( //
            "TM-H", //
            "/benchmark/teammates/model_2021/pcm/teammates.repository", //
            "/benchmark/teammates/text_2015/teammates_2015.txt", //
            "/benchmark/teammates/goldstandards/goldstandard_sad_2015-sam_2021.csv", //
            "/configurations/tm/filterlists_all.txt", // options: filterlists_none.txt, filterlists_onlyCommon.txt, filterlists_all.txt
            "/benchmark/teammates/goldstandards/goldstandard_sad_2015-sam_2021_UME.csv", //
            new ExpectedResults(.524, .695, .597, .970, .589, .979), //
            new ExpectedResults(.161, .695, .262, .878, .295, .884) //
    ), //
    BIGBLUEBUTTON_HISTORICAL( //
            "BBB-H", "/benchmark/bigbluebutton/model_2021/pcm/bbb.repository", //
            "/benchmark/bigbluebutton/text_2015/bigbluebutton_2015.txt", //
            "/benchmark/bigbluebutton/goldstandards/goldstandard_sad_2015-sam_2021.csv", //
            "/configurations/bbb/filterlists_all.txt", // options: filterlists_none.txt, filterlists_onlyCommon.txt, filterlists_all.txt
            "/benchmark/bigbluebutton/goldstandards/goldstandard_sad_2015-sam_2021_UME.csv", //
            new ExpectedResults(.807, .617, .699, .978, .695, .993), //
            new ExpectedResults(.048, .176, .076, .829, .018, .857) //
    ), //

    JABREF_HISTORICAL( //
            "JR-H", "/benchmark/jabref/model_2021/pcm/jabref.repository", //
            "/benchmark/jabref/text_2016/jabref_2016.txt", //
            "/benchmark/jabref/goldstandards/goldstandard_sad_2016-sam_2021.csv", //
            "/configurations/jabref/filterlists_all.txt", // options: filterlists_none.txt, filterlists_onlyCommon.txt, filterlists_all.txt
            "/benchmark/jabref/goldstandards/goldstandard_sad_2016-sam_2021_UME.csv", //
            new ExpectedResults(.817, .999, .899, .966, .886, .960), //
            new ExpectedResults(.083, .111, .095, .683, -0.094, .784) //
    );

    private static final Logger logger = LoggerFactory.getLogger(HistoricProject.class);

    private final String alias;
    private final String model;
    private final String textFile;
    private final String configurationsFile;
    private final String goldStandardTraceabilityLinkRecovery;
    private final String goldStandardMissingTextForModelElement;
    private final ExpectedResults expectedTraceLinkResults;
    private final ExpectedResults expectedInconsistencyResults;
    private final SortedSet<String> resourceNames;

    HistoricProject(String alias, String model, String textFile, String goldStandardTraceabilityLinkRecovery, String configurationsFile,
            String goldStandardMissingTextForModelElement, ExpectedResults expectedTraceLinkResults, ExpectedResults expectedInconsistencyResults) {
        this.alias = alias;
        this.model = model;
        this.textFile = textFile;
        this.configurationsFile = configurationsFile;
        this.goldStandardTraceabilityLinkRecovery = goldStandardTraceabilityLinkRecovery;
        this.goldStandardMissingTextForModelElement = goldStandardMissingTextForModelElement;
        this.expectedTraceLinkResults = expectedTraceLinkResults;
        this.expectedInconsistencyResults = expectedInconsistencyResults;
        resourceNames = new TreeSet<>(List.of(model, textFile, goldStandardTraceabilityLinkRecovery, configurationsFile,
                goldStandardMissingTextForModelElement));
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public File getModelFile() {
        return ProjectHelper.loadFileFromResources(model);
    }

    @Override
    public String getModelResourceName() {
        return model;
    }

    @Override
    public File getModelFile(ModelFormat modelType) {
        return switch (modelType) {
            case PCM -> getModelFile();
            case UML -> ProjectHelper.loadFileFromResources(model.replace("/pcm/", "/uml/").replace(".repository", ".uml"));
            case RAW -> throw new IllegalArgumentException("Raw model is not supported for this project.");
            case ACM -> throw new IllegalArgumentException("ACM model is not supported for this project.");
        };
    }

    @Override
    public String getModelResourceName(ModelFormat modelType) {
        return switch (modelType) {
            case PCM -> model;
            case UML -> model.replace("/pcm/", "/uml/").replace(".repository", ".uml");
            case RAW -> throw new IllegalArgumentException("Raw model is not supported for this project.");
            case ACM -> throw new IllegalArgumentException("ACM model is not supported for this project.");
        };
    }

    @Override
    public File getTextFile() {
        return ProjectHelper.loadFileFromResources(textFile);
    }

    @Override
    public String getTextResourceName() {
        return textFile;
    }

    @Override
    public SortedMap<String, String> getAdditionalConfigurations() {
        return ConfigurationHelper.loadAdditionalConfigs(getAdditionalConfigurationsFile());
    }

    @Override
    public File getAdditionalConfigurationsFile() {
        return ProjectHelper.loadFileFromResources(this.configurationsFile);
    }

    @Override
    public String getAdditionalConfigurationsResourceName() {
        return configurationsFile;
    }

    @Override
    public File getTlrGoldStandardFile() {
        return ProjectHelper.loadFileFromResources(goldStandardTraceabilityLinkRecovery);
    }

    @Override
    public String getTlrGoldStandardResourceName() {
        return goldStandardTraceabilityLinkRecovery;
    }

    @Override
    public ImmutableList<String> getTlrGoldStandard() {
        var path = Paths.get(this.getTlrGoldStandardFile().toURI());
        List<String> goldLinks = Lists.mutable.empty();
        try {
            goldLinks = Files.readAllLines(path);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        goldLinks.removeFirst();
        goldLinks.removeIf(String::isBlank);
        return Lists.immutable.ofAll(goldLinks);
    }

    @Override
    public GoldStandard getTlrGoldStandard(ArchitectureComponentModel architectureComponentModel) {
        return new GoldStandard(getTlrGoldStandardFile(), architectureComponentModel);
    }

    @Override
    public MutableList<String> getMissingTextForModelElementGoldStandard() {
        var path = Paths.get(this.getMissingTextForModelElementGoldStandardFile().toURI());
        List<String> goldLinks = Lists.mutable.empty();
        try {
            goldLinks = Files.readAllLines(path);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        goldLinks.remove("missingModelElementID");
        goldLinks.removeIf(String::isBlank);
        return Lists.mutable.ofAll(goldLinks);
    }

    @Override
    public File getMissingTextForModelElementGoldStandardFile() {
        return ProjectHelper.loadFileFromResources(goldStandardMissingTextForModelElement);
    }

    @Override
    public String getMissingTextForModelElementGoldStandardResourceName() {
        return goldStandardMissingTextForModelElement;
    }

    @Override
    public ExpectedResults getExpectedTraceLinkResults() {
        return expectedTraceLinkResults;
    }

    @Override
    public ExpectedResults getExpectedInconsistencyResults() {
        return expectedInconsistencyResults;
    }

    @Override
    public String getProjectName() {
        return this.name();
    }

    @Override
    public SortedSet<String> getResourceNames() {
        return new TreeSet<>(resourceNames);
    }
}
