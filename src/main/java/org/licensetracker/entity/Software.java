package org.licensetracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "software")
public class Software {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sv_id")
    private Integer id;

    @Column(length = 100)
    private String softwareName;

    @Column(length = 20)
    private String currentVersion;

    @Column(length = 20)
    private String latestVersion;

    @Enumerated(EnumType.STRING)
    private SoftwareStatus status;

    private LocalDate lastChecked;
}
