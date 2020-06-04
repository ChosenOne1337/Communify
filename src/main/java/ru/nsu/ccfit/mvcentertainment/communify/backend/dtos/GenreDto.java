package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos;

import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GenreDto extends AbstractDto<Long> {

    private String name;

}
