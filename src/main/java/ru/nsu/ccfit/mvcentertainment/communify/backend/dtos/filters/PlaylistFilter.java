package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.filters;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.types.Genre;

import java.util.Date;

@Getter @Setter
public class PlaylistFilter {

    private Genre genre;
    private Date minCreationDate;
    private Date maxCreationDate;
    private String name;

}
