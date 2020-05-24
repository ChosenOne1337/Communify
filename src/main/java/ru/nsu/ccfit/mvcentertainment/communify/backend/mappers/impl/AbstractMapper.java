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
import java.util.Objects;

public abstract class AbstractMapper
        <E extends AbstractEntity<I>,
        D extends AbstractDto<I>,
        I extends Serializable>
        implements Mapper<E, D, I> {

    @Getter
    private final ModelMapper mapper;
    private final TypeMap<E, D> entityToDtoTypeMap;
    private final TypeMap<D, E> dtoToEntityTypeMap;

    private final Class<E> entityClass;
    private final Class<D> dtoClass;

    protected AbstractMapper(ModelMapper mapper, Class<E> entityClass, Class<D> dtoClass) {
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
    public E toEntity(D dto) {
        return Objects.isNull(dto)
                ? null
                : mapper.map(dto, entityClass);
    }

    @Override
    public D toDto(E entity) {
        return Objects.isNull(entity)
                ? null
                : mapper.map(entity, dtoClass);
    }

    private Converter<E, D> toDtoConverter() {
        return context -> {
            E source = context.getSource();
            D destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    private Converter<D, E> toEntityConverter() {
        return context -> {
            D source = context.getSource();
            E destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    protected <V> void skipDtoField(DestinationSetter<D,V> destinationSetter) {
        entityToDtoTypeMap.addMappings(m -> m.skip(destinationSetter));
    }

    protected <V> void skipEntityField(DestinationSetter<E,V> destinationSetter) {
        dtoToEntityTypeMap.addMappings(m -> m.skip(destinationSetter));
    }

    protected void mapSpecificFields(E sourceEntity, D destinationDto) {
    }

    protected void mapSpecificFields(D sourceDto, E destinationEntity) {
    }
}
