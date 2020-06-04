package ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.UserDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;

@Component
public class UserMapper extends AbstractMapper<User, UserDto, Long> {

    @Autowired
    public UserMapper(ModelMapper mapper) {
        super(mapper, User.class, UserDto.class);
    }

    @Override
    public User toEntity(UserDto userDto) {
        throw new UnsupportedOperationException();
    }

}

