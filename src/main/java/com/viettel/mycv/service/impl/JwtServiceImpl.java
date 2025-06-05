package com.viettel.mycv.service.impl;

import com.viettel.mycv.exception.InvalidDataException;
import com.viettel.mycv.model.UserEntity;
import com.viettel.mycv.service.JwtService;
import com.viettel.mycv.service.MyUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j(topic="JWT-SERVICE")
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${JWT_ACCESS_KEY}")
    private String accessKey;

    @Value("${JWT_REFRESH_KEY}")
    private String refreshKey;

    @Value("${ACCESS_KEY_EXPIRE_MINUTE}")
    private long accessKeyExpireMinute;

    @Value("${REFRESH_KEY_EXPIRE_DAY}")
    private long refreshKeyExpireDay;

    private final MyUserDetailsService myUserDetailsService;

    @Override
    public String generateAccessToken(String email, List<String> authorities) {
        log.info("Generating access token");
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", authorities);

        log.info("Finish Generating access token");
        return createAccessToken(email, claims);
    }

    @Override
    public String generateRefreshToken(String email, List<String> authorities) {
        log.info("Generating refresh token");
        Map<String, Object> claims = new HashMap<>();

        claims.put("role", authorities);
        log.info("Finish Generating refresh token");
        return createRefreshToken(email, claims);
    }

    @Override
    public String extractEmail(String token, String keyType) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey(keyType))
                .build()
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }


    private String createAccessToken(String email, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessKeyExpireMinute * 60 * 1000))
                .signWith(getKey("access-key"), SignatureAlgorithm.HS256)
                .compact();
    }

    private String createRefreshToken(String email, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshKeyExpireDay * 24 * 60 * 60 * 1000))
                .signWith(getKey("refresh-key"), SignatureAlgorithm.HS256)
                .compact();
    }

    private SecretKey getKey(String type) {
        return switch (type) {
            case "access-key" -> Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey));
            case "refresh-key" -> Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
            default -> throw new InvalidDataException("Invalid token type");
        };
    }
}
