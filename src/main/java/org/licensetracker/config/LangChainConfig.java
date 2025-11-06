package org.licensetracker.config;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class LangChainConfig {

    private static final Logger log = LoggerFactory.getLogger(LangChainConfig.class);
    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.withMaxMessages(10);
    }

    @Bean
    public ChatLanguageModel chatLanguageModel(Environment env) {

        String apiKey = env.getProperty("gemini.api.key");

        if (apiKey == null || apiKey.trim().isEmpty()) {
            apiKey = System.getenv("GEMINI_API_KEY");
        }

        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new RuntimeException(
                    "Error: GEMINI_API_KEY is not set. " +
                            "Please set it in application.properties as 'gemini.api.key' or " +
                            "as environment variable 'GEMINI_API_KEY'."
            );
        }

        log.info("Initializing Google Gemini Chat Model");

        // --- Model Builder (using the property found in Environment) ---
        String modelName = env.getProperty("gemini.model.name", "gemini-2.5-flash"); // Use a fallback default

        return GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(0.7)
                .maxOutputTokens(2048)
                .build();
    }
}