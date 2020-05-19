package ru.nsu.ccfit.mvcentertainment.communify.backend.services;

import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.UserDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.parameters.UserAuthInfoDto;

public interface AuthenticationService {

    String loginUser(UserAuthInfoDto userAuthInfoDto);

    UserDto registerUser(UserAuthInfoDto userAuthInfoDto);

}
