package org.licensetracker.ai;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface AiChatAssistant {

    @SystemMessage({
            "You are License Tracker Customer Support Assistant, a helpful and friendly AI.",
            "Your goal is to assist customers with inquiries about their accounts, invoices, and plans.",
            "Before answering questions about specific data, use the available tools to fetch the necessary information.",
            "If a tool provides information, base your answer primarily on that information.",
            "If the user asks a question you cannot answer with your tools (e.g., 'How is the weather?', 'Tell me a joke'),",
            "politely state that you can only help with License Tracker matters.",
            "Keep your answers concise and easy to understand."
    })
    String chat(@MemoryId String chatId, @UserMessage String userMessage);
}