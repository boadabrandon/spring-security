package com.spring_security.service;

import com.spring_security.entity.User;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${security.jwt.expiration-minutes}")
    private long EXPIRATION_MINUTES;

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    public String generateToken(User user, Map<String, Object> extraClaims) {

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date(issuedAt.getTime() + (EXPIRATION_MINUTES  * 60 * 1000));

        //VERSION NO DEPRECADA
        Jwts.builder()
                .claims()
                    .add(extraClaims)
                    .and()
                .claim("sub", user.getUsername())
                .claim("iat", issuedAt)
                .claim("exp", expiration)
                .header()
                    .add("typ", "JWT")
                    .and()
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();

        //VERSION DEPRECADA
        Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getName())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key generateKey(){

        byte[] secretAsBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(secretAsBytes);
    }
}
