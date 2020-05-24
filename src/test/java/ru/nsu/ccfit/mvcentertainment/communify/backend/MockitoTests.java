package ru.nsu.ccfit.mvcentertainment.communify.backend;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.mockito.MockitoSession;

public class MockitoTests {

    MockitoSession session;

    @BeforeEach
    void beforeTest() {
        session = Mockito.mockitoSession()
                .initMocks(this)
                .startMocking();
    }

    @AfterEach
    void afterTest() {
        session.finishMocking();
    }
}
