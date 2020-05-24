package ru.nsu.ccfit.mvcentertainment.communify.backend.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "track")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Track extends AbstractEntity<Long> {

    @Column(name = "name")
    private String name;

    @Column(name = "author")
    private String author;

    @Column(name = "description")
    private String description;

    @Column(name = "duration")
    private Long duration;

    @ManyToMany(mappedBy = "tracks")
    private final Set<Playlist> playlists = new HashSet<>();

}
