package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.AbstractDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.types.Genre;

import java.util.Date;

@Getter @Setter
public class PlaylistBriefDto extends AbstractDto<Long> {

    private String name;
    private Date creationDate;
    private UserBriefDto owner;
    private Genre genre;

}
