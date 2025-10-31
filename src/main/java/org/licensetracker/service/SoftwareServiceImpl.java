package org.licensetracker.service;

import org.licensetracker.repository.DeviceRepo;
import org.licensetracker.repository.SoftwareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SoftwareServiceImpl implements SoftwareService {

    @Autowired
    private SoftwareRepository softwareRepository;

    @Autowired
    private DeviceRepo deviceRepository;
/*
    @Override
    public SoftwareResponseDTO addSoftware(SoftwareRequestDTO request) {
        Software software = SoftwareUtility.toEntity(request, deviceRepository);
        Software savedSoftware = softwareRepository.save(software);
        return SoftwareUtility.toDto(savedSoftware);
    }

    @Override
    public List<SoftwareResponseDTO> getSoftwareByDeviceId(String deviceId) {
        return softwareRepository.findByDevice_DeviceId(deviceId).stream()
                .map(SoftwareUtility::toDto)
                .collect(Collectors.toList());
    }

 */

    @Override
    public List<String> getAllSoftwareNames() {
        return softwareRepository.findDistinctSoftwareNames();
    }
}
