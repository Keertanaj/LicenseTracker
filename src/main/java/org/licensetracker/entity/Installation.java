package org.licensetracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Device_Software_Installation")
public class Installation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "installation_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "software_id", nullable = false)
    private Software software;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @CreationTimestamp
    private LocalDateTime installationDate;
}
