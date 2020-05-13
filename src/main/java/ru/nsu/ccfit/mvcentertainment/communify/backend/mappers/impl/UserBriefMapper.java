package ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.brief.UserBriefDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;

@Component
public class UserBriefMapper extends AbstractMapper<User, UserBriefDto, Long> {

    @Autowired
    public UserBriefMapper(ModelMapper mapper) {
        super(mapper, User.class, UserBriefDto.class);
    }

}
