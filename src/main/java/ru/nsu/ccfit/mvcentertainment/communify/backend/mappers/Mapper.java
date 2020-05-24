package ru.nsu.ccfit.mvcentertainment.communify.backend.mappers;

import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.AbstractDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.AbstractEntity;

import java.io.Serializable;

public interface Mapper
        <E extends AbstractEntity<I>,
        D extends AbstractDto<I>,
        I extends Serializable> {

    E toEntity(D dto);

    D toDto(E entity);

}
