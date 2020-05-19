package ru.nsu.ccfit.mvcentertainment.communify.backend.security;

import ru.nsu.ccfit.mvcentertainment.communify.backend.security.exceptions.JwtValidationException;

public interface JwtTokenUtils {

    String generateToken(Long userId, String userName);

    JwtTokenInfo parseToken(String token) throws JwtValidationException;

    boolean validateToken(String token) throws JwtValidationException;

}
