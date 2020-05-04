package ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.UserDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.PlaylistBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Playlist;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.Mapper;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class UserMapper extends AbstractMapper<User, UserDto, Long> {

    private final Mapper<Playlist, PlaylistBriefDto, Long> playlistMapper;

    @Autowired
    public UserMapper(ModelMapper mapper,
                      Mapper<Playlist, PlaylistBriefDto, Long> playlistMapper) {
        super(mapper, User.class, UserDto.class);
        this.playlistMapper = playlistMapper;
    }

    @PostConstruct
    public void setupMapper() {
        skipDtoField(UserDto::setOwnedPlaylists);
        skipDtoField(UserDto::setPlaylists);

        skipEntityField(User::setOwnedPlaylists);
        skipEntityField(User::setPlaylists);
    }

    @Override
    protected void mapSpecificFields(User sourceEntity, UserDto destinationDto) {
        List<PlaylistBriefDto> ownedPlayLists = mapEntityListToDtoList(
                sourceEntity.getOwnedPlaylists(), playlistMapper
        );
        List<PlaylistBriefDto> playLists = mapEntityListToDtoList(
                sourceEntity.getPlaylists(), playlistMapper
        );

        destinationDto.setOwnedPlaylists(ownedPlayLists);
        destinationDto.setPlaylists(playLists);
    }

    @Override
    protected void mapSpecificFields(UserDto sourceDto, User destinationEntity) {
    }

}

