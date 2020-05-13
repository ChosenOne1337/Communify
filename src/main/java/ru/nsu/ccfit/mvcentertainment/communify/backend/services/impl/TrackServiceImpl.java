package ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl;

import com.mpatric.mp3agic.Mp3File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.TrackDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters.TrackInfoDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Track;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.Mapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.TrackRepository;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.PlaylistService;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.TrackService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Service
public class TrackServiceImpl
        extends AbstractService<Track, TrackDto, Long>
        implements TrackService {

    private final File trackDirectoryPath;
    private final TrackRepository repository;
    private final Mapper<Track, TrackDto, Long> mapper;

    private final PlaylistService playlistService;

    private static final String TEMP_FILE_PREFIX = "track";
    private static final String TEMP_FILE_SUFFIX = null;

    @Autowired
    public TrackServiceImpl(
            @Value("${custom.tracks.dirpath}") String trackDirectoryPath,
            TrackRepository repository,
            Mapper<Track, TrackDto, Long> mapper,
            PlaylistService playlistService
    ) {
        this.trackDirectoryPath = new File(trackDirectoryPath);
        this.repository = repository;
        this.mapper = mapper;
        this.playlistService = playlistService;

        init();
    }

    @Override
    public File getTrackFile(Long trackId) {
        Track track = getEntityByIdOrThrow(trackId);
        TrackDto trackDto = mapper.toDto(track);
        String trackFileName = getTrackFileNameFromDto(trackDto);
        return new File(trackDirectoryPath, trackFileName);
    }

    @Override
    @Transactional
    public TrackDto uploadTrack(
            Long playlistId,
            TrackInfoDto trackInfoDto,
            InputStream audioFileStream
    ) {

        File trackFile = null;
        try {
            trackFile = createTrackFile(audioFileStream);
            Mp3File mp3trackMetadata = new Mp3File(trackFile);
            TrackDto trackDto = new TrackDto(
                    trackInfoDto.getName(),
                    trackInfoDto.getAuthor(),
                    trackInfoDto.getDescription(),
                    mp3trackMetadata.getLengthInMilliseconds()
            );

            trackDto = createTrack(trackDto, trackFile);
            playlistService.addTrackToPlaylist(playlistId, trackDto.getId());

            return trackDto;
        } catch (Exception e) {
            if (trackFile != null) {
                trackFile.delete();
            }

            throw new RuntimeException(e);
        }
    }

    private File createTrackFile(InputStream audioFileStream) throws IOException {
        File trackFile = File.createTempFile(
                TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX, trackDirectoryPath
        );

        Files.copy(audioFileStream, trackFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        audioFileStream.close();

        return trackFile;
    }

    private TrackDto createTrack(TrackDto trackDto, File trackFile) throws IOException {
        TrackDto createdTrackDto = create(trackDto);
        String trackFileName = getTrackFileNameFromDto(createdTrackDto);
        File renamedTrackFile = new File(trackDirectoryPath, trackFileName);
        Files.move(trackFile.toPath(), renamedTrackFile.toPath());
        return createdTrackDto;
    }

    private String getTrackFileNameFromDto(TrackDto trackDto) {
        return String.format("%d.mp3", trackDto.getId());
    }

    private void init() {
        if (trackDirectoryPath.exists()) {
            return;
        }

        boolean isCreated = trackDirectoryPath.mkdirs();
        if (!isCreated) {
            throw new RuntimeException(
                    String.format("Failed to create %s", trackDirectoryPath.getAbsolutePath())
            );
        }
    }

    @Override
    protected JpaRepository<Track, Long> getRepository() {
        return repository;
    }

    @Override
    protected Mapper<Track, TrackDto, Long> getMapper() {
        return mapper;
    }

}
