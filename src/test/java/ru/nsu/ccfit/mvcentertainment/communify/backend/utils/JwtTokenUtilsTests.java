package ru.nsu.ccfit.mvcentertainment.communify.backend.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.ccfit.mvcentertainment.communify.backend.security.JwtTokenInfo;
import ru.nsu.ccfit.mvcentertainment.communify.backend.security.JwtTokenUtils;
import ru.nsu.ccfit.mvcentertainment.communify.backend.security.exceptions.JwtValidationException;
import ru.nsu.ccfit.mvcentertainment.communify.backend.security.impl.JwtTokenUtilsImpl;

public class JwtTokenUtilsTests {

    static String secretKey = "Y3NlcmduY2lzZXVna2J1dnlzbmJrZ" +
            "WN1cm14dmd1ZW52Z2p4dG1ldnJ5amd4dnN1d" +
            "GVndm5hbXV5ZXZndWxpZ2xpeWd3eWd3Z==";

    @Test
    void testUsualScenario() {
        int expirationTime = 1000000000;
        JwtTokenUtils jwtTokenUtils = new JwtTokenUtilsImpl(secretKey, expirationTime);

        Long userId = 42L;
        String username = "username";
        Long estimatedExpirationTime = System.currentTimeMillis() + expirationTime;

        String token = jwtTokenUtils.generateToken(userId, username);
        Assertions.assertTrue(jwtTokenUtils.validateToken(token));

        JwtTokenInfo tokenInfo = jwtTokenUtils.parseToken(token);
        Assertions.assertEquals(tokenInfo.getUserId(), userId);
        Assertions.assertEquals(tokenInfo.getUserName(), username);
        Assertions.assertTrue(tokenInfo.getExpirationDateMillis() >= estimatedExpirationTime);

    }

    @Test
    void testTokenExpiration() throws InterruptedException {
        int expirationTime = 1000;
        JwtTokenUtils jwtTokenUtils = new JwtTokenUtilsImpl(secretKey, expirationTime);

        String token = jwtTokenUtils.generateToken(42L, "username");
        Thread.sleep(expirationTime);
        Assertions.assertFalse(jwtTokenUtils.validateToken(token));
        Assertions.assertThrows(
                JwtValidationException.class,
                () -> jwtTokenUtils.parseToken(token)
        );
    }

    @Test
    void testTokenInvalidationOnModification() {
        JwtTokenUtils jwtTokenUtils = new JwtTokenUtilsImpl(secretKey, 1000000);
        String token = jwtTokenUtils.generateToken(42L, "username");

        String modifiedToken = token.substring(1);
        Assertions.assertFalse(jwtTokenUtils.validateToken(modifiedToken));
        Assertions.assertThrows(
                JwtValidationException.class,
                () -> jwtTokenUtils.parseToken(modifiedToken)
        );
    }

}
