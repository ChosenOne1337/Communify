package ru.nsu.ccfit.mvcentertainment.communify.backend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.PlaylistDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.TrackDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.PlaylistBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.filters.PlaylistFilter;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters.PlaylistInfoDto;

public interface PlaylistService extends EntityService<PlaylistDto, Long> {

    Long getPlaylistOwnerId(Long playlistId);

    PlaylistDto createPlaylist(Long ownerId, PlaylistInfoDto playlistInfo);

    PlaylistDto updatePlaylistInfo(Long playlistId, PlaylistInfoDto playlistInfoDto);

    Page<PlaylistBriefDto> getAllPlaylists(Pageable pageable);

    Page<PlaylistBriefDto> search(PlaylistFilter filter, Pageable pageable);

    Page<TrackDto> getPlaylistTracks(Long playlistId, Pageable pageable);

    TrackDto addTrackToPlaylist(Long playlistId, Long trackId);

    TrackDto deleteTrackFromPlaylist(Long playlistId, Long trackId);

}
