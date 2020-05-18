package ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters.UserAuthInfoDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.UserRepository;
import ru.nsu.ccfit.mvcentertainment.communify.backend.security.JwtTokenException;
import ru.nsu.ccfit.mvcentertainment.communify.backend.security.JwtTokenUtils;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.AuthenticationService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationServiceImpl(
            JwtTokenUtils jwtTokenUtils,
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public String loginUser(UserAuthInfoDto userAuthInfoDto) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userAuthInfoDto.getUserName(), userAuthInfoDto.getPassword()
            );

            authenticationManager.authenticate(authentication);
            User user = userRepository.findByName(userAuthInfoDto.getUserName());

            return jwtTokenUtils.generateToken(user.getName());
        } catch (AuthenticationException e) {
            throw new JwtTokenException(
                    "Invalid username/password supplied",
                    HttpStatus.UNPROCESSABLE_ENTITY.value()
            );
        }
    }

    @Override
    @Transactional
    public void registerUser(UserAuthInfoDto userAuthInfoDto) {
        if (userRepository.existsByName(userAuthInfoDto.getUserName())) {
            throw new JwtTokenException("Username already in use", HttpStatus.UNPROCESSABLE_ENTITY.value());
        }

        User user = new User();
        user.setName(userAuthInfoDto.getUserName());
        user.setPassword(passwordEncoder.encode(userAuthInfoDto.getPassword()));
        user.setBio("");

        userRepository.save(user);
    }
}
