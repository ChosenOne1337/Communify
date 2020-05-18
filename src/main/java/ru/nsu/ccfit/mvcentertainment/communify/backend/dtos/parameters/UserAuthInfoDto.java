package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthInfoDto {

    private String userName;
    private String password;

}
