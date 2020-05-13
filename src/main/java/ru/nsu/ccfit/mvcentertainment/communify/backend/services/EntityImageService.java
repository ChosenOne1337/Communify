package ru.nsu.ccfit.mvcentertainment.communify.backend.services;

import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.AbstractDto;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

public interface EntityImageService
        <DTO extends AbstractDto<ID>,
        ID extends Serializable> {

    File getImage(ID entityId);

    DTO setImage(ID entityId, InputStream imageInputStream);

    DTO deleteImage(ID entityId);

}
