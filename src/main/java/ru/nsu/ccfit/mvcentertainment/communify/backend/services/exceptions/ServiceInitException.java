package ru.nsu.ccfit.mvcentertainment.communify.backend.services.exceptions;

public class ServiceInitException extends RuntimeException {
    public ServiceInitException() {
    }

    public ServiceInitException(String message) {
        super(message);
    }

    public ServiceInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceInitException(Throwable cause) {
        super(cause);
    }
}
