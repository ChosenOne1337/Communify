package ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.UserDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters.UserAuthInfoDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.Mapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.UserRepository;
import ru.nsu.ccfit.mvcentertainment.communify.backend.security.JwtTokenUtils;
import ru.nsu.ccfit.mvcentertainment.communify.backend.security.exceptions.AuthException;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.AuthenticationService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Mapper<User, UserDto, Long> userMapper;

    @Autowired
    public AuthenticationServiceImpl(
            JwtTokenUtils jwtTokenUtils,
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            Mapper<User, UserDto, Long> userMapper) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public String loginUser(UserAuthInfoDto userAuthInfoDto) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userAuthInfoDto.getName(), userAuthInfoDto.getPassword()
            );

            authenticationManager.authenticate(authentication);
            User user = userRepository.findByName(userAuthInfoDto.getName());

            return jwtTokenUtils.generateToken(user.getId(), user.getName());
        } catch (AuthenticationException e) {
            throw new AuthException("Invalid username or password supplied");
        }
    }

    @Override
    @Transactional
    public UserDto registerUser(UserAuthInfoDto userAuthInfoDto) {
        if (userRepository.existsByName(userAuthInfoDto.getName())) {
            throw new AuthException(
                    String.format("Username '%s' is already in use", userAuthInfoDto.getName())
            );
        }

        User user = new User();
        user.setName(userAuthInfoDto.getName());
        user.setPassword(passwordEncoder.encode(userAuthInfoDto.getPassword()));
        user.setBio("");

        user = userRepository.save(user);
        return userMapper.toDto(user);
    }
}
