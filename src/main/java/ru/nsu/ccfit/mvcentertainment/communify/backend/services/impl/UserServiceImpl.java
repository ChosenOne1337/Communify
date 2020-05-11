package ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.UserDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.Mapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.UserRepository;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.UserService;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.Objects;

@Service
public class UserServiceImpl
    extends AbstractService<User, UserDto, Long>
    implements UserService {

    private static final String TEMP_FILE_PREFIX = "icon";
    private static final String TEMP_FILE_SUFFIX = null;

    private final String iconFormat;
    private final Integer iconWidth;
    private final Integer iconHeight;
    private final File iconDirectoryPath;

    private final UserRepository repository;
    private final Mapper<User, UserDto, Long> mapper;

    @Autowired
    public UserServiceImpl(
            UserRepository repository,
            Mapper<User, UserDto, Long> mapper,
            @Value("${custom.user.icons.dirpath}") String iconDirectoryPath,
            @Value("${custom.user.icons.width}") Integer iconWidth,
            @Value("${custom.user.icons.height}") Integer iconHeight,
            @Value("${custom.user.icons.format}") String iconFormat
    ) {
        this.repository = repository;
        this.mapper = mapper;

        this.iconDirectoryPath = new File(iconDirectoryPath);
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
        this.iconFormat = iconFormat;

        init();
    }

    @Override
    public UserDto create(UserDto userDto) {
        userDto.setBio(Objects.requireNonNullElse(userDto.getBio(), ""));
        return super.create(userDto);
    }

    @Override
    public UserDto setUserIcon(Long userId, InputStream imageInputStream) {
        File tempIconFile = null;
        try {
            tempIconFile = createTempIconFile(imageInputStream);
            validateImage(tempIconFile);
            scaleImage(tempIconFile, iconWidth, iconHeight, iconFormat);
            return setUserIcon(userId, tempIconFile);
        } catch (Exception e) {
            if (tempIconFile != null) {
                tempIconFile.delete();
            }

            throw new RuntimeException(e);
        }

    }

    @Override
    public File getUserIcon(Long userId) {
        return null;
    }

    private File createTempIconFile(InputStream imageInputStream) throws IOException {
        File iconFile = File.createTempFile(
                TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX, iconDirectoryPath
        );

        Files.copy(imageInputStream, iconFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        imageInputStream.close();

        return iconFile;
    }

    private UserDto setUserIcon(Long userId, File iconFile) throws IOException {
        UserDto userDto = mapper.toDto(getEntityByIdOrThrow(userId));
        String iconFileName = getIconFileNameFromDto(userDto);
        File renamedIconFile = new File(iconDirectoryPath, iconFileName);
        Files.move(iconFile.toPath(), renamedIconFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return userDto;
    }

    private static void scaleImage(
            File imageFile,
            Integer width,
            Integer height,
            String outputImageFormat
    ) throws IOException {
        BufferedImage imageToScale = ImageIO.read(imageFile);
        BufferedImage scaledImage = new BufferedImage(width, height, imageToScale.getType());
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR
        );
        graphics2D.setRenderingHint(
                RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY
        );
        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
        );

        graphics2D.drawImage(imageToScale, 0, 0, width, height, null);
        graphics2D.dispose();
        ImageIO.write(scaledImage, outputImageFormat, imageFile);
    }

    private static void validateImage(File imageFile) throws IOException {
        try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(imageFile)) {
            Iterator<ImageReader> imageReaderIterator = ImageIO.getImageReaders(imageInputStream);
            if (!imageReaderIterator.hasNext()) {
                throw new RuntimeException("Image has an unknown format");
            };
        }
    }

    private String getIconFileNameFromDto(UserDto userDto) {
        return String.format("%d.%s", userDto.getId(), iconFormat);
    }

    private void init() {
        if (iconDirectoryPath.exists()) {
            return;
        }

        boolean isCreated = iconDirectoryPath.mkdirs();
        if (!isCreated) {
            throw new RuntimeException(
                    String.format("Failed to create %s", iconDirectoryPath.getAbsolutePath())
            );
        }
    }

    @Override
    protected JpaRepository<User, Long> getRepository() {
        return repository;
    }

    @Override
    protected Mapper<User, UserDto, Long> getMapper() {
        return mapper;
    }
}
