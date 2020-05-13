package ru.nsu.ccfit.mvcentertainment.communify.backend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.PlaylistDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.TrackDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.PlaylistBriefDto;

public interface PlaylistService extends EntityService<PlaylistDto, Long> {
    Page<PlaylistBriefDto> getPlaylists(Pageable pageable);

    Page<TrackDto> getTracks(Long playlistId, Pageable pageable);

    TrackDto addTrack(Long playlistId, Long trackId);

    TrackDto deleteTrack(Long playlistId, Long trackId);
}
