package ru.nsu.ccfit.mvcentertainment.communify.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);

    boolean existsByName(String name);
}

