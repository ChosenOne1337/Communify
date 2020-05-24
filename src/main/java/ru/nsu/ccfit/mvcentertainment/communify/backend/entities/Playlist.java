package ru.nsu.ccfit.mvcentertainment.communify.backend.entities;

import lombok.*;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.types.Genre;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "playlist")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Playlist extends AbstractEntity<Long> {

    private String name;

    private String description;

    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany(mappedBy = "playlists")
    private final Set<User> users = new HashSet<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "track_playlist",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id")
    )
    private final Set<Track> tracks = new HashSet<>();

    @Column(name = "genre")
    @Enumerated(EnumType.STRING)
    private Genre genre;


}
