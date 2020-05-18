package ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters.UserAuthInfoDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.security.JwtTokenUtils;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.AuthenticationService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationServiceImpl(
            JwtTokenUtils jwtTokenUtils,
            AuthenticationManager authenticationManager
    ) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String generateTokenForUser(UserAuthInfoDto userAuthInfoDto) {
        return null;
    }
}
