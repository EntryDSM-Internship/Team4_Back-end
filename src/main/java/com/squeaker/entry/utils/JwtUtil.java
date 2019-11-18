package com.squeaker.entry.utils;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.UUID;

public class JwtUtil {
    private static final String SECURITY_KEY = "this_is_password";// 하루동안 토큰 유지

    private static String generateToken(String data, Long expire) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        JwtBuilder builder = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(data)
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expire))
                .signWith(SignatureAlgorithm.HS256, SECURITY_KEY);

        return builder.compact();
    }

    public static String getAccessToken(String data) {
        return generateToken(data, 1000L * 3600 * 24);
    }
    public static String getRefreshToken(String data) {
        return generateToken(data, 1000L * 3600 * 24 * 30);
    }

    public static String parseToken(String token) {
        String data = Jwts.parser().setSigningKey(SECURITY_KEY).parseClaimsJws(token).getBody().getSubject();
        return data;
    }
}
