package ru.nsu.ccfit.mvcentertainment.communify.backend;

import lombok.experimental.UtilityClass;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.PlaylistDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.TrackDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.UserDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.PlaylistBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.UserBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Playlist;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Track;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.types.Genre;

import java.sql.Date;
import java.util.concurrent.atomic.AtomicLong;

@UtilityClass
public class TestEntityFactory {

    private final AtomicLong userIdGenerator = new AtomicLong(1L);
    private final AtomicLong playlistIdGenerator = new AtomicLong(1L);
    private final AtomicLong trackIdGenerator = new AtomicLong(1L);

    public Long getInvalidId() {
        return -1L;
    }

    public User createUser() {
        return createUser(userIdGenerator.getAndIncrement());
    }

    public User createUser(Long userId) {
        User user = User.builder()
                .name(String.format("User#%d name", userId))
                .bio(String.format("User#%d bio", userId))
                .password(String.format("User#%d password", userId))
                .build();
        user.setId(userId);
        return user;
    }

    public UserDto createUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .name(user.getName())
                .bio(user.getBio())
                .build();
        userDto.setId(user.getId());
        return userDto;
    }

    public UserBriefDto createUserBriefDto(User user) {
        UserBriefDto userBriefDto = UserBriefDto.builder()
                .name(user.getName())
                .build();
        userBriefDto.setId(user.getId());
        return userBriefDto;
    }

    public Playlist createPlaylist(User owner) {
        return createPlaylist(playlistIdGenerator.getAndIncrement(), owner);
    }

    public Playlist createPlaylist(Long playlistId, User owner) {
        Playlist playlist = Playlist.builder()
                .name(String.format("Playlist#%d name", playlistId))
                .description(String.format("Playlist#%d description", playlistId))
                .owner(owner)
                .genre(Genre.JAZZ)
                .creationDate(new Date(123456L))
                .build();
        playlist.setId(playlistId);
        return playlist;
    }

    public PlaylistDto createPlaylistDto(Playlist playlist) {
        PlaylistDto playlistDto = PlaylistDto.builder()
                .name(playlist.getName())
                .owner(createUserBriefDto(playlist.getOwner()))
                .description(playlist.getDescription())
                .genre(playlist.getGenre())
                .creationDate(playlist.getCreationDate())
                .build();
        playlistDto.setId(playlist.getId());
        return playlistDto;
    }

    public PlaylistBriefDto createPlaylistBriefDto(Playlist playlist) {
        PlaylistBriefDto playlistBriefDto = PlaylistBriefDto.builder()
                .name(playlist.getName())
                .owner(createUserBriefDto(playlist.getOwner()))
                .genre(playlist.getGenre())
                .creationDate(playlist.getCreationDate())
                .build();
        playlistBriefDto.setId(playlist.getId());
        return playlistBriefDto;
    }

    public Track createTrack() {
        return createTrack(trackIdGenerator.getAndIncrement());
    }

    public Track createTrack(Long trackId) {
        Track track = Track.builder()
                .name(String.format("Track#%d name", trackId))
                .author(String.format("Track#%d author", trackId))
                .description(String.format("Track#%d description", trackId))
                .duration(123456L)
                .build();
        track.setId(trackId);
        return track;
    }

    public TrackDto createTrackDto(Track track) {
        TrackDto trackDto = TrackDto.builder()
                .name(track.getName())
                .author(track.getAuthor())
                .description(track.getDescription())
                .duration(track.getDuration())
                .build();
        trackDto.setId(track.getId());
        return trackDto;
    }

}
