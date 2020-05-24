package ru.nsu.ccfit.mvcentertainment.communify.backend.entities;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "communify_user")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractEntity<Long> {

    @Column(name = "name")
    private String name;

    @Column(name = "bio")
    private String bio;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private final Set<Playlist> ownedPlaylists = new HashSet<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "playlist_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "playlist_id")
    )
    private final Set<Playlist> playlists = new HashSet<>();

}
