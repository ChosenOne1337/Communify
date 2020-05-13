package ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.TrackDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Track;

@Component
public class TrackMapper extends AbstractMapper<Track, TrackDto, Long> {

    @Autowired
    public TrackMapper(ModelMapper mapper) {
        super(mapper, Track.class, TrackDto.class);
    }

}