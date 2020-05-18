package ru.nsu.ccfit.mvcentertainment.communify.backend.jwt.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.mvcentertainment.communify.backend.jwt.JwtTokenInfo;
import ru.nsu.ccfit.mvcentertainment.communify.backend.jwt.JwtTokenUtils;

import java.security.Key;

@Component
public class JwtTokenUtilsImpl implements JwtTokenUtils {

    private static final String USER_ID_CLAIM_NAME = "userId";
    private static final String USER_NAME_CLAIM_NAME = "userName";
    private static final String EXPIRATION_DATE_CLAIM_NAME = "exp";

    private final Key secretKey;
    private final Integer expirationTime;
    private final JwtParser jwtParser;

    @Autowired
    public JwtTokenUtilsImpl(
            @Value("${jwt.base64SecretKey}") String base64SecretKey,
            @Value("${jwt.exptime}") Integer expirationTime
    ) {

        this.expirationTime = expirationTime;

        byte[] keyBytes = Decoders.BASE64.decode(base64SecretKey);
        secretKey = Keys.hmacShaKeyFor(keyBytes);

        jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build();
    }

    @Override
    public boolean validateToken(String token) {
        JwtTokenInfo jwtTokenInfo = parseToken(token);
        return jwtTokenInfo.getExpirationDateMillis() >= System.currentTimeMillis();
    }

    @Override
    public String generateToken(Long userId, String userName) {
        Claims claims = Jwts.claims();
        claims.put(USER_ID_CLAIM_NAME, userId);
        claims.put(USER_NAME_CLAIM_NAME, userName);
        claims.put(EXPIRATION_DATE_CLAIM_NAME, System.currentTimeMillis() + expirationTime);

        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public JwtTokenInfo parseToken(String token) {
        Claims claims = getTokenClaims(token);
        return new JwtTokenInfo(
            claims.get(USER_ID_CLAIM_NAME, Long.class),
            claims.get(USER_NAME_CLAIM_NAME, String.class),
            claims.get(EXPIRATION_DATE_CLAIM_NAME, Long.class)
        );
    }

    private Claims getTokenClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

}
