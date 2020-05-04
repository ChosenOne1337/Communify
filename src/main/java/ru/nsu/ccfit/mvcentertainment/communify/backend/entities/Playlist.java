package ru.nsu.ccfit.mvcentertainment.communify.backend.entities;

import lombok.Getter;
import lombok.Setter;

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

    @ManyToMany
    @JoinTable(
            name = "playlist_genre",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;


}
