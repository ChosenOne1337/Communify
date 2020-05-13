package ru.nsu.ccfit.mvcentertainment.communify.backend.mappers;

import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.AbstractDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.AbstractEntity;

import java.io.Serializable;

public interface Mapper
        <E extends AbstractEntity<ID>,
        DTO extends AbstractDto<ID>,
        ID extends Serializable> {

    E toEntity(DTO dto);

    DTO toDto(E entity);

}
