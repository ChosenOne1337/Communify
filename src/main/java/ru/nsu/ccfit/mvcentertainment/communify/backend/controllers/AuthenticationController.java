package ru.nsu.ccfit.mvcentertainment.communify.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters.UserAuthInfoDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.AuthenticationService;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(
            @RequestBody UserAuthInfoDto userAuthInfoDto
    ) {
        authenticationService.registerUser(userAuthInfoDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(
            @RequestBody UserAuthInfoDto userAuthInfoDto
    ) {
        String token = authenticationService.loginUser(userAuthInfoDto);
        return ResponseEntity.ok(token);
    }

}
