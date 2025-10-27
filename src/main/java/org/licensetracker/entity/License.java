package org.licensetracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "license")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class License {
    @Id
    private String licenseKey;

    private String vendor;

    private String softwareName;

    @Enumerated(EnumType.STRING)
    private LicenseType licenseType;

    private LocalDate validFrom;

    private LocalDate validTo;

    private Integer maxUsage;

    @Column(length = 1000)
    private String notes;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}