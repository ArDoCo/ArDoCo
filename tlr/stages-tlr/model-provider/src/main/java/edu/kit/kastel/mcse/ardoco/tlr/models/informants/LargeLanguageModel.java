/* Licensed under MIT 2024-2025. */
package edu.kit.kastel.mcse.ardoco.tlr.models.informants;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import edu.kit.kastel.mcse.ardoco.core.architecture.Deterministic;
import edu.kit.kastel.mcse.ardoco.core.common.util.Environment;

@Deterministic
public enum LargeLanguageModel {
    // OPENAI
    GPT_4_O_MINI("GPT-4o mini", () -> createOpenAiModel("gpt-4o-mini-2024-07-18")), //
    GPT_4_O("GPT-4o", () -> createOpenAiModel("gpt-4o-2024-08-06")), //
    OPENAI_GENERIC(Environment.getEnv("OPENAI_MODEL_NAME"), () -> createOpenAiModel(Environment.getEnv("OPENAI_MODEL_NAME"))), //
    // OLLAMA
    LLAMA_3_1_8B("Llama3.1 8b", () -> createOllamaModel("llama3.1:8b-instruct-fp16")), //
    OLLAMA_GENERIC(Environment.getEnv("OLLAMA_MODEL_NAME"), () -> createOllamaModel(Environment.getEnv("OLLAMA_MODEL_NAME")));

    private static final Logger logger = LoggerFactory.getLogger(LargeLanguageModel.class);

    private final Supplier<ChatModel> creator;
    private final String humanReadableName;

    LargeLanguageModel(String humanReadableName, Supplier<ChatModel> creator) {
        this.humanReadableName = humanReadableName;
        this.creator = creator;
    }

    public String getHumanReadableName() {
        return humanReadableName;
    }

    public ChatModel create() {
        return new CachedChatLanguageModel(creator.get(), this.name());
    }

    public boolean isGeneric() {
        return this.name().endsWith("_GENERIC");
    }

    public boolean isOpenAi() {
        return this.name().startsWith("GPT_");
    }

    private static final int SEED = loadSeed();

    private static int loadSeed() {
        String seedEnv = Environment.getEnv("SEED");
        if (seedEnv == null) {
            return 422413373;
        }
        try {
            logger.info("Using SEED: {}", seedEnv);
            return Integer.parseInt(seedEnv);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid SEED environment variable: " + seedEnv, e);
        }
    }

    /**
     * Creates an OpenAI chat model instance.
     * Requires OpenAI organization ID and API key to be set in environment variables.
     *
     * @param model The name of the model to use
     * @return A configured OpenAI chat model instance
     * @throws IllegalStateException If required environment variables are not set
     */
    private static OpenAiChatModel createOpenAiModel(String model) {
        String openAiOrganizationId = Environment.getEnv("OPENAI_ORGANIZATION_ID");
        String openAiApiKey = Environment.getEnv("OPENAI_API_KEY");
        if (openAiOrganizationId == null || openAiApiKey == null) {
            throw new IllegalStateException("OPENAI_ORGANIZATION_ID or OPENAI_API_KEY environment variable not set");
        }
        return new OpenAiChatModel.OpenAiChatModelBuilder().modelName(model)
                .organizationId(openAiOrganizationId)
                .apiKey(openAiApiKey)
                .temperature(0.0)
                .seed(SEED)
                .build();
    }

    private static ChatModel createOllamaModel(String model) {
        String host = Environment.getEnv("OLLAMA_HOST");
        String user = Environment.getEnv("OLLAMA_USER");
        String password = Environment.getEnv("OLLAMA_PASSWORD");
        String token = Environment.getEnv("OLLAMA_TOKEN");

        var ollama = OllamaChatModel.builder().baseUrl(host).modelName(model).timeout(Duration.ofMinutes(15)).temperature(0.0).seed(SEED);
        if (user != null && password != null && !user.isEmpty() && !password.isEmpty()) {
            ollama.customHeaders(Map.of("Authorization", "Basic " + Base64.getEncoder()
                    .encodeToString((user + ":" + password).getBytes(StandardCharsets.UTF_8))));
        } else if (token != null && !token.isEmpty()) {
            // Default to OpenAI API token authentication
            return OpenAiChatModel.builder().baseUrl(host).modelName(model).apiKey(token).timeout(Duration.ofMinutes(15)).temperature(0.0).seed(SEED).build();
        }
        return ollama.build();
    }
}
