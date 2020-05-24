package ru.nsu.ccfit.mvcentertainment.communify.backend.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.AppConfig;
import ru.nsu.ccfit.mvcentertainment.communify.backend.TestEntityFactory;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.TrackDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Track;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl.TrackMapper;

public class TrackMapperTests {

    private static TrackMapper trackMapper;

    private static Track track;
    private static TrackDto trackDto;

    @BeforeAll
    static void setup() {
        ModelMapper modelMapper = new AppConfig().modelMapper();
        trackMapper = new TrackMapper(modelMapper);

        track = TestEntityFactory.createTrack();
        trackDto = TestEntityFactory.createTrackDto(track);
    }

    @Test
    void toDto() {
        Assertions.assertEquals(trackDto, trackMapper.toDto(track));
        Assertions.assertNull(trackMapper.toDto(null));
    }

    @Test
    void fromDto() {
        Assertions.assertEquals(track, trackMapper.toEntity(trackDto));
        Assertions.assertNull(trackMapper.toEntity(null));
    }
}
