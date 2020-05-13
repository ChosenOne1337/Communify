package ru.nsu.ccfit.mvcentertainment.communify.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.UserDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.PlaylistBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.UserIconService;
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
    private final UserIconService userIconService;

    @Autowired
    public UserController(
            UserService userService,
            UserIconService userIconService
    ) {
        this.userService = userService;
        this.userIconService = userIconService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @RequestBody UserDto userDto
    ) {
        return ResponseEntity.ok(userService.create(userDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable("id") Long userId
    ) {
        return ResponseEntity.ok(userService.getById(userId));
    }

    @PutMapping("{id}")
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
        UserDto userDto = userIconService.setImage(userId, imageInputStream);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{id}/icon")
    public ResponseEntity<StreamingResponseBody> getUserIcon(
            @PathVariable("id") Long userId
    ) {
        File iconFile = userIconService.getImage(userId);
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

    @DeleteMapping("/{id}/icon")
    public ResponseEntity<UserDto> deleteUserIcon(
            @PathVariable("id") Long userId
    ) {
        return ResponseEntity.ok(userIconService.deleteImage(userId));
    }

    @GetMapping("/{id}/playlists")
    public ResponseEntity<Page<PlaylistBriefDto>> getUserPlaylists(
            @PathVariable("id") Long userId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getPlaylists(userId, pageable));
    }

    @GetMapping("/{id}/owned-playlists")
    public ResponseEntity<Page<PlaylistBriefDto>> getUserOwnedPlaylists(
            @PathVariable("id") Long userId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getOwnedPlaylists(userId, pageable));
    }

    @PutMapping("{user-id}/playlists/{playlist-id}")
    public ResponseEntity<UserDto> addPlaylist(
            @PathVariable("user-id") Long userId,
            @PathVariable("playlist-id") Long playlistId
    ) {
        return ResponseEntity.ok(userService.addPlaylist(userId, playlistId));
    }

    @DeleteMapping("{userId}/playlists/{playlistId}")
    public ResponseEntity<UserDto> deletePlaylist(
            @PathVariable("userId") Long userId,
            @PathVariable("playlistId") Long playlistId
    ) {
        return ResponseEntity.ok(userService.deletePlaylist(userId, playlistId));
    }

}
