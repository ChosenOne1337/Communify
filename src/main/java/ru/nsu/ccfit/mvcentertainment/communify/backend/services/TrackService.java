package ru.nsu.ccfit.mvcentertainment.communify.backend.services;

import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.TrackDto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface TrackService extends EntityService<TrackDto, Long> {

    TrackDto uploadTrack(String name,
                         String author,
                         String description,
                         InputStream audioFileStream) throws IOException;

    File getTrackFile(Long trackId);

}
