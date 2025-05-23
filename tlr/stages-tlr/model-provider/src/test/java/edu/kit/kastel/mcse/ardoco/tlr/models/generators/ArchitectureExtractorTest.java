/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.generators;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.kastel.mcse.ardoco.core.api.entity.Entity;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.ArchitectureModel;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture.ArchitectureComponent;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture.ArchitectureInterface;
import edu.kit.kastel.mcse.ardoco.core.api.models.arcotl.architecture.ArchitectureItem;
import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;

@Deterministic
public class ArchitectureExtractorTest {

    private static final Logger logger = LoggerFactory.getLogger(ArchitectureExtractorTest.class);

    public void checkModel(ArchitectureModel model) {
        Assertions.assertNotNull(model);

        List<? extends ArchitectureItem> modelContent = model.getContent();
        Assertions.assertFalse(modelContent.isEmpty());
        for (Entity archEndpoint : modelContent) {
            Assertions.assertNotNull(archEndpoint);
            if (archEndpoint instanceof ArchitectureComponent modelComponent) {
                logger.info("---");
                logger.info("{}", modelComponent);
                printInterfaces("Provided", modelComponent.getProvidedInterfaces());
                printInterfaces("Required", modelComponent.getRequiredInterfaces());
            }
        }
        logger.info("---");
    }

    public void printInterfaces(String description, Set<ArchitectureInterface> interfaces) {
        if (!interfaces.isEmpty()) {
            logger.info("{} Interfaces: ", description);
            interfaces.forEach(anInterface -> {
                Assertions.assertNotNull(anInterface);
                logger.info(" {} ", anInterface.getName());
                logger.info("  Methods:");
                anInterface.getSignatures().forEach(signature -> logger.info("  {}", signature));
            });
        }
    }

    @Test
    void extractorTest() {
        Assertions.assertTrue(true);
    }
}
