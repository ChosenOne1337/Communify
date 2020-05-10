package ru.nsu.ccfit.mvcentertainment.communify.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.TrackDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.TrackService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@RestController
@RequestMapping("/tracks")
public class TrackController {

    private final TrackService trackService;

    @Autowired
    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping(value = "/{id}/play")
    public ResponseEntity<StreamingResponseBody> playTrack(
            @PathVariable("id") Long trackId
    ) {
        File trackFile = trackService.getTrackFile(trackId);
        StreamingResponseBody responseBody = outputStream -> {
            Files.copy(trackFile.toPath(), outputStream);
        };

        return ResponseEntity.ok()
                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    String.format("attachment; filename=\"%s\"", trackFile.getName())
                ).contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(responseBody);
    }

    @PostMapping(
            value = "/upload",
            consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<TrackDto> uploadTrack(
            @RequestParam String name,
            @RequestParam String author,
            @RequestParam(required = false) String description,
            HttpServletRequest request
    ) throws IOException {
        InputStream audioFileStream = request.getInputStream();
        TrackDto trackDto = trackService.uploadTrack(
                name,
                author,
                description,
                audioFileStream);
        return ResponseEntity.ok(trackDto);
    }

}
