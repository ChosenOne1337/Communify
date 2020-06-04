package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos;

import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TrackDto extends AbstractDto<Long> {

    private String name;
    private String author;
    private String description;
    private Long duration;

}
