package org.licensetracker.service;

import org.licensetracker.entity.Device;
import org.licensetracker.entity.Installation;
import org.licensetracker.entity.Software;
import org.licensetracker.repository.InstallationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class InstallationServiceImpl implements InstallationService {

    @Autowired
    private InstallationRepository installationRepository;

    @Override
    public void createInstallation(Device device, Software software) {
        Installation installation = new Installation();
        installation.setDevice(device);
        installation.setSoftware(software);
        installationRepository.save(installation);
    }
}
