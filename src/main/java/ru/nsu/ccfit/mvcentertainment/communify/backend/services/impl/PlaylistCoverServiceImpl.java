package ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.PlaylistDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.EntityService;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.PlaylistCoverService;

@Service
public class PlaylistCoverServiceImpl
        extends AbstractEntityImageService<PlaylistDto, Long>
        implements PlaylistCoverService {

    protected PlaylistCoverServiceImpl(
            @Value("${custom.playlist.cover.dirpath}") String coverDirectoryPath,
            @Value("${custom.playlist.cover.width}") Integer coverWidth,
            @Value("${custom.playlist.cover.height}") Integer coverHeight,
            @Value("${custom.playlist.cover.format}") String coverFormat,
            EntityService<PlaylistDto, Long> entityService
    ) {
        super(coverFormat, coverWidth, coverHeight, coverDirectoryPath, entityService);
    }
}
