package org.licensetracker.utility;

import org.licensetracker.dto.SoftwareRequestDTO;
import org.licensetracker.dto.SoftwareResponseDTO;
import org.licensetracker.entity.Device;
import org.licensetracker.entity.Software;
import org.licensetracker.repository.DeviceRepo;

public class SoftwareUtility {

    public static Software toEntity(SoftwareRequestDTO dto) {
        return Software.builder()
                .softwareName(dto.getSoftwareName())
                .currentVersion(dto.getCurrentVersion())
                .latestVersion(dto.getLatestVersion())
                .status(dto.getStatus())
                .lastChecked(dto.getLastChecked())
                .build();
    }

    public static SoftwareResponseDTO toDto(Software software) {
        return SoftwareResponseDTO.builder()
                .id(software.getId())
                .softwareName(software.getSoftwareName())
                .currentVersion(software.getCurrentVersion())
                .latestVersion(software.getLatestVersion())
                .status(software.getStatus())
                .lastChecked(software.getLastChecked())
                .build();
    }
}

