package ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.PlaylistDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.UserBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Playlist;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.Mapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.UserRepository;

import javax.annotation.PostConstruct;

@Component
public class PlaylistMapper extends AbstractMapper<Playlist, PlaylistDto, Long> {

    private final Mapper<User, UserBriefDto, Long> userBriefMapper;
    private final UserRepository userRepository;

    @Autowired
    public PlaylistMapper(
            ModelMapper mapper,
            Mapper<User, UserBriefDto, Long> userBriefMapper,
            UserRepository userRepository
    ) {
        super(mapper, Playlist.class, PlaylistDto.class);
        this.userBriefMapper = userBriefMapper;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void setupMapper() {
        skipDtoField(PlaylistDto::setOwner);
    }

    @Override
    protected void mapSpecificFields(Playlist sourceEntity, PlaylistDto destinationDto) {
        destinationDto.setOwner(userBriefMapper.toDto(sourceEntity.getOwner()));
    }

    @Override
    protected void mapSpecificFields(PlaylistDto sourceDto, Playlist destinationEntity) {
        destinationEntity.setOwner(
                userRepository.findById(sourceDto.getOwner().getId()).orElse(null)
        );
    }

}
