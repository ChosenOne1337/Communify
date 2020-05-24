package ru.nsu.ccfit.mvcentertainment.communify.backend.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.AppConfig;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.PlaylistDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.PlaylistBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.UserBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Playlist;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.types.Genre;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl.PlaylistBriefMapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl.PlaylistMapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl.UserBriefMapper;

import java.sql.Date;

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
        playlistMapper = new PlaylistMapper(modelMapper, userBriefMapper);
        playlistBriefMapper = new PlaylistBriefMapper(modelMapper, userBriefMapper);

        User user = User.builder()
                .name("User's name")
                .bio("User's bio")
                .password("123456")
                .build();
        user.setId(42L);

        UserBriefDto userBriefDto = userBriefMapper.toDto(user);

        playlist = Playlist.builder()
                .name("Playlist's name")
                .description("Playlist's description")
                .owner(user)
                .genre(Genre.JAZZ)
                .creationDate(new Date(123456L))
                .build();
        playlist.setId(42L);

        playlistDto = PlaylistDto.builder()
                .name(playlist.getName())
                .owner(userBriefDto)
                .description(playlist.getDescription())
                .genre(playlist.getGenre())
                .creationDate(playlist.getCreationDate())
                .build();
        playlistDto.setId(playlist.getId());

        playlistBriefDto = PlaylistBriefDto.builder()
                .name(playlist.getName())
                .owner(userBriefDto)
                .genre(playlist.getGenre())
                .creationDate(playlist.getCreationDate())
                .build();
        playlistBriefDto.setId(playlist.getId());
    }

    @Test
    void toDto() {
        Assertions.assertEquals(playlistDto, playlistMapper.toDto(playlist));
        Assertions.assertEquals(playlistBriefDto, playlistBriefMapper.toDto(playlist));
    }

    @Test
    void toEntity() {
        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> playlistMapper.toEntity(playlistDto)
        );
        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> playlistBriefMapper.toEntity(playlistBriefDto)
        );
    }
}
