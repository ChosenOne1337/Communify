package ru.nsu.ccfit.mvcentertainment.communify.backend.services;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.AppConfig;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.TrackDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters.TrackInfoDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Track;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl.TrackMapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.TrackRepository;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.exceptions.ResourceException;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl.TrackServiceImpl;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class TrackServiceTests {

    static Path tempDirectory;

    @Mock
    TrackRepository trackRepository;

    @Mock
    PlaylistService playlistService;

    static Set<Track> trackSet = new HashSet<>();

    static TrackService trackService;

    @BeforeAll
    @SneakyThrows
    static void setup() {
        tempDirectory = Files.createTempDirectory("temp");
        tempDirectory.toFile().deleteOnExit();
    }

    @BeforeEach
    void initService() {
        MockitoAnnotations.initMocks(this);

        trackSet.clear();

        ModelMapper modelMapper = new AppConfig().modelMapper();
        TrackMapper trackMapper = new TrackMapper(modelMapper);

        trackService = new TrackServiceImpl(
                tempDirectory.toFile().getAbsolutePath(),
                trackRepository,
                trackMapper,
                playlistService
        );

        Mockito.when(trackRepository.findById(
                Mockito.any()
        )).then(invocation -> {
            Long trackId = invocation.getArgument(0);
            return trackSet.stream()
                    .filter(t -> t.getId().equals(trackId))
                    .findAny();
        });

        Mockito.when(trackRepository.save(
                Mockito.any()
        )).then(invocation -> {
            Track track = invocation.getArgument(0);
            track.setId((long) trackSet.size());
            trackSet.add(track);
            return track;
        });
    }

    @Test
    @SneakyThrows
    public void test() {
        // get file for nonexistent track
        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> trackService.getTrackFile(-1L)
        );

        Long playlistId = 42L;
        TrackInfoDto trackInfoDto = TrackInfoDto.builder()
                .name("Track name")
                .author("Track author")
                .description("Track description")
                .build();

        // upload invalid mp3 file
        byte[] trackBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(trackBytes);
        Assertions.assertThrows(
                ResourceException.class,
                () -> trackService.uploadTrack(playlistId, trackInfoDto, byteArrayInputStream)
        );

        // upload valid mp3 file
        Path testIconPath = Paths.get("src", "test", "resources", "test_track.mp3");
        TrackDto trackDto = null;
        try (var trackInputStream = Files.newInputStream(testIconPath)) {
            trackDto = trackService.uploadTrack(playlistId, trackInfoDto, trackInputStream);
            Assertions.assertEquals(trackDto.getName(), trackInfoDto.getName());
            Assertions.assertEquals(trackDto.getAuthor(), trackInfoDto.getAuthor());
            Assertions.assertEquals(trackDto.getDescription(), trackInfoDto.getDescription());
        }

        // get track file
        File trackFile = trackService.getTrackFile(trackDto.getId());
        Assertions.assertTrue(trackFile.exists());

    }

    @AfterAll
    @SneakyThrows
    static void cleanup() {
        for (Path path : Files.newDirectoryStream(tempDirectory)) {
            Files.delete(path);
        }
        Files.delete(tempDirectory);
    }

}
