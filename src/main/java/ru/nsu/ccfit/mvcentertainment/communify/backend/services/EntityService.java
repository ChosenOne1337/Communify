package ru.nsu.ccfit.mvcentertainment.communify.backend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Collection;

public interface EntityService<DTO, ID extends Serializable> {

    long countAll();

    DTO getById(ID id);

    Page<DTO> getAll(Pageable pageable);

    Collection<DTO> getAllById(Collection<ID> idCollection);

    DTO create(DTO dto);

    DTO save(ID id, DTO dto);

    Collection<DTO> saveAll(Collection<DTO> dtoCollection);

    void deleteById(ID id);

    void deleteAllById(Collection<ID> idCollection);

}
