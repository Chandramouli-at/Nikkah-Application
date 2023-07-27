package dev.umar.marriageApp;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.security.SecureRandom;
import java.util.Base64;

public class JwtUtil {

//    private static final String SECRET_KEY = keyGen();

    private static String keyGen() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[32];
        secureRandom.nextBytes(keyBytes);

        // Encode the byte array to Base64 to get the secret key
        String secretKey = Base64.getEncoder().encodeToString(keyBytes);

        return secretKey;
    }

    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    public static String generateToken(String email) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, keyGen());

        return builder.compact();
    }
}
