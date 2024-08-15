package com.example.kanbanbackend.Auth;

import com.example.kanbanbackend.Entitites.Share.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Service
public class JwtTokenUtil implements Serializable {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.max-token-interval-hour}")
    private long JWT_TOKEN_VALIDITY;


    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        System.out.println(user);
        claims.put("iss", "https://intproj23.sit.kmutt.ac.th/sy2/");
        claims.put("name",user.getName());
        claims.put("oid",user.getOid());
        claims.put("email",user.getEmail());
        claims.put("role",user.getRole());
        return doGenerateToken(claims, user.getUsername());
    }


    // Generate JWT token with claims(any config) and subject(username)
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims) // Set claims
                .setSubject(subject) // Set the subject (username)
                .setIssuedAt(new Date()) // Set issued date
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY)) // Set expiration date
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY) // Sign the token with the secret key
                .compact();
    }

    public Boolean isTokenValid(String token, String username) {
        final String tokenUsername = getUsernameFromToken(token);
        return (
                tokenUsername.equals(username)
                        && !isTokenExpired(token));
    }

}
