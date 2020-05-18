package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class UserInfoDto {

    @NotNull
    private String bio;

}
