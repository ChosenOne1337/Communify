package ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.GenreDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.Genre;

@Component
public class GenreMapper extends AbstractMapper<Genre, GenreDto, Long> {

    @Autowired
    public GenreMapper(ModelMapper mapper) {
        super(mapper, Genre.class, GenreDto.class);
    }

}
