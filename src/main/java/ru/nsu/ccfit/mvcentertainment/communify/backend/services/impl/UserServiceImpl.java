package ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.UserDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.PlaylistBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters.UserInfoDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Playlist;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.Mapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.PlaylistRepository;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.UserRepository;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.UserService;

@Service
public class UserServiceImpl
    extends AbstractService<User, UserDto, Long>
    implements UserService {

    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final Mapper<User, UserDto, Long> mapper;
    private final Mapper<Playlist, PlaylistBriefDto, Long> playlistBriefMapper;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            PlaylistRepository playlistRepository,
            Mapper<User, UserDto, Long> mapper,
            Mapper<Playlist, PlaylistBriefDto, Long> playlistBriefMapper
    ) {
        this.userRepository = userRepository;
        this.playlistRepository = playlistRepository;
        this.mapper = mapper;
        this.playlistBriefMapper = playlistBriefMapper;
    }

    @Override
    @Transactional
    public UserDto updateUserInfo(Long userId, UserInfoDto userInfoDto) {
        User user = getEntityByIdOrThrow(userId);
        user.setBio(userInfoDto.getBio());
        user = userRepository.save(user);
        return mapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto addUserPlaylist(Long userId, Long playlistId) {
        User user = getEntityByIdOrThrow(userId);
        Playlist playlist = getEntityByIdOrThrow(playlistRepository, playlistId);
        user.getPlaylists().add(playlist);
        user = userRepository.save(user);
        return mapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto deleteUserPlaylist(Long userId, Long playlistId) {
        User user = getEntityByIdOrThrow(userId);
        Playlist playlist = getEntityByIdOrThrow(playlistRepository, playlistId);
        user.getPlaylists().remove(playlist);
        user = userRepository.save(user);
        return mapper.toDto(user);
    }

    @Override
    public Page<PlaylistBriefDto> getUserPlaylists(Long userId, Pageable pageable) {
        return playlistRepository
                .findAllByUserId(userId, pageable)
                .map(playlistBriefMapper::toDto);
    }

    @Override
    public Page<PlaylistBriefDto> getOwnedPlaylists(Long userId, Pageable pageable) {
        return playlistRepository
                .findAllByOwnerId(userId, pageable)
                .map(playlistBriefMapper::toDto);
    }

    @Override
    protected JpaRepository<User, Long> getUserRepository() {
        return userRepository;
    }

    @Override
    protected Mapper<User, UserDto, Long> getMapper() {
        return mapper;
    }
}
