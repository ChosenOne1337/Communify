package ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.PlaylistDto;
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

    public PlaylistServiceImpl(
            PlaylistRepository repository,
            Mapper<Playlist, PlaylistDto, Long> mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PlaylistDto create(PlaylistDto playlistDto) {
        Date currentDate = Calendar.getInstance().getTime();
        playlistDto.setCreationDate(currentDate);
        return super.create(playlistDto);
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
