package ru.nsu.ccfit.mvcentertainment.communify.backend.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "communify_user")
@Getter @Setter
public class User extends AbstractEntity<Long> {

    @Column(name = "name")
    private String name;

    @Column(name = "bio")
    private String bio;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Playlist> ownedPlaylists;

    @ManyToMany
    @JoinTable(
            name = "playlist_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id")
    )
    private List<Playlist> playlists;

}
