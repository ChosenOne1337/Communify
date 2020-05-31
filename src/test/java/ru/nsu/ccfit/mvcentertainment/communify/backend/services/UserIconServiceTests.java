package ru.nsu.ccfit.mvcentertainment.communify.backend.services;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.nsu.ccfit.mvcentertainment.communify.backend.TestEntityFactory;
import ru.nsu.ccfit.mvcentertainment.communify.backend.dtos.UserDto;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.exceptions.ResourceException;
import ru.nsu.ccfit.mvcentertainment.communify.backend.services.impl.UserIconServiceImpl;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserIconServiceTests {

    @Mock
    UserService userService;

    static Path tempDirectory;

    UserIconService userIconService;
    static final Long existingUserId = 42L;

    @BeforeAll
    @SneakyThrows
    static void setup() {
        tempDirectory = Files.createTempDirectory("temp");
        tempDirectory.toFile().deleteOnExit();
    }

    @BeforeEach
    void initService() {
        MockitoAnnotations.initMocks(this);

        userIconService = new UserIconServiceImpl(
            tempDirectory.toFile().getAbsolutePath(),
            64,
            64,
            "png",
            userService
        );

        Mockito.when(userService.getById(
                Mockito.any()
        )).then(invocation -> {
            Long id = invocation.getArgument(0);
            if (!existingUserId.equals(id)) {
                throw new EntityNotFoundException();
            }

            UserDto existingUserDto = TestEntityFactory.createUserDto(
                    TestEntityFactory.createUser(existingUserId)
            );

            return existingUserDto;
        });
    }

    @Test
    @SneakyThrows
    public void test() {
        // get nonexistent icon
        Assertions.assertThrows(
                ResourceException.class,
                () -> userIconService.getImage(existingUserId)
        );

        // set invalid user icon
        byte[] imageBytes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        InputStream invalidImageInputStream = new ByteArrayInputStream(imageBytes);
        Assertions.assertThrows(
                ResourceException.class,
                () -> userIconService.setImage(existingUserId, invalidImageInputStream)
        );

        // make throw IOException internally, and then ResourceException as result
        InputStream throwingInputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        };
        Assertions.assertThrows(
                ResourceException.class,
                () -> userIconService.setImage(existingUserId, throwingInputStream)
        );

        // set user icon
        Path testIconPath = Paths.get("src", "test", "resources", "test_icon.png");
        try (var imageInputStream = Files.newInputStream(testIconPath)) {
            userIconService.setImage(existingUserId, imageInputStream);
        }

        // get existent icon
        File iconFile = userIconService.getImage(existingUserId);
        Assertions.assertTrue(iconFile.exists());

        // delete icon
        userIconService.deleteImage(existingUserId);
        Assertions.assertThrows(
                ResourceException.class,
                () -> userIconService.getImage(existingUserId)
        );
        Assertions.assertThrows(
            ResourceException.class,
                () -> userIconService.deleteImage(existingUserId)
        );
    }

    @AfterAll
    @SneakyThrows
    static void cleanup() {
        for (Path path : Files.newDirectoryStream(tempDirectory)) {
            Files.delete(path);
        }
        Files.delete(tempDirectory);
    }
}
