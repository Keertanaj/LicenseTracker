package org.licensetracker.service;

import org.licensetracker.dto.AuditLogRequestDTO;
import org.licensetracker.dto.AuditLogResponseDTO;
import org.licensetracker.entity.AuditLog;
import org.licensetracker.entity.User;
import org.licensetracker.repository.AuditLogRepository;
import org.licensetracker.repository.UserRepository;
import org.licensetracker.utility.AuditLogUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Map<String, Object> getCurrentUserInfo() {
        Map<String, Object> userInfo = new HashMap<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();

            if ("anonymousUser".equals(username)) {
                userInfo.put("userId", 0L); // System User ID
                return userInfo;
            }

            try {
                // Look up User ID from the database using the username/email
                User user = userRepository.findByEmail(username)
                        .orElse(null);

                if (user != null) {
                    userInfo.put("userId", user.getUserId());
                } else {
                    // Log warning and use fail-safe ID
                    System.err.println("WARN: Could not fetch user ID for principal: " + username);
                    userInfo.put("userId", -1L); // Dedicated 'Unknown' User ID
                }
            } catch (Exception e) {
                System.err.println("ERROR during user ID lookup: " + e.getMessage());
                userInfo.put("userId", -1L);
            }
        } else {
            userInfo.put("userId", -2L); // Unauthenticated ID
        }

        return userInfo;
    }


    @Override
    public void createLog(AuditLogRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Audit failed: User ID not found."));

        AuditLog log = new AuditLog();
        log.setUser(user);
        log.setEntityType(requestDTO.getEntityType());
        log.setEntityId(requestDTO.getEntityId());
        log.setAction(requestDTO.getAction());
        log.setDetails(requestDTO.getDetails());
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }

    @Override
    public List<AuditLogResponseDTO> getAuditLogs(LocalDate startDate, LocalDate endDate, Long userId, String entityType) {
        Specification<AuditLog> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("timestamp"), startDate.atStartOfDay()));
            }
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("timestamp"), endDate.plusDays(1).atStartOfDay()));
            }
            if (userId != null) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("userId"), userId));
            }
            if (entityType != null && !entityType.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("entityType"), entityType));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return auditLogRepository.findAll(spec).stream()
                .map(AuditLogUtility::toDto)
                .collect(Collectors.toList());
    }
}
