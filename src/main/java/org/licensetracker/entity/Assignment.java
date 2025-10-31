package org.licensetracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assignment")
@Builder
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assignmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    @JsonIgnore
    private Device device;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "license_key", nullable = false)
    @JsonIgnore
    private License license;

    @Column(nullable = false)
    private LocalDate assignedOn = LocalDate.now();
}