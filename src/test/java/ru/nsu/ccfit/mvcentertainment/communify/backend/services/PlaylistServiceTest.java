package ru.nsu.ccfit.mvcentertainment.communify.backend.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.nsu.ccfit.mvcentertainment.communify.backend.AppConfig;
import ru.nsu.ccfit.mvcentertainment.communify.backend.TestEntityFactory;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.PlaylistDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.TrackDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.PlaylistBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters.PlaylistInfoDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Playlist;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Track;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.types.Genre;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl.PlaylistBriefMapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl.PlaylistMapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl.TrackMapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl.UserBriefMapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.PlaylistRepository;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.TrackRepository;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.UserRepository;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl.PlaylistServiceImpl;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PlaylistServiceTest {

    @Mock
    PlaylistRepository playlistRepository;

    @Mock
    TrackRepository trackRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UserService userService;

    static PlaylistService playlistService;

    Set<Track> trackSet;
    Set<User> userSet;
    Set<Playlist> playlistSet;

    @BeforeEach
    void initService() {
        MockitoAnnotations.initMocks(this);

        trackSet = new HashSet<>();
        userSet = new HashSet<>();
        playlistSet = new HashSet<>();

        ModelMapper modelMapper = new AppConfig().modelMapper();
        UserBriefMapper userBriefMapper = new UserBriefMapper(modelMapper);
        PlaylistMapper playlistMapper = new PlaylistMapper(
                modelMapper,
                userBriefMapper,
                userRepository
        );
        PlaylistBriefMapper playlistBriefMapper = new PlaylistBriefMapper(
                modelMapper,
                userBriefMapper
        );
        TrackMapper trackMapper = new TrackMapper(modelMapper);

        playlistService = new PlaylistServiceImpl(
                userService,
                playlistRepository,
                trackRepository,
                playlistMapper,
                playlistBriefMapper,
                trackMapper
        );

        Mockito.when(playlistRepository.getPlaylistOwnerId(
                Mockito.any()
        )).then(invocation -> {
            Long playlistId = invocation.getArgument(0);
            return playlistSet.stream()
                    .filter(p -> p.getId().equals(playlistId))
                    .findAny()
                    .map(Playlist::getOwner)
                    .map(User::getId)
                    .orElse(null);
        });

        Mockito.when(playlistRepository.save(
                Mockito.any()
        )).then(invocation -> {
            Playlist playlist = invocation.getArgument(0);
            if (playlist.getId() == null) {
                playlist.setId((long) playlistSet.size());
            } else {
                playlistSet.removeIf(p -> p.getId().equals(playlist.getId()));
            }

            playlistSet.add(playlist);
            return playlist;
        });

        Mockito.when(playlistRepository.findById(
                Mockito.any()
        )).then(invocation -> {
            Long playlistId = invocation.getArgument(0);
            return playlistSet.stream()
                    .filter(p -> p.getId().equals(playlistId))
                    .findAny();
        });

        Mockito.when(playlistRepository.findAll(
                (Pageable) null
        )).then(invocation -> new PageImpl<>(new ArrayList<>(playlistSet)));

        Mockito.when(trackRepository.findAllPlaylistTracks(
                Mockito.any(), Mockito.any()
        )).then(invocation -> {
            Long playlistId = invocation.getArgument(0);
            List<Track> tracks = playlistSet.stream()
                    .filter(p -> p.getId().equals(playlistId))
                    .flatMap(p -> p.getTracks().stream())
                    .collect(Collectors.toList());

            return new PageImpl<>(tracks);
        });

        Mockito.when(trackRepository.findById(
                Mockito.any()
        )).then(invocation -> {
            Long trackId = invocation.getArgument(0);
            return trackSet.stream()
                    .filter(t -> t.getId().equals(trackId))
                    .findAny();
        });

        Mockito.when(userRepository.findById(
                Mockito.any()
        )).then(invocation -> {
            Long userId = invocation.getArgument(0);
            return userSet.stream()
                    .filter(u -> u.getId().equals(userId))
                    .findAny();
        });

        Mockito.when(userService.addUserPlaylist(
                Mockito.any(), Mockito.any()
        )).then(invocation -> {
            Long userId = invocation.getArgument(0);
            Long playlistId = invocation.getArgument(1);

            User user = userSet.stream()
                    .filter(u -> u.getId().equals(userId))
                    .findAny()
                    .orElse(null);

            if (user == null) {
                throw new EntityNotFoundException();
            }

            Playlist playlist = playlistSet.stream()
                    .filter(p -> p.getId().equals(playlistId))
                    .findAny()
                    .orElse(null);

            if (playlist == null) {
                throw new EntityNotFoundException();
            }

            userSet.remove(user);
            playlistSet.remove(playlist);

            playlist.setOwner(user);
            user.getPlaylists().add(playlist);

            userSet.add(user);
            playlistSet.add(playlist);

            return null;
        });
    }

    @Test
    void test() {
        User user = TestEntityFactory.createUser();
        userSet.add(user);

        PlaylistInfoDto playlistInfoDto = PlaylistInfoDto.builder()
                .name("playlist")
                .description("description")
                .genre(Genre.JAZZ)
                .build();

        // check creation
        PlaylistDto playlistDto = playlistService.createPlaylist(user.getId(), playlistInfoDto);
        Assertions.assertEquals(playlistDto.getOwner().getId(), user.getId());
        Assertions.assertEquals(playlistDto.getName(), playlistInfoDto.getName());
        Assertions.assertEquals(playlistDto.getDescription(), playlistInfoDto.getDescription());
        Assertions.assertEquals(playlistDto.getGenre(), playlistInfoDto.getGenre());

        // check list of all playlists
        Set<Long> allPlaylistIdSet = playlistService
                .getAllPlaylists(null)
                .stream()
                .map(PlaylistBriefDto::getId)
                .collect(Collectors.toSet());

        Set<Long> playlistIdSet = playlistSet
                .stream()
                .map(Playlist::getId)
                .collect(Collectors.toSet());

        Assertions.assertTrue(
                allPlaylistIdSet.containsAll(playlistIdSet) &&
                playlistIdSet.containsAll(allPlaylistIdSet)
        );

        // check playlist's owner
        Long ownerId = playlistService.getPlaylistOwnerId(playlistDto.getId());
        Assertions.assertEquals(user.getId(), ownerId);

        // update playlist
        PlaylistInfoDto newPlaylistInfo = PlaylistInfoDto.builder()
                .name("updated name")
                .description("updated description")
                .genre(Genre.METAL)
                .build();
        PlaylistDto newPlaylistDto = playlistService.updatePlaylistInfo(
                playlistDto.getId(), newPlaylistInfo
        );
        Assertions.assertEquals(newPlaylistInfo.getName(), newPlaylistDto.getName());
        Assertions.assertEquals(newPlaylistInfo.getDescription(), newPlaylistDto.getDescription());
        Assertions.assertEquals(newPlaylistInfo.getGenre(), newPlaylistDto.getGenre());

        // get tracks
        Track track = TestEntityFactory.createTrack();
        trackSet.add(track);

        List<TrackDto> playlistTracks = playlistService
                .getPlaylistTracks(playlistDto.getId(), null)
                .getContent();
        Assertions.assertTrue(playlistTracks.isEmpty());

        // add track
        playlistService.addTrackToPlaylist(playlistDto.getId(), track.getId());
        playlistTracks = playlistService
                .getPlaylistTracks(playlistDto.getId(), null)
                .getContent();

        Assertions.assertEquals(playlistTracks.size(), trackSet.size());
        Assertions.assertTrue(playlistTracks.contains(TestEntityFactory.createTrackDto(track)));

        // delete track
        playlistService.deleteTrackFromPlaylist(playlistDto.getId(), track.getId());
        playlistTracks = playlistService
                .getPlaylistTracks(playlistDto.getId(), null)
                .getContent();
        Assertions.assertTrue(playlistTracks.isEmpty());
    }

}
