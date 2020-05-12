package ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.UserDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.EntityService;

@Service
public class UserIconService
    extends AbstractEntityImageService<UserDto, Long> {

    @Autowired
    public UserIconService(
            @Value("${custom.user.icons.dirpath}") String iconDirectoryPath,
            @Value("${custom.user.icons.width}") Integer iconWidth,
            @Value("${custom.user.icons.height}") Integer iconHeight,
            @Value("${custom.user.icons.format}") String iconFormat,
            EntityService<UserDto, Long> entityService
    ) {
        super(iconFormat, iconWidth, iconHeight, iconDirectoryPath, entityService);
    }

}
