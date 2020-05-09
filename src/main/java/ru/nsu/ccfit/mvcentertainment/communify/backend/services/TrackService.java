package ru.nsu.ccfit.mvcentertainment.communify.backend.services;

import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.TrackDto;

public interface TrackService extends Service<TrackDto, Long> {

    TrackDto uploadTrack(String name,
                         String author,
                         String description,
                         byte[] audioFileBytes);

    byte[] getAudioFileBytes(Long trackId);

}
