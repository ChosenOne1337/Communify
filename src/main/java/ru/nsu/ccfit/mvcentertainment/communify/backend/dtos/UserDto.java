package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos;

import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDto extends AbstractDto<Long> {

    private String name;
    private String bio;

}
