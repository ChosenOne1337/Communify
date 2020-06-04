package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief;

import lombok.*;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.AbstractDto;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserBriefDto extends AbstractDto<Long> {

    private String name;

}
