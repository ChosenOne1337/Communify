package ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.AbstractDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.AbstractEntity;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.Mapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.EntityService;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;

public abstract class AbstractService
        <E extends AbstractEntity<I>,
        D extends AbstractDto<I>,
        I extends Serializable>
        implements EntityService<D, I> {

    protected abstract JpaRepository<E, I> getUserRepository();
    protected abstract Mapper<E, D, I> getMapper();

    @Override
    public D getById(I id) {
        E entity = getEntityByIdOrThrow(id);
        return getMapper().toDto(entity);
    }

    @Override
    public Page<D> getAll(Pageable pageable) {
        return getUserRepository()
                .findAll(pageable)
                .map(getMapper()::toDto);
    }

    @Override
    @Transactional
    public D create(D dto) {
        var entity = getMapper().toEntity(dto);
        entity = getUserRepository().save(entity);
        return getMapper().toDto(entity);
    }

    @Override
    @Transactional
    public D save(I id, D dto) {
        dto.setId(id);
        return create(dto);
    }

    @Override
    public void deleteById(I id) {
        getUserRepository().deleteById(id);
    }

    protected E getEntityByIdOrThrow(I id) {
        return getEntityByIdOrThrow(getUserRepository(), id);
    }

    protected <X, I> X getEntityByIdOrThrow(JpaRepository<X, I> repository, I id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Entity with id '%s' was not found", id)
                )
        );
    }

}
