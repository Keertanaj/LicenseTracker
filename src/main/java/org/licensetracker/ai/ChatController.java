package org.licensetracker.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.licensetracker.dto.ChatRequestDto;
import org.licensetracker.dto.ChatResponseDto;
import org.licensetracker.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final AiChatAssistant chatAssistant;

    @PostMapping("/message")
    public ResponseEntity<ChatResponseDto> sendMessage(@RequestBody ChatRequestDto request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
                return ResponseEntity.status(401).body(
                        new ChatResponseDto("Please log in to use the chat assistant.", null)
                );
            }

            User currentUser = (User) authentication.getPrincipal();
            String chatId = "user-" + currentUser.getUserId();

            log.info("Processing chat message for user: {}, chatId: {}", currentUser.getEmail(), chatId);

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
