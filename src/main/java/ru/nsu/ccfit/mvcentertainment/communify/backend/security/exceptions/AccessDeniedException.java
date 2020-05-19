package ru.nsu.ccfit.mvcentertainment.communify.backend.security.exceptions;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException() {
    }

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessDeniedException(Throwable cause) {
        super(cause);
    }

    private String formatMessage(String message) {
        return message == null ? "Access denied" : String.format("Access denied: %s", message);
    }

}
