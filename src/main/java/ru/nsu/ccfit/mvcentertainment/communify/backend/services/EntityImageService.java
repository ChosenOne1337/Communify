package ru.nsu.ccfit.mvcentertainment.communify.backend.services;

import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.AbstractDto;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

public interface EntityImageService
        <D extends AbstractDto<I>,
        I extends Serializable> {

    File getImage(I entityId);

    D setImage(I entityId, InputStream imageInputStream);

    D deleteImage(I entityId);

}
