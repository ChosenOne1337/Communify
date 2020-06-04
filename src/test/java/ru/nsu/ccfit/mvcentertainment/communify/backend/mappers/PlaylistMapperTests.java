package ru.nsu.ccfit.mvcentertainment.communify.backend.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.AppConfig;
import ru.nsu.ccfit.mvcentertainment.communify.backend.TestEntityFactory;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.PlaylistDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.PlaylistBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Playlist;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl.PlaylistBriefMapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl.PlaylistMapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl.UserBriefMapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.UserRepository;

import java.util.Optional;

public class PlaylistMapperTests {

    private static PlaylistBriefMapper playlistBriefMapper;
    private static PlaylistMapper playlistMapper;

    private static Playlist playlist;
    private static PlaylistDto playlistDto;
    private static PlaylistBriefDto playlistBriefDto;

    @BeforeAll
    static void setup() {
        ModelMapper modelMapper = new AppConfig().modelMapper();
        UserBriefMapper userBriefMapper = new UserBriefMapper(modelMapper);

        UserRepository userRepository = Mockito.mock(UserRepository.class);
        Mockito.when(userRepository.findById(
                Mockito.any()
        )).then(invocation -> {
            Long userId = invocation.getArgument(0);
            return userId == null ?
                    Optional.empty() : Optional.of(TestEntityFactory.createUser(userId));
        });

        playlistMapper = new PlaylistMapper(modelMapper, userBriefMapper, userRepository);
        playlistMapper.setupMapper();

        playlistBriefMapper = new PlaylistBriefMapper(modelMapper, userBriefMapper);
        playlistBriefMapper.setupMapper();

        User user = TestEntityFactory.createUser();
        playlist = TestEntityFactory.createPlaylist(user);
        playlistDto = TestEntityFactory.createPlaylistDto(playlist);
        playlistBriefDto = TestEntityFactory.createPlaylistBriefDto(playlist);
    }

    @Test
    void toDto() {
        Assertions.assertEquals(playlistDto, playlistMapper.toDto(playlist));
        Assertions.assertEquals(playlistBriefDto, playlistBriefMapper.toDto(playlist));
    }

    @Test
    void toEntity() {
        Playlist playlist = playlistMapper.toEntity(playlistDto);
        Assertions.assertEquals(playlistDto, playlistMapper.toDto(playlist));

        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> playlistBriefMapper.toEntity(playlistBriefDto)
        );
    }
}
