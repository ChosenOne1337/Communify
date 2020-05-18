package ru.nsu.ccfit.mvcentertainment.communify.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters.UserAuthInfoDto;

@RestController
public class AuthenticationController {


    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody UserAuthInfoDto userAuthInfoDto
    ) {
        return null;
    }

}
