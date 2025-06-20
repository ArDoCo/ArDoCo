/* Licensed under MIT 2023-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.text.providers.informants.corenlp.textprocessor;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.kastel.mcse.ardoco.core.api.text.Text;
import edu.kit.kastel.mcse.ardoco.core.textproviderjson.error.InvalidJsonException;
import edu.kit.kastel.mcse.ardoco.core.textproviderjson.error.NotConvertableException;
import edu.kit.kastel.mcse.ardoco.tlr.text.providers.informants.corenlp.config.ConfigManager;

/**
 * This text processor processes texts using CoreNLP.
 */
public class TextProcessor {

    private static final int MAX_FAILED_SERVICE_REQUESTS = 2;
    Logger logger = LoggerFactory.getLogger(TextProcessor.class);

    /**
     * processes and annotates a given text
     *
     * @param inputText the input text
     * @return the annotated text
     */
    public Text processText(String inputText) {
        boolean microserviceAvailable = MicroserviceChecker.isMicroserviceAvailable();
        if (ConfigManager.INSTANCE.getNlpProviderSource().equals("microservice") && microserviceAvailable) {
            int numberOfTry = 0;
            while (numberOfTry < MAX_FAILED_SERVICE_REQUESTS) {
                try {
                    logger.info("Processing text with CoreNLP microservice.");
                    return processService(inputText);
                } catch (IOException e) {
                    numberOfTry++;
                    logger.warn("Could not process text with CoreNLP microservice. Trying again. ", e);
                } catch (NotConvertableException | InvalidJsonException e) {
                    logger.warn("Could not process text with CoreNLP microservice. Text not convertable. ", e);
                    return processLocally(inputText);
                }
            }
            logger.warn("Could not process text with CoreNLP microservice. Processing locally instead.");
        }
        logger.info("Processing text locally.");
        return processLocally(inputText);
    }

    private Text processLocally(String inputText) {
        return new TextProcessorLocal().processText(inputText);
    }

    private Text processService(String inputText) throws IOException, NotConvertableException, InvalidJsonException {
        return new TextProcessorService().processText(inputText);
    }

}
