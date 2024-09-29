package com.example.kanbanbackend.Auth;

import com.example.kanbanbackend.Entitites.Share.User;
import com.example.kanbanbackend.Repository.Share.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Value("#{${jwt.max-token-interval-minutes}*60*1000}")
    private long JWT_TOKEN_VALIDITY;

    @Value("#{${jwt.max-refresh-token-interval-minutes}*60*1000}")
    private long refreshTokenValidity;

    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @Autowired
    UserRepository userRepository;


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

    public Claims getAllClaimsFromToken(String token) {
//        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        }
//        catch (Exception e) {
//            throw new RuntimeException("Invalid JWT token", e);
//        }
//    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", "https://intproj23.sit.kmutt.ac.th/sy2/");
        claims.put("name",user.getName());
        claims.put("oid",user.getOid());
        claims.put("email",user.getEmail());
        claims.put("role","owner");
        return doGenerateToken(claims, user.getUsername(), JWT_TOKEN_VALIDITY);
    }
    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", "https://intproj23.sit.kmutt.ac.th/sy2/");
        claims.put("oid",user.getOid());
        return doGenerateToken(claims, user.getUsername(), refreshTokenValidity);
    }


    // Generate JWT token with claims(any config) and subject(username)
    private String doGenerateToken(Map<String, Object> claims, String subject, long expiration) {
        long expirationTime = (long) Math.floor(System.currentTimeMillis() + expiration);
        if(claims.size() > 2){//gen access_token
            return Jwts.builder()
                    .setHeaderParam("typ", "JWT") // Add header
                    .setClaims(claims) // Set claims
                    .setSubject(subject) // Set the subject (username)
                    .setIssuedAt(new Date()) // Set issued date
                    .setExpiration(new Date(expirationTime)) // Set expiration date
                    .signWith(signatureAlgorithm, SECRET_KEY) // Sign the token with the secret key
                    .compact();
        }else { // gen refresh_token
            return Jwts.builder()
                    .setHeaderParam("typ", "JWT") // Add header
                    .setClaims(claims) // Set claims
                    .setIssuedAt(new Date()) // Set issued date
                    .setExpiration(new Date(expirationTime)) // Set expiration date
                    .signWith(signatureAlgorithm, SECRET_KEY) // Sign the token with the secret key
                    .compact();
        }

    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Claims decodedToken( HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7).trim();
        return getAllClaimsFromToken(token);
    }

}
