package org.licensetracker.exception;

public class SoftwareAlreadyCurrentException extends RuntimeException {
    public SoftwareAlreadyCurrentException(String message) {
        super(message);
    }
}
