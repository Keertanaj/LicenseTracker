package org.licensetracker.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentRequestDTO {
    private String deviceId;
    private String licenseId;
}
