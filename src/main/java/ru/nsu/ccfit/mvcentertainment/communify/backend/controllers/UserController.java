package ru.nsu.ccfit.mvcentertainment.communify.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.UserDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable("id") Long userId
    ) {
        return ResponseEntity.ok(userService.getById(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(
            @RequestBody UserDto userDto
    ) {
        return ResponseEntity.ok(userService.create(userDto));
    }

    @PutMapping("{id}/update")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable("id") Long userId,
            @RequestBody UserDto userDto
    ) {
        return ResponseEntity.ok(userService.save(userId, userDto));
    }

    @PutMapping(
            value = "/{id}/icon",
            consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<UserDto> setUserIcon(
            @PathVariable("id") Long userId,
            HttpServletRequest request
    ) throws IOException {
        InputStream imageInputStream = request.getInputStream();
        UserDto userDto = userService.setUserIcon(userId, imageInputStream);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{id}/icon")
    public ResponseEntity<StreamingResponseBody> getUserIcon(
            @PathVariable("id") Long userId
    ) {
        // TODO: что, если иконки нет?
        File iconFile = userService.getUserIcon(userId);
        StreamingResponseBody responseBody = outputStream -> {
            Files.copy(iconFile.toPath(), outputStream);
        };

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=\"%s\"", iconFile.getName())
                ).contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(responseBody);
    }

}
