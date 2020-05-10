package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackDto extends AbstractDto<Long> {

    private String name;
    private String author;
    private String description;
    private Long duration;

}
