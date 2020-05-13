package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class TrackInfoDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String author;

    @NotNull
    private String description;

}
