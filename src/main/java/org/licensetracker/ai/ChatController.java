package org.licensetracker.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.licensetracker.dto.ChatRequestDto;
import org.licensetracker.dto.ChatResponseDto;
import org.licensetracker.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final AiChatAssistant chatAssistant;

    @PostMapping("/message")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_LEAD', 'PRODUCT_OWNER'))")
    public ResponseEntity<ChatResponseDto> sendMessage(@RequestBody ChatRequestDto request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String chatId = UUID.randomUUID().toString();

            log.info("Processing chat message for user: {}, chatId: {}", authentication.getName(), chatId);

            String response = chatAssistant.chat(chatId, request.getMessage());

            log.info("AI response generated for chatId: {}", chatId);

            return ResponseEntity.ok(new ChatResponseDto(response, chatId));

        } catch (Exception e) {
            log.error("Error processing chat message", e);
            return ResponseEntity.status(500).body(
                    new ChatResponseDto("Sorry, I encountered an error. Please try again.", null)
            );
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'PROCUREMENT_LEAD', 'PRODUCT_OWNER'))")
    @DeleteMapping("/clear/{chatId}")
    public ResponseEntity<Void> clearChatHistory(@PathVariable String chatId) {
        try {
            log.info("Chat history clear requested for chatId: {}", chatId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error clearing chat history", e);
            return ResponseEntity.status(500).build();
        }
    }
}
