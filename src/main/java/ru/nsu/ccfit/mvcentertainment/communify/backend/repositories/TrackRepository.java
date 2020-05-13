package ru.nsu.ccfit.mvcentertainment.communify.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Track;

public interface TrackRepository extends JpaRepository<Track, Long> {
}
