package ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.UserDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.entities.User;
import ru.nsu.ccfit.mvcentertainment.communify.backend.mappers.Mapper;
import ru.nsu.ccfit.mvcentertainment.communify.backend.repositories.UserRepository;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.UserService;
import ru.nsu.ccfit.mvcentertainment.communify.backend.utils.ImageUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
    public File getUserIcon(Long userId) {
        UserDto userDto = mapper.toDto(getEntityByIdOrThrow(userId));
        String iconFileName = getIconFileNameFromDto(userDto);
        File iconFile = new File(iconDirectoryPath, iconFileName);
        if (!iconFile.exists()) {
            throw new RuntimeException(
                    String.format("Icon file for user id '%d' does not exist", userId)
            );
        }

        return iconFile;
    }

    @Override
    @SneakyThrows
    public UserDto deleteUserIcon(Long userId) {
        UserDto userDto = mapper.toDto(getEntityByIdOrThrow(userId));
        String iconFileName = getIconFileNameFromDto(userDto);
        File iconFile = new File(iconDirectoryPath, iconFileName);

        boolean isDeleted = Files.deleteIfExists(iconFile.toPath());
        if (!isDeleted) {
            throw new RuntimeException(
                    String.format("Icon file for user id '%d' does not exist", userId)
            );
        }

        return userDto;
    }

    @Override
    public UserDto setUserIcon(Long userId, InputStream imageInputStream) {
        File tempIconFile = null;
        try {
            tempIconFile = createTempIconFile(imageInputStream);
            ImageUtils.validateImage(tempIconFile);
            ImageUtils.scaleImage(tempIconFile, iconWidth, iconHeight, iconFormat);
            return setUserIcon(userId, tempIconFile);
        } catch (Exception e) {
            if (tempIconFile != null) {
                tempIconFile.delete();
            }

            throw new RuntimeException(e);
        }

    }

    private UserDto setUserIcon(Long userId, File iconFile) throws IOException {
        UserDto userDto = mapper.toDto(getEntityByIdOrThrow(userId));
        String iconFileName = getIconFileNameFromDto(userDto);
        File renamedIconFile = new File(iconDirectoryPath, iconFileName);
        Files.move(iconFile.toPath(), renamedIconFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return userDto;
    }

    private File createTempIconFile(InputStream imageInputStream) throws IOException {
        File iconFile = File.createTempFile(
                TEMP_FILE_PREFIX, TEMP_FILE_SUFFIX, iconDirectoryPath
        );

        Files.copy(imageInputStream, iconFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        imageInputStream.close();

        return iconFile;
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
