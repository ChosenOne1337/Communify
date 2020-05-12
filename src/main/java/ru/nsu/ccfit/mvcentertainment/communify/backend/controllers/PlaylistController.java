package ru.nsu.ccfit.mvcentertainment.communify.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.PlaylistCoverService;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.PlaylistService;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final PlaylistCoverService playlistCoverService;

    @Autowired
    public PlaylistController(
            PlaylistService playlistService,
            PlaylistCoverService playlistCoverService
    ) {
        this.playlistService = playlistService;
        this.playlistCoverService = playlistCoverService;
    }
}
