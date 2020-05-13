package ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.PlaylistDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.TrackDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.PlaylistBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.UserBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters.PlaylistInfoDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Playlist;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Track;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.Mapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.PlaylistRepository;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.TrackRepository;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.PlaylistService;

import java.util.Calendar;
import java.util.Date;

@Service
public class PlaylistServiceImpl
        extends AbstractService<Playlist, PlaylistDto, Long>
        implements PlaylistService {

    private final PlaylistRepository repository;
    private final TrackRepository trackRepository;
    private final Mapper<Playlist, PlaylistDto, Long> mapper;
    private final Mapper<Playlist, PlaylistBriefDto, Long> briefMapper;
    private final Mapper<Track, TrackDto, Long> trackMapper;

    public PlaylistServiceImpl(
            PlaylistRepository repository,
            TrackRepository trackRepository,
            Mapper<Playlist, PlaylistDto, Long> mapper,
            Mapper<Playlist, PlaylistBriefDto, Long> briefMapper,
            Mapper<Track, TrackDto, Long> trackMapper
    ) {
        this.repository = repository;
        this.trackRepository = trackRepository;
        this.mapper = mapper;
        this.briefMapper = briefMapper;
        this.trackMapper = trackMapper;
    }

    @Override
    public PlaylistDto createPlaylist(PlaylistInfoDto playlistInfo) {
        Date creationDate = Calendar.getInstance().getTime();

        UserBriefDto userBriefDto = new UserBriefDto();
        userBriefDto.setId(playlistInfo.getOwnerId());

        PlaylistDto playlistDto = new PlaylistDto(
                playlistInfo.getName(),
                playlistInfo.getDescription(),
                creationDate,
                userBriefDto,
                playlistInfo.getGenre()
        );

        return create(playlistDto);
    }

    @Override
    @Transactional
    public PlaylistDto updatePlaylistInfo(
            Long playlistId,
            PlaylistInfoDto playlistInfoDto
    ) {
        PlaylistDto playlistDto = getById(playlistId);
        playlistDto.setName(playlistInfoDto.getName());
        playlistDto.setDescription(playlistInfoDto.getDescription());
        playlistDto.setGenre(playlistInfoDto.getGenre());
        return save(playlistId, playlistDto);
    }

    @Override
    public Page<PlaylistBriefDto> getAllPlaylists(Pageable pageable) {
        return repository
                .findAll(pageable)
                .map(briefMapper::toDto);
    }

    @Override
    public Page<TrackDto> getPlaylistTracks(Long playlistId, Pageable pageable) {
        return trackRepository
                .findAllPlaylistTracks(playlistId, pageable)
                .map(trackMapper::toDto);
    }

    @Override
    @Transactional
    public TrackDto addTrackToPlaylist(Long playlistId, Long trackId) {
        Playlist playlist = getEntityByIdOrThrow(playlistId);
        Track track = trackRepository.getOne(trackId);
        playlist.getTracks().add(track);
        repository.save(playlist);
        return trackMapper.toDto(track);
    }

    @Override
    @Transactional
    public TrackDto deleteTrackFromPlaylist(Long playlistId, Long trackId) {
        Playlist playlist = getEntityByIdOrThrow(playlistId);
        Track track = trackRepository.getOne(trackId);
        playlist.getTracks().remove(track);
        repository.save(playlist);
        return trackMapper.toDto(track);
    }

    @Override
    protected JpaRepository<Playlist, Long> getRepository() {
        return repository;
    }

    @Override
    protected Mapper<Playlist, PlaylistDto, Long> getMapper() {
        return mapper;
    }
}
