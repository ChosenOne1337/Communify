package ru.nsu.ccfit.mvcentertainment.communify.backend.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "track")
@Getter @Setter
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
    private List<Playlist> playlists;

}
