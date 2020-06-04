package ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.PlaylistBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.UserBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Playlist;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.Mapper;

import javax.annotation.PostConstruct;

@Component
public class PlaylistBriefMapper extends AbstractMapper<Playlist, PlaylistBriefDto, Long> {

    private final Mapper<User, UserBriefDto, Long> userBriefMapper;

    @Autowired
    public PlaylistBriefMapper(
            ModelMapper mapper,
            Mapper<User, UserBriefDto, Long> userBriefMapper
    ) {
        super(mapper, Playlist.class, PlaylistBriefDto.class);
        this.userBriefMapper = userBriefMapper;
    }

    @PostConstruct
    public void setupMapper() {
        skipDtoField(PlaylistBriefDto::setOwner);
    }

    @Override
    protected void mapSpecificFields(Playlist sourceEntity, PlaylistBriefDto destinationDto) {
        destinationDto.setOwner(userBriefMapper.toDto(sourceEntity.getOwner()));
    }

    @Override
    public Playlist toEntity(PlaylistBriefDto playlistBriefDto) {
        throw new UnsupportedOperationException();
    }

}
