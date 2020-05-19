package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.types.Genre;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class PlaylistInfoDto {

    @NotEmpty
    private String name;

    @NotNull
    private Genre genre;

    @NotNull
    private String description;

}
