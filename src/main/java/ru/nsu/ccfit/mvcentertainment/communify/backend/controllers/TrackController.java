package ru.nsu.ccfit.mvcentertainment.communify.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.TrackService;

import java.io.File;

@RestController
@RequestMapping("/tracks")
public class TrackController {

    private final TrackService trackService;

    @Autowired
    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping(value = "/{trackId}")
    public ResponseEntity<StreamingResponseBody> playTrack(
            @PathVariable Long trackId
    ) {
        File trackFile = trackService.getTrackFile(trackId);
        return StreamingResponseFactory.getStreamingResponse(trackFile);
    }

}
