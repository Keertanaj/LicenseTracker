package org.licensetracker.ai;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.licensetracker.service.ComplianceBotAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ai")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "http://localhost:3000")
public class AIController {

    private final ComplianceBotAgent complianceBotAgent;

    @Autowired
    public AIController(ComplianceBotAgent complianceBotAgent) {
        this.complianceBotAgent = complianceBotAgent;
    }

    @PostMapping("/query")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> getSummary(
            // Correctly accepts sessionId as a URL Query Parameter
            @RequestParam String sessionId,
            @RequestBody Map<String, Object> request) {

        String userQuery = (String) request.get("query");

        if (userQuery == null || userQuery.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Passes the sessionId to the agent for memory
        String botResponse = complianceBotAgent.chat(sessionId, userQuery);

        Map<String, String> response = Map.of("botResponse", botResponse);

        return ResponseEntity.ok(response);
    }
}