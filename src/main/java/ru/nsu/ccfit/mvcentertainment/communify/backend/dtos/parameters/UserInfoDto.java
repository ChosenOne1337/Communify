package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
@Builder
@EqualsAndHashCode
public class UserInfoDto {

    @NotNull
    private String bio;

}
