package ru.nsu.ccfit.mvcentertainment.communify.backend.entities;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.types.Genre;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "playlist")
@Getter @Setter
public class Playlist extends AbstractEntity<Long> {

    private String name;

    private String description;

    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany
    @JoinTable(
            name = "track_playlist",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id")
    )
    private List<Track> tracks;

    @Column(name = "genre")
    @Enumerated(EnumType.STRING)
    private Genre genre;


}
