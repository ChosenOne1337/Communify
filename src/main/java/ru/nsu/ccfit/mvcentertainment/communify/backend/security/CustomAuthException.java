package ru.nsu.ccfit.mvcentertainment.communify.backend.security;

public class CustomAuthException extends RuntimeException {

    private final int httpStatusCode;

    public CustomAuthException(String message, int httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
