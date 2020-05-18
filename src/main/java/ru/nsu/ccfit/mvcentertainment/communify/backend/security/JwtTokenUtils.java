package ru.nsu.ccfit.mvcentertainment.communify.backend.security;

public interface JwtTokenUtils {

    String generateToken(Long userId, String userName);

    JwtTokenInfo parseToken(String token) throws JwtTokenException;

    boolean validateToken(String token) throws JwtTokenException;

}
