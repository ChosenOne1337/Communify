package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters;

import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserAuthInfoDto {

    private String name;
    private String password;

}
