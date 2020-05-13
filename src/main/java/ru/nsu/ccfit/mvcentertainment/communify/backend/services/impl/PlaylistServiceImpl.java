package ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.PlaylistDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.PlaylistBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Playlist;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.Mapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.PlaylistRepository;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.PlaylistService;

import java.util.Calendar;
import java.util.Date;

@Service
public class PlaylistServiceImpl
        extends AbstractService<Playlist, PlaylistDto, Long>
        implements PlaylistService {

    private final PlaylistRepository repository;
    private final Mapper<Playlist, PlaylistDto, Long> mapper;
    private final Mapper<Playlist, PlaylistBriefDto, Long> briefMapper;

    public PlaylistServiceImpl(
            PlaylistRepository repository,
            Mapper<Playlist, PlaylistDto, Long> mapper,
            Mapper<Playlist, PlaylistBriefDto, Long> briefMapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.briefMapper = briefMapper;
    }

    @Override
    public PlaylistDto create(PlaylistDto playlistDto) {
        Date currentDate = Calendar.getInstance().getTime();
        playlistDto.setCreationDate(currentDate);
        return super.create(playlistDto);
    }

    @Override
    public Page<PlaylistBriefDto> getPlaylists(Pageable pageable) {
        return repository
                .findAll(pageable)
                .map(briefMapper::toDto);
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
