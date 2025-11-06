package org.licensetracker.exception;

public class SoftwareNotInstalledException extends RuntimeException {
    public SoftwareNotInstalledException(String message) {
        super(message);
    }
}
