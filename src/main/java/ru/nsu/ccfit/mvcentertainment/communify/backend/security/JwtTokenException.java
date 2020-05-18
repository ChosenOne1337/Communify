package ru.nsu.ccfit.mvcentertainment.communify.backend.security;

public class JwtTokenException extends RuntimeException {

    private final int httpStatusCode;

    public JwtTokenException(String message, int httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
