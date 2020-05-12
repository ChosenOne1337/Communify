package ru.nsu.ccfit.mvcentertainment.communify.backend.services;

import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.UserDto;

import java.io.File;
import java.io.InputStream;

public interface UserService extends Service<UserDto, Long> {

    UserDto setUserIcon(Long userId, InputStream imageInputStream);

    File getUserIcon(Long userId);

    UserDto deleteUserIcon(Long userId);
}
