package ru.nsu.ccfit.mvcentertainment.communify.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Track;

public interface TrackRepository
        extends JpaRepository<Track, Long>, JpaSpecificationExecutor<Track> {
}
