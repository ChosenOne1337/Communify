package ru.nsu.ccfit.mvcentertainment.communify.backend.mappers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.AppConfig;
import ru.nsu.ccfit.mvcentertainment.communify.backend.TestEntityFactory;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.UserDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.UserBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl.UserBriefMapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl.UserMapper;

public class UserMapperTests {

    private static UserBriefMapper userBriefMapper;
    private static UserMapper userMapper;

    private static User user;
    private static UserDto userDto;
    private static UserBriefDto userBriefDto;

    @BeforeAll
    static void setup() {
        ModelMapper modelMapper = new AppConfig().modelMapper();
        userMapper = new UserMapper(modelMapper);
        userBriefMapper = new UserBriefMapper(modelMapper);

        user = TestEntityFactory.createUser();
        userDto = TestEntityFactory.createUserDto(user);
        userBriefDto = TestEntityFactory.createUserBriefDto(user);
    }

    @Test
    void toDto() {
        Assertions.assertEquals(userDto, userMapper.toDto(user));
        Assertions.assertEquals(userBriefDto, userBriefMapper.toDto(user));
    }

    @Test
    void toEntity() {
        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> userMapper.toEntity(userDto)
        );
        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> userBriefMapper.toEntity(userBriefDto)
        );
    }
}
