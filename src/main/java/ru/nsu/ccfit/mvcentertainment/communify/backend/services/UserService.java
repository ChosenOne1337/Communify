package ru.nsu.ccfit.mvcentertainment.communify.backend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.UserDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.PlaylistBriefDto;

public interface UserService extends EntityService<UserDto, Long> {

    UserDto addPlaylist(Long userId, Long playlistId);

    UserDto deletePlaylist(Long userId, Long playlistId);

    Page<PlaylistBriefDto> getPlaylists(Long userId, Pageable pageable);

    Page<PlaylistBriefDto> getOwnedPlaylists(Long userId, Pageable pageable);

}
