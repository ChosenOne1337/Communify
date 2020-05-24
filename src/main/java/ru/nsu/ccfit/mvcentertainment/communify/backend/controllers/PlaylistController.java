package ru.nsu.ccfit.mvcentertainment.communify.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.PlaylistDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.TrackDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.PlaylistBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters.PlaylistInfoDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters.TrackInfoDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.security.UserIdentityValidator;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.PlaylistCoverService;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.PlaylistService;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.TrackService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    private final TrackService trackService;
    private final PlaylistService playlistService;
    private final PlaylistCoverService playlistCoverService;

    @Autowired
    public PlaylistController(
            TrackService trackService,
            PlaylistService playlistService,
            PlaylistCoverService playlistCoverService
    ) {
        this.trackService = trackService;
        this.playlistService = playlistService;
        this.playlistCoverService = playlistCoverService;
    }

    @PostMapping
    public ResponseEntity<PlaylistDto> createPlaylist(
            @RequestBody @Valid PlaylistInfoDto playlistInfo
    ) {
        Long ownerId = UserIdentityValidator.getActualUserId();
        return ResponseEntity.ok(playlistService.createPlaylist(ownerId, playlistInfo));
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<PlaylistDto> getPlaylist(
            @PathVariable Long playlistId
    ) {
        return ResponseEntity.ok(playlistService.getById(playlistId));
    }

    @GetMapping
    public ResponseEntity<Page<PlaylistBriefDto>> getAllPlaylists(
            Pageable pageable
    ) {
        return ResponseEntity.ok(playlistService.getAllPlaylists(pageable));
    }

    @PutMapping("{playlistId}")
    public ResponseEntity<PlaylistDto> updatePlaylistInfo(
            @PathVariable Long playlistId,
            @RequestBody @Valid PlaylistInfoDto playlistInfoDto
    ) {
        validatePlaylistOwner(playlistId);
        return ResponseEntity.ok(playlistService.updatePlaylistInfo(playlistId, playlistInfoDto));
    }

    @PutMapping(
            value = "/{playlistId}/cover",
            consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<PlaylistDto> setPlaylistCover(
            @PathVariable Long playlistId,
            HttpServletRequest request
    ) throws IOException {
        validatePlaylistOwner(playlistId);
        InputStream imageInputStream = request.getInputStream();
        PlaylistDto playlistDto = playlistCoverService.setImage(playlistId, imageInputStream);
        return ResponseEntity.ok(playlistDto);
    }

    @GetMapping("/{playlistId}/cover")
    public ResponseEntity<StreamingResponseBody> getPlaylistCover(
            @PathVariable Long playlistId
    ) {
        File coverFile = playlistCoverService.getImage(playlistId);
        return StreamingResponseFactory.getStreamingResponse(coverFile);
    }

    @DeleteMapping("/{playlistId}/cover")
    public ResponseEntity<PlaylistDto> deletePlaylistCover(
            @PathVariable Long playlistId
    ) {
        validatePlaylistOwner(playlistId);
        return ResponseEntity.ok(playlistCoverService.deleteImage(playlistId));
    }

    @GetMapping("/{playlistId}/tracks")
    public ResponseEntity<Page<TrackDto>> getPlaylistTracks(
            @PathVariable Long playlistId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(playlistService.getPlaylistTracks(playlistId, pageable));
    }

    @PutMapping("/{playlistId}/tracks/{trackId}")
    public ResponseEntity<TrackDto> addTrackToPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long trackId
    ) {
        validatePlaylistOwner(playlistId);
        return ResponseEntity.ok(playlistService.addTrackToPlaylist(playlistId, trackId));
    }

    @PostMapping(
            value = "/{playlistId}/tracks",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<TrackDto> uploadTrackToPlaylist(
            @PathVariable Long playlistId,
            @RequestPart("trackInfo") @Valid TrackInfoDto trackInfoDto,
            @RequestPart("audioFile") MultipartFile audioFile
    ) throws IOException {
        validatePlaylistOwner(playlistId);
        try (InputStream audioFileStream = audioFile.getInputStream()) {
            return ResponseEntity.ok(trackService.uploadTrack(
                    playlistId, trackInfoDto, audioFileStream
            ));
        }
    }

    @DeleteMapping("/{playlistId}/tracks/{trackId}")
    public ResponseEntity<TrackDto> deleteTrackFromPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long trackId
    ) {
        validatePlaylistOwner(playlistId);
        return ResponseEntity.ok(playlistService.deleteTrackFromPlaylist(playlistId, trackId));
    }

    private void validatePlaylistOwner(Long playlistId) {
        Long ownerId = playlistService.getPlaylistOwnerId(playlistId);
        UserIdentityValidator.validateUserId(ownerId);
    }

}
