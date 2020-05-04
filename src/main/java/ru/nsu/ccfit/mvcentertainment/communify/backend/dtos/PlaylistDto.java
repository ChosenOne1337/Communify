package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.UserBriefDto;

import java.util.Date;
import java.util.List;

@Getter @Setter
public class PlaylistDto extends AbstractDto<Long> {

    private String name;
    private String description;
    private Date creationDate;
    private UserBriefDto owner;
    private List<GenreDto> genres;
    private List<TrackDto> tracks;

}
