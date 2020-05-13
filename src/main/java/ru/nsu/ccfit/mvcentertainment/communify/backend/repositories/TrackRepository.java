package ru.nsu.ccfit.mvcentertainment.communify.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Track;

public interface TrackRepository extends JpaRepository<Track, Long> {

    @Query("select distinct t from Track t join t.playlists p where p.id = :playlistId")
    Page<Track> findAllPlaylistTracks(@Param("playlistId") Long playlistId, Pageable pageable);

}
