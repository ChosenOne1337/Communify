package ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.UserDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.Mapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.UserRepository;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.UserService;

import java.util.Objects;

@Service
public class UserServiceImpl
    extends AbstractService<User, UserDto, Long>
    implements UserService {

    private final UserRepository repository;
    private final Mapper<User, UserDto, Long> mapper;

    @Autowired
    public UserServiceImpl(
            UserRepository repository,
            Mapper<User, UserDto, Long> mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UserDto create(UserDto userDto) {
        userDto.setBio(Objects.requireNonNullElse(userDto.getBio(), ""));
        return super.create(userDto);
    }

    @Override
    protected JpaRepository<User, Long> getRepository() {
        return repository;
    }

    @Override
    protected Mapper<User, UserDto, Long> getMapper() {
        return mapper;
    }

}
