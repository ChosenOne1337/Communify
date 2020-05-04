package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TrackDto extends AbstractDto<Long> {

    private String name;
    private String author;
    private String description;
    private Long duration;

}
