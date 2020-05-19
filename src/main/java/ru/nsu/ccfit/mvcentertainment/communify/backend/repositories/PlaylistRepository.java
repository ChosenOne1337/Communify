package ru.nsu.ccfit.mvcentertainment.communify.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Playlist;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    @Query("select distinct o.id from Playlist p join p.owner o where p.id = :playlistId")
    Long getPlaylistOwnerId(@Param("playlistId") Long playlistId);

    Page<Playlist> findAllByOwnerId(Long ownerId, Pageable pageable);

    @Query("select distinct p from Playlist p join p.users u where u.id = :userId")
    Page<Playlist> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}
