package org.licensetracker.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "license")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class License {
    @Id
    private String licenseKey;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

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