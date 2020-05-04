package ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.impl;

import lombok.Getter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.DestinationSetter;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.AbstractDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.AbstractEntity;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.Mapper;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractMapper
        <E extends AbstractEntity<ID>,
        DTO extends AbstractDto<ID>,
        ID extends Serializable>
        implements Mapper<E, DTO, ID> {

    @Getter
    private final ModelMapper mapper;
    private final TypeMap<E, DTO> entityToDtoTypeMap;
    private final TypeMap<DTO, E> dtoToEntityTypeMap;

    private final Class<E> entityClass;
    private final Class<DTO> dtoClass;

    protected AbstractMapper(ModelMapper mapper, Class<E> entityClass, Class<DTO> dtoClass) {
        this.mapper = mapper;
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;

        entityToDtoTypeMap = mapper
                .createTypeMap(entityClass, dtoClass)
                .setPostConverter(toDtoConverter());
        dtoToEntityTypeMap = mapper
                .createTypeMap(dtoClass, entityClass)
                .setPostConverter(toEntityConverter());
    }

    @Override
    public E toEntity(DTO dto) {
        return Objects.isNull(dto)
                ? null
                : mapper.map(dto, entityClass);
    }

    @Override
    public DTO toDto(E entity) {
        return Objects.isNull(entity)
                ? null
                : mapper.map(entity, dtoClass);
    }

    private Converter<E, DTO> toDtoConverter() {
        return context -> {
            E source = context.getSource();
            DTO destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    private Converter<DTO, E> toEntityConverter() {
        return context -> {
            DTO source = context.getSource();
            E destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    protected <V> void skipDtoField(DestinationSetter<DTO,V> destinationSetter) {
        entityToDtoTypeMap.addMappings(m -> m.skip(destinationSetter));
    }

    protected <V> void skipEntityField(DestinationSetter<E,V> destinationSetter) {
        dtoToEntityTypeMap.addMappings(m -> m.skip(destinationSetter));
    }

    protected <T extends AbstractEntity<I>, S extends AbstractDto<I>, I extends Serializable>
                    List<S> mapEntityListToDtoList(List<T> entityList, Mapper<T, S, I> mapper) {

        List<T> notNullEntityList = Objects.requireNonNullElse(
                entityList, Collections.emptyList()
        );

        return notNullEntityList
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    protected void mapSpecificFields(E sourceEntity, DTO destinationDto) {
    }

    protected void mapSpecificFields(DTO sourceDto, E destinationEntity) {
    }
}
