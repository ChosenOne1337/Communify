package ru.nsu.ccfit.mvcentertainment.communify.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.PlaylistDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.TrackDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.PlaylistBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.PlaylistCoverService;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.PlaylistService;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.TrackService;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    private final UserService userService;
    private final TrackService trackService;
    private final PlaylistService playlistService;
    private final PlaylistCoverService playlistCoverService;

    @Autowired
    public PlaylistController(
            UserService userService,
            TrackService trackService,
            PlaylistService playlistService,
            PlaylistCoverService playlistCoverService
    ) {
        this.userService = userService;
        this.trackService = trackService;
        this.playlistService = playlistService;
        this.playlistCoverService = playlistCoverService;
    }

    @PostMapping
    public ResponseEntity<PlaylistDto> createPlaylist(
            @RequestBody PlaylistDto playlistDto
    ) {
        return ResponseEntity.ok(playlistService.create(playlistDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDto> getPlaylist(
            @PathVariable("id") Long playlistId
    ) {
        return ResponseEntity.ok(playlistService.getById(playlistId));
    }

    @GetMapping
    public ResponseEntity<Page<PlaylistBriefDto>> getPlaylists(
            Pageable pageable
    ) {
        return ResponseEntity.ok(playlistService.getPlaylists(pageable));
    }

    @PutMapping("{id}")
    public ResponseEntity<PlaylistDto> updatePlaylist(
            @PathVariable("id") Long userId,
            @RequestBody PlaylistDto playlistDto
    ) {
        return ResponseEntity.ok(playlistService.save(userId, playlistDto));
    }
    
    @PutMapping(
            value = "/{id}/cover",
            consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<PlaylistDto> setPlaylistCover(
            @PathVariable("id") Long playlistId,
            HttpServletRequest request
    ) throws IOException {
        InputStream imageInputStream = request.getInputStream();
        PlaylistDto playlistDto = playlistCoverService.setImage(playlistId, imageInputStream);
        return ResponseEntity.ok(playlistDto);
    }

    @GetMapping("/{id}/cover")
    public ResponseEntity<StreamingResponseBody> getPlaylistCover(
            @PathVariable("id") Long playlistId
    ) {
        File coverFile = playlistCoverService.getImage(playlistId);
        StreamingResponseBody responseBody = outputStream -> {
            Files.copy(coverFile.toPath(), outputStream);
        };

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=\"%s\"", coverFile.getName())
                ).contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(responseBody);
    }

    @DeleteMapping("/{id}/cover")
    public ResponseEntity<PlaylistDto> deletePlaylistCover(
            @PathVariable("id") Long playlistId
    ) {
        return ResponseEntity.ok(playlistCoverService.deleteImage(playlistId));
    }

    @GetMapping("/{id}/tracks")
    public ResponseEntity<Page<TrackDto>> getTracks(
            @PathVariable("id") Long playlistId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(playlistService.getTracks(playlistId, pageable));
    }

    @PutMapping("/{playlistId}/tracks/{trackId}")
    public ResponseEntity<TrackDto> addTrack(
            @PathVariable("playlistId") Long playlistId,
            @PathVariable("trackId") Long trackId
    ) {
        return ResponseEntity.ok(playlistService.addTrack(playlistId, trackId));
    }

    @DeleteMapping("/{playlistId}/tracks/{trackId}")
    public ResponseEntity<TrackDto> deleteTrack(
            @PathVariable("playlistId") Long playlistId,
            @PathVariable("trackId") Long trackId
    ) {
        return ResponseEntity.ok(playlistService.deleteTrack(playlistId, trackId));
    }

}
