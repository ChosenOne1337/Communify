package ru.nsu.ccfit.mvcentertainment.communify.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.UserDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.PlaylistBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters.UserInfoDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.security.UserIdentityValidator;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.UserIconService;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(userService.getById(userId));
    }

    @PutMapping("{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long userId,
            @RequestBody @Valid UserInfoDto userInfoDto
    ) {
        UserIdentityValidator.validateUserId(userId);
        return ResponseEntity.ok(userService.updateUserInfo(userId, userInfoDto));
    }

    @PutMapping(
            value = "/{userId}/icon",
            consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<UserDto> setUserIcon(
            @PathVariable Long userId,
            HttpServletRequest request
    ) throws IOException {
        UserIdentityValidator.validateUserId(userId);
        InputStream imageInputStream = request.getInputStream();
        UserDto userDto = userIconService.setImage(userId, imageInputStream);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{userId}/icon")
    public ResponseEntity<StreamingResponseBody> getUserIcon(
            @PathVariable Long userId
    ) {
        File iconFile = userIconService.getImage(userId);
        return StreamingResponseFactory.getStreamingResponse(iconFile);
    }

    @DeleteMapping("/{userId}/icon")
    public ResponseEntity<UserDto> deleteUserIcon(
            @PathVariable Long userId
    ) {
        UserIdentityValidator.validateUserId(userId);
        return ResponseEntity.ok(userIconService.deleteImage(userId));
    }

    @GetMapping("/{userId}/playlists")
    public ResponseEntity<Page<PlaylistBriefDto>> getUserPlaylists(
            @PathVariable Long userId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getUserPlaylists(userId, pageable));
    }

    @GetMapping("/{userId}/owned-playlists")
    public ResponseEntity<Page<PlaylistBriefDto>> getUserOwnedPlaylists(
            @PathVariable Long userId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getOwnedPlaylists(userId, pageable));
    }

    @PutMapping("{userId}/playlists/{playlistId}")
    public ResponseEntity<UserDto> addPlaylist(
            @PathVariable Long userId,
            @PathVariable Long playlistId
    ) {
        UserIdentityValidator.validateUserId(userId);
        return ResponseEntity.ok(userService.addUserPlaylist(userId, playlistId));
    }

    @DeleteMapping("{userId}/playlists/{playlistId}")
    public ResponseEntity<UserDto> deletePlaylist(
            @PathVariable Long userId,
            @PathVariable Long playlistId
    ) {
        UserIdentityValidator.validateUserId(userId);
        return ResponseEntity.ok(userService.deleteUserPlaylist(userId, playlistId));
    }

}
