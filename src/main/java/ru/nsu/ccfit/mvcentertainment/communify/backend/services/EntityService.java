package ru.nsu.ccfit.mvcentertainment.communify.backend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

public interface EntityService<D, I extends Serializable> {

    D getById(I id);

    Page<D> getAll(Pageable pageable);

    D create(D dto);

    D save(I id, D dto);

    void deleteById(I id);

}
