package ru.nsu.ccfit.mvcentertainment.communify.backend.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import ru.nsu.ccfit.mvcentertainment.communify.backend.AppConfig;
import ru.nsu.ccfit.mvcentertainment.communify.backend.TestEntityFactory;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.UserDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.PlaylistBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters.UserInfoDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Playlist;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl.PlaylistBriefMapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl.UserBriefMapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl.UserMapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.PlaylistRepository;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.UserRepository;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl.UserServiceImpl;

import javax.persistence.EntityNotFoundException;
import java.util.*;

public class UserServiceTests {

    @Mock
    UserRepository userRepository;

    @Mock
    PlaylistRepository playlistRepository;

    static UserServiceImpl userService;
    static PlaylistBriefMapper playlistBriefMapper;
    static UserMapper userMapper;

    static User existingUser;
    static UserDto existingUserDto;

    @BeforeAll
    static void setup() {
        ModelMapper modelMapper = new AppConfig().modelMapper();
        UserBriefMapper userBriefMapper = new UserBriefMapper(modelMapper);

        userMapper = new UserMapper(modelMapper);
        playlistBriefMapper = new PlaylistBriefMapper(modelMapper, userBriefMapper);
    }

    @BeforeEach
    void initService() {
        MockitoAnnotations.initMocks(this);

        userService = new UserServiceImpl(
                userRepository,
                playlistRepository,
                userMapper,
                playlistBriefMapper
        );
    }

    void initUserRepository() {
        existingUser = TestEntityFactory.createUser();
        existingUserDto = TestEntityFactory.createUserDto(existingUser);

        Mockito.when(userRepository.findById(
                Mockito.anyLong()
        )).then(invocation -> {
            Long userId = invocation.getArgument(0);
            return Optional.ofNullable(existingUser.getId().equals(userId) ? existingUser : null);
        });

        Mockito.when(userRepository.save(Mockito.any()))
                .then(invocation -> invocation.getArgument(0));
    }


    @Test
    void getUser() {
        initUserRepository();
        Assertions.assertEquals(existingUserDto, userService.getById(existingUser.getId()));

        Long nonExistingUserId = TestEntityFactory.getInvalidId();
        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userService.getById(nonExistingUserId)
        );
    }

    @Test
    void updateUser() {
        initUserRepository();
        UserInfoDto userInfoDto = UserInfoDto.builder()
                .bio("Bio has been changed!")
                .build();

        Long nonExistingUserId = TestEntityFactory.getInvalidId();
        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userService.updateUserInfo(nonExistingUserId, userInfoDto)
        );

        existingUserDto.setBio(userInfoDto.getBio());
        Assertions.assertEquals(
                existingUserDto, userService.updateUserInfo(existingUser.getId(), userInfoDto)
        );
    }

    @Test
    void testPlaylists() {
        final Map<Long, Playlist> existingPlaylistsMap = new HashMap<>();
        final Set<Playlist> userPlaylistSet = new HashSet<>();
        final Set<Playlist> ownedPlaylistSet = new HashSet<>();

        existingUser = TestEntityFactory.createUser();
        existingUserDto = TestEntityFactory.createUserDto(existingUser);

        Mockito.when(userRepository.findById(
                Mockito.anyLong()
        )).then(invocation -> {
            Long userId = invocation.getArgument(0);
            return Optional.ofNullable(existingUser.getId().equals(userId) ? existingUser : null);
        });

        Mockito.when(userRepository.save(
                Mockito.any()
        )).then(invocation -> {
            User user = invocation.getArgument(0);
            userPlaylistSet.addAll(user.getPlaylists());
            userPlaylistSet.retainAll(user.getPlaylists());
            ownedPlaylistSet.addAll(user.getOwnedPlaylists());
            ownedPlaylistSet.retainAll(user.getOwnedPlaylists());
            return user;
        });

        Mockito.when(playlistRepository.findById(
                Mockito.anyLong()
        )).then(invocation -> {
            Long playlistId = invocation.getArgument(0);
            return Optional.ofNullable(existingPlaylistsMap.get(playlistId));
        });

        Mockito.when(playlistRepository.findAllByOwnerId(
                Mockito.anyLong(), Mockito.any()
        )).then(invocation -> new PageImpl<>(new ArrayList<>(ownedPlaylistSet)));

        Mockito.when(playlistRepository.findAllByUserId(
                Mockito.anyLong(), Mockito.any()
        )).then(invocation -> new PageImpl<>(new ArrayList<>(userPlaylistSet)));

        Mockito.when(playlistRepository.save(Mockito.any()))
                .then(invocation -> invocation.getArgument(0));

        Playlist ownedPlaylist = TestEntityFactory.createPlaylist(existingUser);
        PlaylistBriefDto ownedPlaylistBriefDto = TestEntityFactory.createPlaylistBriefDto(ownedPlaylist);
        existingUser.getOwnedPlaylists().add(ownedPlaylist);
        existingUser.getPlaylists().add(ownedPlaylist);
        ownedPlaylistSet.add(ownedPlaylist);
        userPlaylistSet.add(ownedPlaylist);
        existingPlaylistsMap.put(ownedPlaylist.getId(), ownedPlaylist);

        Playlist otherPlaylist = TestEntityFactory.createPlaylist(TestEntityFactory.createUser());
        PlaylistBriefDto otherPlaylistBriefDto = TestEntityFactory.createPlaylistBriefDto(otherPlaylist);
        existingPlaylistsMap.put(otherPlaylist.getId(), otherPlaylist);

        var userPlaylists = userService.getUserPlaylists(existingUser.getId(), null).getContent();
        Assertions.assertTrue(userPlaylists.contains(ownedPlaylistBriefDto));
        Assertions.assertEquals(1, userPlaylists.size());

        userService.addUserPlaylist(existingUser.getId(), otherPlaylist.getId());
        userPlaylists = userService.getUserPlaylists(existingUser.getId(), null).getContent();
        Assertions.assertTrue(userPlaylists.contains(otherPlaylistBriefDto));
        Assertions.assertEquals(2, userPlaylists.size());

        userService.deleteUserPlaylist(existingUser.getId(), ownedPlaylist.getId());
        userPlaylists = userService.getUserPlaylists(existingUser.getId(), null).getContent();
        Assertions.assertFalse(userPlaylists.contains(ownedPlaylistBriefDto));
        Assertions.assertEquals(1, userPlaylists.size());

        var ownedPlaylists = userService.getOwnedPlaylists(existingUser.getId(), null).getContent();
        Assertions.assertTrue(ownedPlaylists.contains(ownedPlaylistBriefDto));
        Assertions.assertEquals(1, ownedPlaylists.size());
    }
}
