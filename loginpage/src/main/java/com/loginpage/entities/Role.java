package com.loginpage.entities;

public enum Role {
    ROLE_USER,
    // 1. Administration & Core
    ROLE_ADMIN,                 // Add User, Ask AI Assistant
    ROLE_SECURITY_HEAD,         // View Audit Logs
    ROLE_PRODUCT_OWNER,         // Generate User Training Checklist

    // 2. Compliance & Procurement
    ROLE_COMPLIANCE_LEAD,       // Check License Utilization
    ROLE_COMPLIANCE_OFFICER,    // Export Report, Find Non-Compliant Devices
    ROLE_PROCUREMENT_LEAD,      // Forecast Renewals
    ROLE_PROCUREMENT_OFFICER,   // Add Software License
    ROLE_OPERATIONS_MANAGER,    // Receive Alert, Decommission Device

    // 3. IT & Network
    ROLE_IT_AUDITOR,            // Find Non-Compliant Devices
    ROLE_NETWORK_ADMIN,         // Add Device, Assign License, Bulk Upload
    ROLE_NETWORK_ENGINEER       // Track Software Versions
}