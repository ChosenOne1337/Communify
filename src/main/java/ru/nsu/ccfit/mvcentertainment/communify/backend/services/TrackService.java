package ru.nsu.ccfit.mvcentertainment.communify.backend.services;

import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.TrackDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters.TrackInfoDto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface TrackService extends EntityService<TrackDto, Long> {

    TrackDto uploadTrack(Long playlistId,
                         TrackInfoDto trackInfoDto,
                         InputStream audioFileStream) throws IOException;

    File getTrackFile(Long trackId);

}
