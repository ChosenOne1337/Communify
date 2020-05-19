package ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.AbstractDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.EntityImageService;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.EntityService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Getter(value = AccessLevel.PROTECTED)
public abstract class AbstractEntityImageService
        <DTO extends AbstractDto<ID>,
        ID extends Serializable>
        implements EntityImageService<DTO, ID> {

    private static final String TEMP_FILE_PREFIX = "image";
    private static final String TEMP_FILE_SUFFIX = null;

    private final String imageFormat;
    private final Integer imageWidth;
    private final Integer imageHeight;
    private final File imageDirectoryPath;

    private final EntityService<DTO, ID> entityService;

    protected AbstractEntityImageService(
            String imageFormat,
            Integer imageWidth,
            Integer imageHeight,
            String imageDirectoryPath,
            EntityService<DTO, ID> entityService
    ) {

        this.imageFormat = imageFormat;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.imageDirectoryPath = new File(imageDirectoryPath);
        this.entityService = entityService;

        init();
    }

    @Override
    public File getImage(ID entityId) {
        DTO dto = entityService.getById(entityId);
        String imageFileName = getImageFileNameFromDto(dto);
        File imageFile = new File(imageDirectoryPath, imageFileName);

        if (!imageFile.exists()) {
            throw new RuntimeException(
                    String.format("Image file for entity with id '%s' does not exist", entityId)
            );
        }

        return imageFile;
    }

    @Override
    public DTO setImage(ID entityId, InputStream imageInputStream) {
        File tempImageFile = null;
        try (imageInputStream) {
            tempImageFile = createTempImageFile(imageInputStream);
            ImageUtils.validateImage(tempImageFile);
            ImageUtils.scaleImage(tempImageFile, imageWidth, imageHeight, imageFormat);

            DTO dto = entityService.getById(entityId);
            String imageFileName = getImageFileNameFromDto(dto);
            File renamedImageFile = new File(imageDirectoryPath, imageFileName);
            Files.move(
                    tempImageFile.toPath(), renamedImageFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );

            return dto;
        } catch (Exception e) {
            if (tempImageFile != null) {
                tempImageFile.delete();
            }

            throw new RuntimeException(e);
        }
    }

    @Override
    @SneakyThrows
    public DTO deleteImage(ID entityId) {
        DTO dto = entityService.getById(entityId);
        String imageFileName = getImageFileNameFromDto(dto);
        File imageFile = new File(imageDirectoryPath, imageFileName);
        boolean isDeleted = Files.deleteIfExists(imageFile.toPath());
        if (!isDeleted) {
            throw new RuntimeException(
                    String.format("Image file for entity with id '%s' does not exist", entityId)
            );
        }

        return dto;
    }

    protected String getImageFileNameFromDto(DTO dto) {
        return String.format("%s.%s", dto.getId(), imageFormat);
    }

    private File createTempImageFile(InputStream imageInputStream) throws IOException {
        File iconFile = File.createTempFile(
                TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX, imageDirectoryPath
        );

        Files.copy(imageInputStream, iconFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        imageInputStream.close();

        return iconFile;
    }

    private void init() {
        if (imageDirectoryPath.exists()) {
            return;
        }

        boolean isCreated = imageDirectoryPath.mkdirs();
        if (!isCreated) {
            throw new RuntimeException(
                    String.format("Failed to create %s", imageDirectoryPath.getAbsolutePath())
            );
        }
    }
}
