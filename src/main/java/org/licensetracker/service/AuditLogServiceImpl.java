package org.licensetracker.service;

import org.licensetracker.dto.AuditLogResponseDTO;
import org.licensetracker.entity.AuditLog;
import org.licensetracker.repository.AuditLogRepository;
import org.licensetracker.utility.AuditLogUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

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
