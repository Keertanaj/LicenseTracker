package org.licensetracker.service;

import org.licensetracker.entity.Device;
import org.licensetracker.entity.Software;

public interface InstallationService {
    void createInstallation(Device device, Software software);
}
