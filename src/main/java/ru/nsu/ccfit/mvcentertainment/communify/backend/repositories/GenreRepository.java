package ru.nsu.ccfit.mvcentertainment.communify.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Genre;

public interface GenreRepository
        extends JpaRepository<Genre, Long>, JpaSpecificationExecutor<Genre> {
}
