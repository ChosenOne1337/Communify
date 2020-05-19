package ru.nsu.ccfit.mvcentertainment.communify.backend.security.exceptions;

public class JwtValidationException extends RuntimeException {

    public JwtValidationException() {
    }

    public JwtValidationException(String message) {
        super(message);
    }

    public JwtValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtValidationException(Throwable cause) {
        super(cause);
    }

}
