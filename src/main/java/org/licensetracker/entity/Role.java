package org.licensetracker.entity;

public enum Role {
    ROLE_USER,
    // 1. Administration & Core
    ROLE_ADMIN,
    ROLE_SECURITY_HEAD,
    ROLE_PRODUCT_OWNER,

    // 2. Compliance & Procurement
    ROLE_COMPLIANCE_LEAD,
    ROLE_COMPLIANCE_OFFICER,
    ROLE_PROCUREMENT_LEAD,
    ROLE_PROCUREMENT_OFFICER,
    ROLE_OPERATIONS_MANAGER,

    // 3. IT & Network
    ROLE_IT_AUDITOR,
    ROLE_NETWORK_ADMIN,
    ROLE_NETWORK_ENGINEER
}