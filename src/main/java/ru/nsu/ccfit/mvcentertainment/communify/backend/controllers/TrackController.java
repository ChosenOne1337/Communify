package ru.nsu.ccfit.mvcentertainment.communify.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.TrackDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.TrackService;

@RestController
public class TrackController {

    private final TrackService trackService;

    @Autowired
    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping(
            value = "/{id}/play",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public @ResponseBody
    ResponseEntity<byte[]> playTrack(
            @PathVariable("id") Long trackId
    ) {
        byte[] audioFileBytes = trackService.getAudioFileBytes(trackId);
        return ResponseEntity.ok(audioFileBytes);
    }

    @PostMapping(
            value = "/upload",
            consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<TrackDto> uploadTrack(
            @RequestParam String name,
            @RequestParam String author,
            @RequestParam(required = false) String description,
            @RequestBody byte[] audioFileBytes
    ) {
        TrackDto trackDto = trackService.uploadTrack(name, author, description, audioFileBytes);
        return ResponseEntity.ok(trackDto);
    }
}
