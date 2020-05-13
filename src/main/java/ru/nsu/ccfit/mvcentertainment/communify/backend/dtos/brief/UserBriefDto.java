package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.AbstractDto;

@Getter @Setter
public class UserBriefDto extends AbstractDto<Long> {

    private String name;

}
