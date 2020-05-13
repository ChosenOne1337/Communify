package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.UserBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.types.Genre;

import java.util.Date;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistDto extends AbstractDto<Long> {

    private String name;
    private String description;
    private Date creationDate;
    private UserBriefDto owner;
    private Genre genre;

}
