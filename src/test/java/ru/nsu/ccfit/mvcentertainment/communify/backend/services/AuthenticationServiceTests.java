package ru.nsu.ccfit.mvcentertainment.communify.backend.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.nsu.ccfit.mvcentertainment.communify.backend.AppConfig;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.UserDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters.UserAuthInfoDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl.UserMapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.UserRepository;
import ru.nsu.ccfit.mvcentertainment.communify.backend.security.JwtTokenInfo;
import ru.nsu.ccfit.mvcentertainment.communify.backend.security.JwtTokenUtils;
import ru.nsu.ccfit.mvcentertainment.communify.backend.security.exceptions.AuthException;
import ru.nsu.ccfit.mvcentertainment.communify.backend.security.impl.JwtTokenUtilsImpl;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl.AuthenticationServiceImpl;

import java.util.HashSet;
import java.util.Set;

public class AuthenticationServiceTests {

    Set<User> userSet = new HashSet<>();

    @Mock
    UserRepository userRepository;

    @Mock
    AuthenticationManager authManager;

    @Mock
    PasswordEncoder passwordEncoder;

    static UserMapper userMapper;
    static JwtTokenUtils jwtTokenUtils;

    AuthenticationService authService;

    @BeforeAll
    static void setup() {
        ModelMapper modelMapper = new AppConfig().modelMapper();
        userMapper = new UserMapper(modelMapper);

        jwtTokenUtils = new JwtTokenUtilsImpl(
                "Y3NlcmduY2lzZXVna2J1dnlzbmJ" +
                "rZWN1cm14dmd1ZW52Z2p4dG1ldnJ5amd4" +
                "dnN1dGVndm5hbXV5ZXZndWxpZ2xpeWd3eWd3Z==",
                1000000);
    }

    @BeforeEach
    void initService() {
        MockitoAnnotations.initMocks(this);

        authService = new AuthenticationServiceImpl(
                jwtTokenUtils,
                authManager,
                userRepository,
                passwordEncoder,
                userMapper
        );

        userSet.clear();

        Mockito.when(passwordEncoder.encode(
                Mockito.anyString()
        )).then(invocation -> invocation.getArgument(0));

        Mockito.when(userRepository.findByName(
                Mockito.anyString()
        )).then(invocation -> {
            String name = invocation.getArgument(0);
            return userSet.stream()
                    .filter(u -> u.getName().equals(name))
                    .findAny()
                    .orElse(null);
        });

        Mockito.when(userRepository.existsByName(
                Mockito.anyString()
        )).then(invocation -> {
            String name = invocation.getArgument(0);
            return userSet.stream()
                    .anyMatch(u -> u.getName().equals(name));
        });

        Mockito.when(userRepository.save(
                Mockito.any()
        )).then(invocation -> {
            User user = invocation.getArgument(0);
            user.setId((long) userSet.size());
            userSet.add(user);
            return user;
        });

        Mockito.when(authManager.authenticate(
                Mockito.any()
        )).then(invocation -> {
            Authentication authentication = invocation.getArgument(0);
            String name = (String) authentication.getPrincipal();
            String password = (String) authentication.getCredentials();
            String encodedPassword = passwordEncoder.encode(password);

            User user = userRepository.findByName(name);
            if (user == null || !user.getPassword().equals(encodedPassword)) {
                throw new BadCredentialsException("");
            }

            return authentication;
        });
    }

    @Test
    void test() {
        // nonexistent user login error check
        UserAuthInfoDto userAuthInfoDto = new UserAuthInfoDto(
                "nonexistent_user", "password"
        );
        Assertions.assertThrows(
                AuthException.class,
                () -> authService.loginUser(userAuthInfoDto)
        );

        // normal register scenario check
        UserDto userDto = authService.registerUser(userAuthInfoDto);
        Assertions.assertEquals(userDto.getName(), userAuthInfoDto.getName());

        // repeated registration error check
        Assertions.assertThrows(
                AuthException.class,
                () -> authService.registerUser(userAuthInfoDto)
        );

        // normal login scenario check
        String jwtToken = authService.loginUser(userAuthInfoDto);
        JwtTokenInfo jwtTokenInfo = jwtTokenUtils.parseToken(jwtToken);
        Assertions.assertEquals(userAuthInfoDto.getName(), jwtTokenInfo.getUserName());

        // bad credentials error check
        userAuthInfoDto.setPassword("bad_password");
        Assertions.assertThrows(
                AuthException.class,
                () -> authService.loginUser(userAuthInfoDto)
        );
    }

}
