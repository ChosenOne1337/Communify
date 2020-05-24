package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserInfoDto {

    @NotNull
    private String bio;

}
