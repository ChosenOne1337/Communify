package ru.nsu.ccfit.mvcentertainment.communify.backend.dtos;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.PlaylistBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Playlist;

import javax.persistence.*;
import java.util.List;

@Getter @Setter
public class UserDto extends AbstractDto<Long> {

    private String name;
    private String bio;
    private List<PlaylistBriefDto> ownedPlaylists;
    private List<PlaylistBriefDto> playlists;

}
