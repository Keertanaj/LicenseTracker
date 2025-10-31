package org.licensetracker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentResponseDTO {
    private Integer assignmentId;
    private String deviceId;
    private String licenseId;
    private String softwareName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignmentDate;
}
