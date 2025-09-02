package com.spring_security.service;

import com.spring_security.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    @Value("${security.jwt.expiration-minutes}")
    private long EXPIRATION_MINUTES;

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    public String generateToken(User user, Map<String, Object> extraClaims) {

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date(issuedAt.getTime() + (EXPIRATION_MINUTES  * 60 * 1000));

         return Jwts.builder()
            .claims(extraClaims)
            .subject(user.getName())
            .issuedAt(issuedAt)
            .expiration(expiration)
            .header().type("JWT").and()
            .signWith(generateKey())
            .compact();
    }

    private SecretKey generateKey(){
        byte[] secretAsBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(secretAsBytes);
    }

    public String extractUsername(String jwt) {
        return extractAllClaims(jwt).getSubject();
    }

    private Claims extractAllClaims(String jwt) {
    return Jwts.parser()
            .verifyWith(generateKey())
            .build()
            .parseSignedClaims(jwt)
            .getPayload();
}

}
