package ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.PlaylistDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.TrackDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.UserBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Playlist;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Track;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.Mapper;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class PlaylistMapper extends AbstractMapper<Playlist, PlaylistDto, Long> {

    private final Mapper<User, UserBriefDto, Long> userMapper;
    private final Mapper<Track, TrackDto, Long> trackMapper;
    private final JpaRepository<User, Long> userRepository;

    @Autowired
    public PlaylistMapper(ModelMapper mapper,
                          Mapper<User, UserBriefDto, Long> userMapper,
                          Mapper<Track, TrackDto, Long> trackMapper,
                          JpaRepository<User, Long> userRepository) {
        super(mapper, Playlist.class, PlaylistDto.class);
        this.userMapper = userMapper;
        this.trackMapper = trackMapper;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void setupMapper() {
        skipDtoField(PlaylistDto::setOwner);
        skipDtoField(PlaylistDto::setTracks);

        skipEntityField(Playlist::setOwner);
        skipEntityField(Playlist::setTracks);
    }

    @Override
    protected void mapSpecificFields(Playlist sourceEntity, PlaylistDto destinationDto) {
        destinationDto.setOwner(userMapper.toDto(sourceEntity.getOwner()));

        List<TrackDto> tracks = mapEntityListToDtoList(
                sourceEntity.getTracks(), trackMapper
        );
        destinationDto.setTracks(tracks);
    }

    @Override
    protected void mapSpecificFields(PlaylistDto sourceDto, Playlist destinationEntity) {
        destinationEntity.setOwner(userRepository.getOne(sourceDto.getId()));
    }

}
