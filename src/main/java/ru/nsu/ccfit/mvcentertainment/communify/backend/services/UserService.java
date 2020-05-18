package ru.nsu.ccfit.mvcentertainment.communify.backend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.UserDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.PlaylistBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters.UserInfoDto;

public interface UserService extends EntityService<UserDto, Long> {

    UserDto updateUserInfo(Long userId, UserInfoDto userInfoDto);

    UserDto addUserPlaylist(Long userId, Long playlistId);

    UserDto deleteUserPlaylist(Long userId, Long playlistId);

    Page<PlaylistBriefDto> getUserPlaylists(Long userId, Pageable pageable);

    Page<PlaylistBriefDto> getOwnedPlaylists(Long userId, Pageable pageable);
}
