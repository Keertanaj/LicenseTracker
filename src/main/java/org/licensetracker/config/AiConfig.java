package org.licensetracker.config;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import org.licensetracker.ai.AiChatAssistant;
//import org.licensetracker.ai.ChatAssistant;
import org.licensetracker.ai.ComplianceTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
    @Value("${GEMINI_API_KEY}")
   private String apiKey;

    @Bean
    public ChatMemoryStore chatMemoryStore() {
        return new InMemoryChatMemoryStore();
    }


    @Bean
    public ChatLanguageModel chatLanguageModel() {
        //String apiKey = System.getenv("GEMINI_API_KEY");

        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new RuntimeException(
                    "Error: GEMINI_API_KEY environment variable is not set. " +
                            "Please set it in your IDE's Run Configuration."
            );
        }

        return GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.0-flash")
                .temperature(0.5)
                .maxOutputTokens(2048)
                .build();
    }


    @Bean
    public AiChatAssistant chatAssistant(ChatLanguageModel chatLanguageModel, // <-- Spring finds our manual bean
                                         ChatMemoryStore chatMemoryStore,
                                         ComplianceTools credFlowTools) {

        return AiServices.builder(AiChatAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemoryProvider(chatId -> MessageWindowChatMemory.builder()
                        .chatMemoryStore(chatMemoryStore)
                        .maxMessages(20)
                        .id(chatId)
                        .build())
                .tools(credFlowTools)
                .build();
    }
}