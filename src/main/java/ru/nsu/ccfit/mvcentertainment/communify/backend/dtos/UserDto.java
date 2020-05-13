package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDto extends AbstractDto<Long> {

    private String name;
    private String bio;

}
