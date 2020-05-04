package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief;

import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.AbstractDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.GenreDto;

import java.util.Date;
import java.util.List;

public class PlaylistBriefDto extends AbstractDto<Long> {

    private String name;
    private Date creationDate;
    private UserBriefDto owner;
    private List<GenreDto> genres;

}
